package com.ritense.valtimo.common.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ritense.authorization.AuthorizationContext.Companion.runWithoutAuthorization
import com.ritense.document.domain.impl.request.NewDocumentRequest
import com.ritense.document.exception.DocumentNotFoundException
import com.ritense.document.service.DocumentService
import com.ritense.processdocument.service.CorrelationService

class DocumentProcessService(
    private val correlationService: CorrelationService,
    private val documentService: DocumentService
) {

    fun createDocumentAndStartProcess(
        documentDefinitionName: String,
        messageName: String,
        documentContent: Map<String, Any>,
        variables: Map<String, Any>
    ) {

        val newDocument = runWithoutAuthorization {
            documentService.createDocument(
                NewDocumentRequest(
                    documentDefinitionName,
                    jacksonObjectMapper().valueToTree(documentContent)
                )
            )
        }
        val newDocumentId = newDocument.resultingDocument()?.get()?.id()?.id
            ?: throw DocumentNotFoundException("Document could not be created.")

        correlationService.sendStartMessage(
            message = messageName,
            businessKey = newDocumentId.toString(),
            variables = variables
        )
    }
}