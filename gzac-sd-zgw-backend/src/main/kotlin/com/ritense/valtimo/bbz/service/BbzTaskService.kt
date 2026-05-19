package com.ritense.valtimo.bbz.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.ritense.authorization.AuthorizationContext.Companion.runWithoutAuthorization
import com.ritense.document.domain.Document
import com.ritense.document.service.DocumentService
import com.ritense.valtimo.bbz.domain.Informatieverzoek
import com.ritense.valtimo.common.service.CustomPortaalTaakService
import mu.KotlinLogging


class BbzTaskService(
    val documentService: DocumentService,
    val customPortaalTaakService: CustomPortaalTaakService
) {
    fun writeInformatiereactieToDocument(reactieMap: Map<String, Any>, documentId: String) {
        val document = documentService.get(documentId)
        val informatieverzoeken = getInformatieverzoeken(document) as ArrayList
        val laatsteInformatieverzoek = informatieverzoeken.last()

        laatsteInformatieverzoek.reactie = reactieMap
        informatieverzoeken[informatieverzoeken.lastIndex] = laatsteInformatieverzoek

        val rootNode = mapper.createObjectNode()
        rootNode.set<ObjectNode>(INFORMATIEVERZOEKEN, mapper.convertValue(informatieverzoeken))

        runWithoutAuthorization {
            documentService.modifyDocument(document, rootNode)
        }
    }

    private fun getInformatieverzoeken(document: Document): List<Informatieverzoek>{

        val informatieverzoekenJson = document.content().asJson().get(INFORMATIEVERZOEKEN) as ArrayNode?
            ?: mapper.createArrayNode()
        return mapper .readValue(informatieverzoekenJson.toString(), object : TypeReference<List<Informatieverzoek>>() {})
    }

    fun cancelPortalTask(taskId: String, objectManagementTitle: String) {
        customPortaalTaakService.cancelPortaalTaak(taskId, objectManagementTitle)
        logger.info { "Closing Portal task $taskId" }
    }

    fun updatePortaalTaak(taskId: String, objectManagementTitle: String, newDeadline: String) {
        customPortaalTaakService.updatePortaalTaak(taskId, objectManagementTitle, newDeadline)
        logger.info { "Updating Portal task $taskId" }
    }

    companion object {
        val logger = KotlinLogging.logger {  }
        val mapper = jacksonObjectMapper().registerKotlinModule()
        private const val INFORMATIEVERZOEKEN = "informatieverzoeken"
    }
}