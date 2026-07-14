package com.ritense.valtimo.financieletrainers.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.ValueNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ritense.authorization.AuthorizationContext.Companion.runWithoutAuthorization
import com.ritense.document.domain.Document
import com.ritense.document.service.DocumentService
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import mu.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import java.util.UUID

class DeelnameCaseService(
    private val documentService: DocumentService
) {

    fun getAmountOfSessiesInDocumentById(businessKey: String): Int {
        val document = getDocumentById(businessKey)
        val sessiesNode = getTrainingssessiesFromDocument(document)

        return sessiesNode.size()
    }

    fun getAmountOfAttendedSessiesInDocumentById(businessKey: String): Int {
        val document = getDocumentById(businessKey)
        val sessiesNode = getTrainingssessiesFromDocument(document)
        val numOfAttendedSessies = sessiesNode.filter { session ->
            session.get(AANWEZIG_KEY_NAME).booleanValue()
        }.size

        return numOfAttendedSessies
    }

    fun getAmountOfMissedSessiesInDocumentById(businessKey: String): Int {
        val document = getDocumentById(businessKey)
        val sessiesNode = getTrainingssessiesFromDocument(document)
        val missedSessies: List<JsonNode> = sessiesNode.filter {
            !it.get("aanwezig").booleanValue()
        }

        return missedSessies.size
    }

    //TODO: Make generic and move to DocumentWriterService
    fun writeProcessVariablesToArrayAtPath(execution: DelegateExecution, vararg keys: String) {
        val document = getDocumentById(execution.businessKey)
        val rootNode = document.content().asJson() as ObjectNode
        val trainingsDeelnameNode = rootNode.get("trainingsdeelname") ?: rootNode.putObject("trainingsdeelname")
        val sessiesNode = trainingsDeelnameNode.get("trainingssessies")
            ?.takeIf { !it.isMissingNode }
            ?.let { it as ArrayNode }
            ?: (trainingsDeelnameNode as ObjectNode).putArray("trainingssessies")

        val newSessie = jacksonObjectMapper().createObjectNode()

        buildTargetNodeWithSelectedValues(execution, keys, newSessie)

        sessiesNode.add(newSessie)
        runWithoutAuthorization { documentService.modifyDocument(document, rootNode) }
    }

    fun setTrainingsresultaatToDocument(execution: DelegateExecution, vararg keys: String) {
        val trainingsdeelnameDocument = getDocumentById(execution.businessKey)

        val rootNode = jacksonObjectMapper().createObjectNode()
        val trainingsresultaatNode = rootNode
            .putObject("trainingsdeelname")
            .putObject("trainingsresultaat")

        buildTargetNodeWithSelectedValues(execution, keys, trainingsresultaatNode)

        runWithoutAuthorization { documentService.modifyDocument(trainingsdeelnameDocument, rootNode) }
    }

    fun setTrainingStartdatumToProcessVariable(execution: DelegateExecution) {
        val trainingsdeelnameDocument = getDocumentById(execution.businessKey)

        val startdatumNode = trainingsdeelnameDocument
            .content()
            .asJson()
            .get("training")
            .get("startdatum")
            .asText()

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val startdatum = formatter.format(ZonedDateTime.parse(startdatumNode))

        execution.setVariable("startdatum", startdatum)
    }

    fun getTrainingObjectUrlFromBeheerDocument(trainingsbeheerDocumentId: String): String {
        val beheerDocument = getDocumentById(trainingsbeheerDocumentId)
        return beheerDocument.content().asJson().get("trainingObjectUrl").asText()
    }

    fun assignCaseHandler(businessKey: String, assigneeId: String) {
        val documentId = UUID.fromString(businessKey)
        documentService.assignUserToDocument(documentId, assigneeId)
    }

    private fun buildTargetNodeWithSelectedValues(
        execution: DelegateExecution,
        keys: Array<out String>,
        targetNode: ObjectNode
    ) {
        execution.variables.forEach {
            it.takeIf { keys.contains(it.key) }?.apply {
                val valueNode = jacksonObjectMapper().valueToTree<ValueNode>(it.value)
                targetNode.replace(it.key, valueNode)
            } ?: logger.debug("Skipping ${it.key}")
        }
    }

    private fun getTrainingssessiesFromDocument(document: Document): ArrayNode {
        return document.content().asJson()
            .at("/trainingsdeelname/trainingssessies")
            ?.let { it as ArrayNode }
            ?: jacksonObjectMapper().createArrayNode()
    }

    private fun getDocumentById(businessKey: String): Document {
        return runWithoutAuthorization { documentService.get(businessKey) }
    }

    companion object {
        private val logger = KotlinLogging.logger { }
        private val AANWEZIG_KEY_NAME = "aanwezig"
    }
}