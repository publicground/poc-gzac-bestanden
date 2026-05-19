package com.ritense.valtimo.common.service

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.ritense.authorization.AuthorizationContext.Companion.runWithoutAuthorization
import com.ritense.document.domain.Document
import com.ritense.document.domain.impl.request.ModifyDocumentRequest
import com.ritense.document.service.DocumentService
import com.ritense.valtimo.contract.json.patch.JsonPatch
import com.ritense.valtimo.contract.json.patch.operation.RemoveOperation

class DocumentWriterService(
    private val documentService: DocumentService
) {

    fun writeValueToDocumentAtPath(targetValue: Any, targetPath: String, documentId: String) {
        val document = getDocumentById(documentId)
        val valueNode = jacksonObjectMapper().valueToTree<JsonNode>(targetValue)
        val jsonPatch = buildJsonPatchWithObjectNodeAtPath(valueNode, targetPath)

        runWithoutAuthorization { documentService.modifyDocument(document, jsonPatch) }
    }

//    @RunWithoutAuthorization
    fun removeValueAtPath(targetPath: String, businessKey: String) {
        var content = getDocumentContentById(businessKey)
        val sanitizedPath = removeRootSlashFromPathString(targetPath)
        val pathKeyNames = sanitizedPath.split("/")
        val pathIterator = pathKeyNames.iterator()
        while (pathIterator.hasNext()) {
            val name = pathIterator.next()
            content.get(name)?.let {
                content = it
            } ?: return
        }

        val request = ModifyDocumentRequest.create(
            getDocumentById(businessKey),
            mapper.createObjectNode()
        ).withJsonPatch(JsonPatch())

        request.jsonPatch().add(
            RemoveOperation(JsonPointer.compile(targetPath))
        )

        documentService.modifyDocument(request)
    }

    private fun buildJsonPatchWithObjectNodeAtPath(jsonObject: JsonNode, targetPath: String): JsonNode {
        val sanitizedPath = removeRootSlashFromPathString(targetPath)
        val pathKeyNames = sanitizedPath.split("/")
        val pathIterator = pathKeyNames.iterator()
        val rootNode = jacksonObjectMapper().createObjectNode()

        var secondToLastNode = jacksonObjectMapper().createObjectNode()
        var currentNode = rootNode
        while (pathIterator.hasNext()) {
            secondToLastNode = currentNode
            currentNode = currentNode.putObject(pathIterator.next())
        }

        secondToLastNode.replace(pathKeyNames.last(), jsonObject)

        return rootNode
    }

    private fun removeRootSlashFromPathString(targetPath: String): String {
        return if (!targetPath.startsWith("/")) targetPath else targetPath.substring(1)
    }

    private fun getDocumentById(businessKey: String): Document {
        return runWithoutAuthorization { documentService.get(businessKey) }
    }

    private fun getDocumentContentById(businessKey: String): JsonNode {
        return getDocumentById(businessKey).content().asJson()
    }

    companion object {
        private val mapper = jsonMapper {
            addModule(kotlinModule())
        }
    }
}