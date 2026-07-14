package com.ritense.valtimo.common.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.ritense.authorization.AuthorizationContext.Companion.runWithoutAuthorization
import com.ritense.catalogiapi.CatalogiApiPlugin
import com.ritense.document.domain.Document
import com.ritense.document.domain.impl.JsonSchemaDocumentId
import com.ritense.document.service.DocumentService
import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.contract.json.MapperSingleton
import com.ritense.zakenapi.ZakenApiPlugin
import com.ritense.zakenapi.domain.ZaakResponse
import com.ritense.zakenapi.domain.ZaakResultaat
import com.ritense.zakenapi.domain.ZaakStatus
import com.ritense.zakenapi.service.ZaakDocumentService
import mu.KotlinLogging
import java.net.URI
import java.util.UUID

class ZaakPropertiesService(
    private val documentService: DocumentService,
    private val zaakDocumentService: ZaakDocumentService,
    private val pluginService: PluginService
) {

    fun storeSelectedZaakPropertiesInDocumentRoot(businessKey: String, vararg selectedProperties: String) {
        val document = getDocumentById(businessKey)
        val zaak = zaakDocumentService.getZaakByDocumentIdOrThrow(UUID.fromString(businessKey))
        val rootJsonNode = objectMapper.createObjectNode()

        val zakenApiPlugin = getZakenApiPlugin(url = zaak.url)
        val catalogiApiPlugin = getCatalogiApiPlugin(zaaktype = zaak.zaaktype)

        val zaakstatus = zakenApiPlugin?.getZaakStatus(zaakUrl = zaak.url)
        val zaakresultaat = zakenApiPlugin?.getZaakResultaat(zaakUrl = zaak.url)

        val statusOmschrijving = getStatusOmschrijving(zaakstatus, catalogiApiPlugin)
        val resultaatOmschrijving = getResultaatOmschrijving(zaakresultaat, catalogiApiPlugin)

        val filteredZaakNode = filterZaakNode(zaak, selectedProperties, statusOmschrijving, resultaatOmschrijving)

        runWithoutAuthorization {
            documentService.modifyDocument(
                document,
                rootJsonNode.set(ZAAK_NODE_NAME, filteredZaakNode)
            )
        }
    }

    private fun getDocumentById(documentId: String): Document {
        return runWithoutAuthorization {
            documentService.findBy(
                JsonSchemaDocumentId.existingId(UUID.fromString(documentId))
            ).orElseThrow()
        }
    }

    private fun getZakenApiPlugin(url: URI): ZakenApiPlugin? {
        return pluginService.createInstance(
            ZakenApiPlugin::class.java,
            ZakenApiPlugin.findConfigurationByUrl(url)
        )
    }

    private fun getCatalogiApiPlugin(zaaktype: URI): CatalogiApiPlugin? {
        return pluginService.createInstance(
            CatalogiApiPlugin::class.java,
            CatalogiApiPlugin.findConfigurationByUrl(zaaktype)
        )
    }

    private fun getStatusOmschrijving(zaakstatus: ZaakStatus?, catalogiApiPlugin: CatalogiApiPlugin?): String? {
        return try {
            zaakstatus?.let { catalogiApiPlugin?.getStatustype(it.statustype)?.omschrijving }
        } catch (e: Exception) {
            logger.error("Failed to get status description", e)
            null
        }
    }

    private fun getResultaatOmschrijving(
        zaakresultaat: ZaakResultaat?,
        catalogiApiPlugin: CatalogiApiPlugin?
    ): String? {
        return try {
            zaakresultaat?.let { catalogiApiPlugin?.getResultaattype(it.resultaattype)?.omschrijving }
        } catch (e: Exception) {
            logger.error("Failed to get resultaat description", e)
            null
        }
    }

    private fun filterZaakNode(
        zaak: ZaakResponse,
        selectedProperties: Array<out String>,
        statusOmschrijving: String?,
        resultaatOmschrijving: String?
    ): ObjectNode {
        val zaakMap = objectMapper.convertValue(zaak, HashMap::class.java)
        val filteredZaakNode = objectMapper.valueToTree<ObjectNode>(
            zaakMap.filter { selectedProperties.contains(it.key) && it.value != null }
        )

        statusOmschrijving?.let { filteredZaakNode.put(STATUS_OMSCHRIJVING, it) }
        resultaatOmschrijving?.let { filteredZaakNode.put(RESULTAAT_OMSCHRIJVING, it) }

        return filteredZaakNode
    }

    companion object {
        const val ZAAK_NODE_NAME = "openzaak"
        const val STATUS_OMSCHRIJVING = "statusOmschrijving"
        const val RESULTAAT_OMSCHRIJVING = "resultaatOmschrijving"
        private val objectMapper: ObjectMapper = MapperSingleton.get()
        private val logger = KotlinLogging.logger {}
    }
}