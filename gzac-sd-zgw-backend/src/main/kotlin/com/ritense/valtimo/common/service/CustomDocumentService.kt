package com.ritense.valtimo.common.service

import com.ritense.authorization.AuthorizationContext.Companion.runWithoutAuthorization
import com.ritense.document.service.DocumentService
import mu.KotlinLogging

class CustomDocumentService(
    private val documentService: DocumentService,
) {

    fun deleteDocument(businessKey: String) {
        val document = documentService.get(businessKey)
        runWithoutAuthorization {
            documentService.deleteDocument(document.id())
        }

        logger.info("Deleted document with id {}", document.id())
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}