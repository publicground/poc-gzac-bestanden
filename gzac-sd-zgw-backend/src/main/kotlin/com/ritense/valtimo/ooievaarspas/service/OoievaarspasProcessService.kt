package com.ritense.valtimo.ooievaarspas.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.treeToValue
import com.ritense.authorization.AuthorizationContext.Companion.runWithoutAuthorization
import com.ritense.document.domain.Document
import com.ritense.document.service.DocumentService
import com.ritense.valtimoplugins.suwinet.service.DateTimeService
import com.ritense.zakenapi.service.ZaakDocumentService
import java.util.UUID

class OoievaarspasProcessService(
    private val documentService: DocumentService,
    private val zaakDocumentService: ZaakDocumentService,
    private val dateTimeService: DateTimeService,
    val besluitTypeOmschrijving: String
) {

    fun getHersteltermijnVanLaatsteInformatieverzoek(businessKey: String): Boolean {
        val document = getDocumentById(businessKey)

        val informatieverzoekenArrayNode = getInformatieverzoekenArrayNode(document)

        return if (!informatieverzoekenArrayNode.isMissingNode) {
            informatieverzoekenArrayNode
                .get(informatieverzoekenArrayNode.size() - 1)
                .get("hersteltermijn")
                .asBoolean()
        } else {
            false
        }
    }

    fun getDocumentenVanLaatstePortalReactie(businessKey: String): List<Any> {
        val document = getDocumentById(businessKey)
        val reactiesArrayNode = document.content().asJson().requiredAt("/beoordelingEnAfhandeling/informatieReacties")

        if (!reactiesArrayNode.isEmpty) {
            val zaakResources = zaakDocumentService.getInformatieObjectenAsRelatedFiles(UUID.fromString(businessKey))
            val reactieDocumentUrls = reactiesArrayNode
                .lastOrNull()
                ?.mapNotNull { reactie -> reactie.get("ontvangenBestanden") }
                ?.flatten()
                ?.mapNotNull { it.textValue() }
                ?.map { UUID.fromString(it.substringAfterLast("/")) }
                ?: emptyList()

            return objectMapper.convertValue(
                zaakResources.filter { relatedFile ->
                    reactieDocumentUrls.contains(relatedFile.fileId)
                }
            )
        }

        return emptyList()
    }

    fun setStopHersteltermijnForLatestVerzoekInDocument(businessKey: String): String {
        val timestamp = dateTimeService.getCurrentTimeStamp()

        getDocumentById(businessKey).apply {
            runWithoutAuthorization {
                documentService.modifyDocument(
                    this,
                    this.content().asJson()
                        .let {
                            val latestVerzoek =
                                it.at("/beoordelingEnAfhandeling/informatieverzoeken").last() as ObjectNode

                            latestVerzoek.put("stopHersteltermijn", timestamp)
                            it
                        }
                )
            }
        }

        return timestamp
    }

    fun getPropertyOfLatestVerzoekFromDocument(businessKey: String, property: String): Any {
        return getDocumentById(businessKey).content().asJson().let {
            val latestVerzoek =
                it.at("/beoordelingEnAfhandeling/informatieverzoeken").last()

            latestVerzoek.get(property)?.toValue()
                ?: throw NoSuchElementException("Property '${property}' was not found in the latest verzoek")
        }
    }

    //Can be used in camunda (JUEL) expressions.
    fun getPropertyOfLatestVerzoekOrDefault(businessKey: String, property: String, default: Any): Any {
        return getDocumentById(businessKey).content().asJson().let {
            val latestVerzoek =
                it.get("beoordelingEnAfhandeling").get("informatieverzoeken")?.last()
                    ?: jacksonObjectMapper().createObjectNode()

            latestVerzoek.get(property)?.toValue() ?: default
        }
    }

    val toValue: JsonNode.() -> Any? = {
        when {
            this.isValueNode || this.isContainerNode -> jacksonObjectMapper().treeToValue(this)
            else -> null
        }
    }

    fun setStartHersteltermijnInDocument(businessKey: String) {
        val document = getDocumentById(businessKey)

        val informatieverzoekenArrayNode = getInformatieverzoekenArrayNode(document)

        runWithoutAuthorization {
            documentService.modifyDocument(
                document,
                getRootNodeInformatieverzoeken(informatieverzoekenArrayNode)
            )
        }
    }

    fun setInformatieReactiesToDocument(reactieMap: Map<String, Any>, businessKey: String) {

        val informatieReacties = getDocumentById(businessKey)
            .content()
            .asJson()
            .get("beoordelingEnAfhandeling")
            .get("informatieReacties") as ArrayNode?
            ?: objectMapper.createArrayNode()

        informatieReacties.add(objectMapper.convertValue<ObjectNode>(reactieMap))

        val rootNode = objectMapper.createObjectNode()
        val beoordelingEnAfhandeling = objectMapper.createObjectNode()
        beoordelingEnAfhandeling.set<ArrayNode>("informatieReacties", informatieReacties)
        rootNode.set<ObjectNode>("beoordelingEnAfhandeling", beoordelingEnAfhandeling)

        runWithoutAuthorization {
            documentService.modifyDocument(getDocumentById(businessKey), rootNode)
        }
    }

    private fun getInformatieverzoekenArrayNode(document: Document) = document
        .content()
        .asJson()
        .get("beoordelingEnAfhandeling")
        .get("informatieverzoeken") as ArrayNode

    private fun getRootNodeInformatieverzoeken(informatieverzoekenArrayNode: ArrayNode): ObjectNode {

        val rootNode = jacksonObjectMapper().createObjectNode()
        val beoordelingEnAfhandeling = jacksonObjectMapper().createObjectNode()
        val laatsteIndexInformatieverzoekenArray = informatieverzoekenArrayNode.size() - 1

        if (!informatieverzoekenArrayNode.isMissingNode) {
            val laatsteInformatieverzoek = informatieverzoekenArrayNode
                .get(laatsteIndexInformatieverzoekenArray) as ObjectNode

            laatsteInformatieverzoek.put("startHersteltermijn", dateTimeService.getCurrentTimeStamp())
            informatieverzoekenArrayNode[laatsteIndexInformatieverzoekenArray] = laatsteInformatieverzoek
            beoordelingEnAfhandeling.set<ArrayNode>("informatieverzoeken", informatieverzoekenArrayNode)
            rootNode.set<ObjectNode>("beoordelingEnAfhandeling", beoordelingEnAfhandeling)
        }

        return rootNode
    }

    private fun getDocumentById(businessKey: String): Document {
        return runWithoutAuthorization { documentService.get(businessKey) }
    }

    companion object {
        private val objectMapper = jacksonObjectMapper().findAndRegisterModules()
    }
}