package com.ritense.valtimo.common.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ritense.authorization.AuthorizationContext.Companion.runWithoutAuthorization
import com.ritense.document.domain.Document
import com.ritense.document.domain.impl.JsonSchemaDocumentId
import com.ritense.document.domain.impl.request.NewDocumentRequest
import com.ritense.document.exception.DocumentNotFoundException
import com.ritense.document.service.DocumentService
import com.ritense.processdocument.service.ProcessDocumentAssociationService
import org.camunda.bpm.engine.RuntimeService
import java.util.UUID

class MessageCorrelationService(
    private val runtimeService: RuntimeService,
    private val documentService: DocumentService,
    private val associationService: ProcessDocumentAssociationService
) {
    fun sendCorrelationMessage(message: String) {
        runtimeService.correlateMessage(message)
    }

    fun sendCorrelationMessageByBusinessKey(message: String, businessKey: String) {
        runtimeService.createMessageCorrelation(message)
            .processInstanceBusinessKey(businessKey)
            .correlate()
    }

    fun sendCorrelationMessageByBusinessKeyWithVariable(
        message: String,
        businessKey: String,
        variableName: String,
        variableValue: Any
    ) {
        runtimeService.createMessageCorrelation(message)
            .processInstanceBusinessKey(businessKey)
            .setVariable(variableName, variableValue)
            .correlate()
    }

    fun sendCorrelationMessageWithVariablesToAll(
        message: String,
        businessKey: String,
        variables: Map<String, Any>
    ) {
        runtimeService.createMessageCorrelation(message)
            .processInstanceBusinessKey(businessKey)
            .setVariables(variables)
            .correlateAll()
    }

    fun sendCorrelationMessageByBusinessKeyWithSourceBusinessKey(
        messageName: String,
        targetBusinessKey: String,
        sourceBusinessKey: String
    ) {
        runtimeService.createMessageCorrelation(messageName)
            .processInstanceBusinessKey(targetBusinessKey)
            .setVariable("messageOriginBusinessKey", sourceBusinessKey)
            .correlate()
    }

    fun sendCorrelationStartMessageByBusinessKey(message: String, process: String, businessKey: String) {
        runWithoutAuthorization { documentService.findBy(JsonSchemaDocumentId.existingId(UUID.fromString(businessKey))) }
            .ifPresentOrElse({ document: Document? ->
                val processInstance = runtimeService.createMessageCorrelation(message)
                    .processInstanceBusinessKey(businessKey)
                    .correlateStartMessage()
                if (document != null) {
                    runWithoutAuthorization {
                        associationService.createProcessDocumentInstance(
                            processInstance.id,
                            UUID.fromString(document.id().toString()),
                            process
                        )
                    }
                }
            }, { throw RuntimeException("Document not found!") }
            )
    }

    fun sendCorrelationStartMessageByBusinessKeyWithVariables(
        businessKey: String,
        message: String,
        processName: String,
        variables: Map<String, Any>
    ) {
        val processInstance = runtimeService.startProcessInstanceByMessage(message, businessKey, variables)

        runWithoutAuthorization {
            associationService.createProcessDocumentInstance(
                processInstance.id,
                UUID.fromString(businessKey),
                processName
            )
        }
    }

    fun createDocumentAndStartProcessByMessage(
        documentDefinitionName: String,
        messageName: String,
        processName: String,
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

        sendCorrelationStartMessageByBusinessKeyWithVariables(
            businessKey = newDocumentId.toString(),
            message = messageName,
            processName = processName,
            variables = variables
        )
    }
}
