package com.ritense.valtimo.common.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.treeToValue
import com.ritense.authorization.AuthorizationContext.Companion.runWithoutAuthorization
import com.ritense.document.domain.Document
import com.ritense.document.service.DocumentService

class DocumentReaderService(
    private val documentService: DocumentService
) {

    //Do not use in camunda (JUEL) expressions. Camunda doesn't know how to handle null.
    fun getValueFromDocumentAtPath(targetPath: String, documentId: String): Any? {
        return getDocumentById(documentId)
            .content().asJson()
            .at(targetPath)
            .toValue()
    }

    //Can be used in camunda (JUEL) expressions.
    fun getValueFromDocumentAtPathOrDefault(targetPath: String, documentId: String, default: Any): Any {
        return getDocumentById(documentId)
            .content().asJson()
            .at(targetPath)
            .toValue() ?: default
    }

    val toValue: JsonNode.() -> Any? = {
        when {
            this.isValueNode || this.isContainerNode -> jacksonObjectMapper().treeToValue(this)
            else -> null
        }
    }

    fun getDocumentContentById(businessKey: String): JsonNode {
        return runWithoutAuthorization {getDocumentById(businessKey).content().asJson()}
    }

    fun getDocumentById(businessKey: String): Document {
        return runWithoutAuthorization { documentService.get(businessKey) }
    }
}