package com.ritense.valtimo.common.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ritense.documentenapi.DocumentenApiPlugin
import com.ritense.documentenapi.web.rest.dto.RelatedFileDto
import com.ritense.plugin.domain.PluginConfigurationId
import com.ritense.plugin.service.PluginService
import com.ritense.zakenapi.service.ZaakDocumentService
import org.camunda.bpm.engine.delegate.DelegateExecution
import java.util.UUID


class CustomZaakInformatieobjectenService(
    private val zaakDocumentService: ZaakDocumentService,
    private val pluginService: PluginService
) {
    fun getZaakInformatieobjecten(execution: DelegateExecution): JsonNode {
        val relatedFiles = zaakDocumentService.getInformatieObjectenAsRelatedFiles(
            UUID.fromString(execution.businessKey)
        )

        val zaakInformatieobjecten = jacksonObjectMapper().createArrayNode()

        if (relatedFiles.isNotEmpty()) {
            relatedFiles.map { relatedFile ->
                val fileName = relatedFile.fileName
                val createdOn = relatedFile.createdOn.toString()
                val documentUrl = getDocumentUrl(relatedFile)

                val informatieobject = jacksonObjectMapper().createObjectNode()
                informatieobject.put("fileName", fileName)
                informatieobject.put("createdOn", createdOn)
                informatieobject.put("documentUrl", documentUrl)
                zaakInformatieobjecten.add(informatieobject)
            }
        }
        return zaakInformatieobjecten
    }

    // Necessary because the above can't be used with the 'link-document-to-zaak' plugin action
    fun getInformatieobjectUrlsFromZaakByBusinessKey(businessKey: String): List<String> =
        zaakDocumentService
            .getInformatieObjectenAsRelatedFiles(
                UUID.fromString(businessKey)
            )
            .map { getDocumentUrl(it) }

    private fun getDocumentUrl(relatedFile: RelatedFileDto): String {
        val resourceId = relatedFile.fileId.toString()
        val pluginConfigurationId = relatedFile.pluginConfigurationId

        val documentApiPlugin = checkNotNull(
            pluginService.createInstance(
                PluginConfigurationId.existingId(pluginConfigurationId)
            ) as DocumentenApiPlugin
        ) { "Could not find DocumentenApiPlugin with pluginConfigurationId: $pluginConfigurationId" }

        return documentApiPlugin.url.toString().plus("enkelvoudiginformatieobjecten/$resourceId")
    }
}