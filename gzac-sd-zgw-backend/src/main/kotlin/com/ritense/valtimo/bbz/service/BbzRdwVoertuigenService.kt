package com.ritense.valtimo.bbz.service

import com.ritense.valtimo.common.service.DocumentWriterService
import mu.KotlinLogging

class BbzRdwVoertuigenService(
    private val documentWriterService: DocumentWriterService
) {

    /**
     * Store RDW voertuigen from Suwinet
     */
    fun storeVoertuigen(rdwVoertuigen: Map<String, Any>?, targetPath: String, businessKey: String) {
        /**
         *  Get RDW bron data from pv voertuigen
         */
        try {
            rdwVoertuigen
                ?.let {
                    documentWriterService.writeValueToDocumentAtPath(it, targetPath, businessKey)
                } ?: logger.debug { "No rdw voertuigen found for document with ID $businessKey" }
        } catch ( e: Exception) {
            logger.error("Exiting scope due to nested error.", e)
            return
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {  }
    }
}