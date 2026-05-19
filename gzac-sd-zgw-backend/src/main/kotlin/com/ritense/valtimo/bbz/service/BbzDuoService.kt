package com.ritense.valtimo.bbz.service

import com.ritense.valtimo.common.service.DocumentWriterService
import mu.KotlinLogging

class BbzDuoService(
    private val documentWriterService: DocumentWriterService
) {
    fun storeDuoPersonalInformation(duoPersoonsInformatie: Map<String, Any>?, targetPath: String, businessKey: String) {
        try {
            duoPersoonsInformatie
                ?.let {
                    documentWriterService.writeValueToDocumentAtPath(it, targetPath, businessKey)
                } ?: logger.debug { "No DUO PersoonsInformatie found for document with ID $businessKey" }
        } catch (e: Exception) {
            logger.error("Exiting scope due to nested error.", e)
            return
        }

    }

    fun storeDuoStudiefinancieringInfo(
        duoStudiefinancieringInfo: Map<String, Any>?,
        targetPath: String,
        businessKey: String
    ) {
        try {
            duoStudiefinancieringInfo
                ?.let {
                    documentWriterService.writeValueToDocumentAtPath(it, targetPath, businessKey)
                } ?: logger.debug { "No DUO StudiefinancieringInfo found for document with ID $businessKey" }
        } catch (e: Exception) {
            logger.error("Exiting scope due to nested error.", e)
            return
        }

    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}