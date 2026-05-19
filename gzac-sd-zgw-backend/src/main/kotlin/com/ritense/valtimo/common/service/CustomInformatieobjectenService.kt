package com.ritense.valtimo.common.service

import com.ritense.documentenapi.service.DocumentenApiService
import com.ritense.resource.service.TemporaryResourceStorageService
import com.ritense.zakenapi.service.ZaakDocumentService
import java.util.UUID
import javassist.NotFoundException
import mu.KotlinLogging
import org.springframework.web.client.HttpClientErrorException

class CustomInformatieobjectenService(
    private val temporaryResourceStorageService: TemporaryResourceStorageService,
    private val documentenApiService: DocumentenApiService,
    private val zaakDocumentService: ZaakDocumentService
) {
    fun getDocumentMetadataValue(tempResourceId: String, metadataKey: String): String {
        return temporaryResourceStorageService.getMetadataValue(tempResourceId, metadataKey)
    }

    fun getDocumentUrlById(documentId: String, businessKey: String): String {
        val relatedFiles = zaakDocumentService.getInformatieObjectenAsRelatedFiles(UUID.fromString(businessKey))

        if (relatedFiles.isNotEmpty()) {
            try {
                logger.debug { "Retrieving document url for document with id $documentId" }

                val pluginConfigurationId = relatedFiles.first().pluginConfigurationId.toString()
                return documentenApiService.getInformatieObject(
                    pluginConfigurationId,
                    UUID.fromString(businessKey),
                    documentId
                ).url.toString()
            } catch (e: HttpClientErrorException) {
                throw NotFoundException("No document url found for document with id: $documentId")
            }
        } else
            throw NoSuchElementException("No related file(s) found for document $businessKey")
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}