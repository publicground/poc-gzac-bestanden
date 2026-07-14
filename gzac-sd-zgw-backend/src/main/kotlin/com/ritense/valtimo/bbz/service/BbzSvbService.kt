package com.ritense.valtimo.bbz.service

import com.ritense.valtimo.common.service.DocumentWriterService
import mu.KotlinLogging

@Suppress("UNUSED")
class BbzSvbService(
    private val documentWriterService: DocumentWriterService
) {


    fun storeUitkeringen(svbUitkeringen: Map<String, Any>?, targetPath: String, businessKey: String) {

        /**
         *  write bron data from pv svbUitkeringen to document
         */
        try {
            svbUitkeringen
                ?.let {
                    documentWriterService.writeValueToDocumentAtPath(it, targetPath, businessKey)
                } ?: logger.debug { "No SVB uitkeringen found for document with ID $businessKey" }

        } catch (e: Exception) {
            logger.error("Exiting scope due to nested error.", e)
            return
        }
    }

    fun storePensioenen(svbPensioenen: Map<String, Any>?, targetPath: String, businessKey: String) {
        svbPensioenen
            ?.let {
                documentWriterService.writeValueToDocumentAtPath(it, targetPath, businessKey)
            } ?: logger.debug { "No pensioenen found for document with ID $businessKey" }
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}