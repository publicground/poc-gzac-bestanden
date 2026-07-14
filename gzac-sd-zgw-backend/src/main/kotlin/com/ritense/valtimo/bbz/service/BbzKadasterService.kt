package com.ritense.valtimo.bbz.service

import com.ritense.valtimo.common.service.DocumentWriterService
import mu.KotlinLogging

class BbzKadasterService(
    private val documentWriterService: DocumentWriterService
) {
    fun storeKadastraleObjecten(kadastraleObjecten: Map<String, Any>?, targetPath: String, businessKey: String) {
        try {
            kadastraleObjecten
                ?.let {
                    documentWriterService.writeValueToDocumentAtPath(it, targetPath, businessKey)
                } ?: logger.debug { "No kadastrale objecten found for document with ID $businessKey" }
        } catch (e: Exception) {
            logger.error("Exiting scope due to nested error.", e)
            return
        }
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}