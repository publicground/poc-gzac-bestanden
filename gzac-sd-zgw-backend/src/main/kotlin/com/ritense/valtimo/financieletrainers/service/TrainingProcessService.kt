package com.ritense.valtimo.financieletrainers.service

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.treeToValue
import com.ritense.authorization.AuthorizationContext.Companion.runWithoutAuthorization
import com.ritense.document.domain.Document
import com.ritense.document.domain.impl.request.ModifyDocumentRequest
import com.ritense.document.service.DocumentService
import com.ritense.valtimo.contract.json.patch.JsonPatch
import com.ritense.valtimo.contract.json.patch.operation.ReplaceOperation
import mu.KotlinLogging
import org.springframework.transaction.annotation.Transactional

open class TrainingProcessService(
    private val documentService: DocumentService
) {

    open fun storeDeelnameByDocumentIdInTraining(deelnameBusinessKey: String, trainingBusinessKey: String) {
        val jsonPatchRootNode = objectMapper.createObjectNode()
        val deelnameDocument = getDocumentById(deelnameBusinessKey)
        val trainingDocument = getDocumentById(trainingBusinessKey)
        val newDeelnameEntry = createTrainingsdeelnameFromDeelnameDocument(deelnameDocument)
        val trainingsdeelnameZakenNode = getTrainingsdeelnameZakenFromDocument(trainingDocument)

        trainingsdeelnameZakenNode.add(newDeelnameEntry)

        jsonPatchRootNode.replace(TRAININGSDEELNAME_ZAKEN_NODE_NAME, trainingsdeelnameZakenNode)
        runWithoutAuthorization { documentService.modifyDocument(trainingDocument, jsonPatchRootNode) }
    }

    @Transactional
    open fun updateDeelnameByDocumentIdInTraining(deelnameBusinessKey: String, trainingBusinessKey: String) {
        val deelnameDocument = getDocumentById(deelnameBusinessKey)
        val trainingDocument = getDocumentById(trainingBusinessKey)
        val rootNode = objectMapper.createObjectNode()
        val updatedDeelnameEntry = createTrainingsdeelnameFromDeelnameDocument(deelnameDocument)
        val trainingsdeelnameZakenNode = getTrainingsdeelnameZakenFromDocument(trainingDocument)
        val deelnameNode = trainingsdeelnameZakenNode.firstOrNull {
            it.get(TRAININGSDEELNAME_ZAAK_ID_NODE_NAME)?.textValue().equals(deelnameBusinessKey)
        }

        trainingsdeelnameZakenNode.set(trainingsdeelnameZakenNode.indexOf(deelnameNode), updatedDeelnameEntry)
        rootNode.replace(TRAININGSDEELNAME_ZAKEN_NODE_NAME, trainingsdeelnameZakenNode)

        runWithoutAuthorization { documentService.modifyDocument(trainingDocument, rootNode) }
    }

    open fun removeDeelnameByDocumentIdFromTraining(
        deelnameBusinessKey: String,
        trainingBusinessKey: String
    ) {
        val document = getDocumentById(trainingBusinessKey)

        logger.info(
            "Retrieving deelname with document id {} from training document.",
            deelnameBusinessKey
        )

        val deelnameZakenNode = getTrainingsdeelnameZakenFromDocument(document)

        val indexToRemove = (0 until deelnameZakenNode.size()).firstOrNull { i ->
            val id = deelnameZakenNode.get(i)
                ?.get(TRAININGSDEELNAME_ZAAK_ID_NODE_NAME)
                ?.asText()
                ?.trim()

            id == deelnameBusinessKey.trim()
        }

        if (indexToRemove == null) {
            logger.warn(
                "No deelname found with document id {} to remove from training with document id {}",
                deelnameBusinessKey,
                trainingBusinessKey
            )
            return
        }

        logger.info(
            "Removing deelname with document id {} at index {} from training with document id {}",
            deelnameBusinessKey,
            indexToRemove,
            trainingBusinessKey
        )

        deelnameZakenNode.remove(indexToRemove)

        val rootNode = objectMapper.createObjectNode().apply {
            set<JsonNode>(TRAININGSDEELNAME_ZAKEN_NODE_NAME, deelnameZakenNode)
        }

        runWithoutAuthorization {
            documentService.modifyDocument(document, rootNode)
        }

        logger.info(
            "Successfully removed deelname with document id {} from training with document id {}",
            deelnameBusinessKey,
            trainingBusinessKey
        )
    }

    open fun getAmountOfDeelnamesInDocumentById(businessKey: String): Number {
        val document = getDocumentById(businessKey)
        val deelnamesNode = getTrainingsdeelnameZakenFromDocument(document)

        return deelnamesNode.size()
    }

    //temporary solution until valtimo 9.17
    //TODO: implement List<VariableMap> solution instead of List<Any> to be able to directly use the properties of an inschrijving in the expressions
    open fun getDeelnamesFromTraining(businessKey: String): List<Any> {
        val document = getDocumentById(businessKey)
        val collection: MutableList<Any> = ArrayList()

        getTrainingsdeelnameZakenFromDocument(document)
            .elements()
            .forEachRemaining(collection::add)

        return collection
    }

    //TODO: refactor alongside getDeelnamesFromTraining
    open fun getActiveDeelnamesFromTraining(businessKey: String): List<Any> {
        val document = getDocumentById(businessKey)
        val collection: MutableList<Any> = ArrayList()

        getTrainingsdeelnameZakenFromDocument(document)
            .filter { deelname ->
                deelname.get("status")?.textValue() != "Afwezig"
            }
            .apply {
                collection.addAll(this)
            }

        return collection
    }

    open fun getFullNamesOfDeelnemersFromTraining(businessKey: String): List<String> {
        val deelnames: JsonNode = objectMapper.convertValue(getDeelnamesFromTraining(businessKey))

        return when (deelnames) {
            is ArrayNode -> deelnames.map { it.get("volledigeNaamDeelnemer").textValue() }
            else -> emptyList()
        }
    }

    open fun removeAanwezigheidFromAllDeelnames(businessKey: String) {
        val document = getDocumentById(businessKey)
        val rootNode = objectMapper.createObjectNode()
        val deelnameZaken = getDeelnamesFromTraining(businessKey)
        val modifyDocumentRequest: ModifyDocumentRequest = ModifyDocumentRequest.create(document, rootNode)

        modifyDocumentRequest.withJsonPatch(JsonPatch())
        for (i in 0..deelnameZaken.size - 1) {
            modifyDocumentRequest.jsonPatch().add(
                ReplaceOperation(
                    JsonPointer.compile("/${TRAININGSDEELNAME_ZAKEN_NODE_NAME}/${i}/${TRAININGSDEELNAME_AANWEZIGHEID_NODE_NAME}"),
                    objectMapper.createObjectNode()
                )
            )
        }

        runWithoutAuthorization { documentService.modifyDocument(modifyDocumentRequest) }
    }

    open fun setStatusOfDeelnameById(status: String, deelnameBusinessKey: String, trainingBusinessKey: String) {
        val document = getDocumentById(trainingBusinessKey)
        val documentRootNode = document.content().asJson()
        val deelnameZaken = documentRootNode.get(TRAININGSDEELNAME_ZAKEN_NODE_NAME)

        deelnameZaken.first { deelname ->
            deelname.get(TRAININGSDEELNAME_ZAAK_ID_NODE_NAME).textValue().equals(deelnameBusinessKey)
        }
            .apply {
                this as ObjectNode
                this.put("status", status)
            }

        runWithoutAuthorization { documentService.modifyDocument(document, documentRootNode) }
    }


    open fun getTrainingsdeelnameBusinessKey(inschrijvingObjectNode: ObjectNode): String {
        return inschrijvingObjectNode
            .get(TRAININGSDEELNAME_ZAAK_ID_NODE_NAME)
            .textValue()
    }

    //dirty fix for camunda not being able to parse ObjectNodes
    //TODO: make inschrijving a variablemap instead of ObjectNode
    open fun prepareProcessVariablesForAanwezigheidOpDeelnameProcess(
        businessKey: String,
        inschrijving: ObjectNode
    ): Map<String, Any> {
        val variableMap =
            objectMapper.treeToValue<Map<String, Any>>(inschrijving.get("aanwezigheid")) as MutableMap

        return variableMap
    }

    //dirty fix for camunda not being able to parse ObjectNodes
    //TODO: make inschrijving a variablemap instead of ObjectNode
    open fun prepareProcessVariablesForResultaatOpDeelnameProcess(
        inschrijving: ObjectNode
    ): Map<String, Any> {
        val inschrijvingMap =
            objectMapper.treeToValue<Map<String, Any>>(inschrijving)
        val variableMap = HashMap<String, Any>()

        inschrijvingMap
            .filter { it.key.contains("resultaat") }
            .apply { variableMap.putAll(this) }

        return variableMap
    }

    // placeholder in various letters.
    open fun getTrainersFullNamesFromTraining(businessKey: String): List<String> {
        val document = getDocumentById(businessKey)

        return document
            .content()
            .asJson()
            .get("training")?.get("trainers")?.map { it.get("volledigeNaam").textValue() }
            ?: emptyList()
    }

    private fun getTrainingsdeelnameZakenFromDocument(document: Document): ArrayNode {
        return document.content().asJson()
            .get(TRAININGSDEELNAME_ZAKEN_NODE_NAME)
            ?.let { it as ArrayNode }
            ?: objectMapper.createArrayNode()
    }

    private fun createTrainingsdeelnameFromDeelnameDocument(document: Document): JsonNode {
        val documentNode = document.content().asJson()
            ?: objectMapper.createObjectNode()
        val deelnemerNode = documentNode?.get("deelnemer")
            ?: objectMapper.createObjectNode()
        val trainingsdeelnameNode = documentNode?.get("trainingsdeelname")
            ?: objectMapper.createObjectNode()
        val deelnameNode = objectMapper.createObjectNode()

        with(deelnameNode) {
            put(TRAININGSDEELNAME_ZAAK_ID_NODE_NAME, document.id().toString())
            replace("volledigeNaamDeelnemer", deelnemerNode.get("volledigeNaam"))
            replace("telefoonDeelnemer", deelnemerNode.get("telefoonnummer"))
            replace("emailadresDeelnemer", deelnemerNode.get("emailadres"))
            replace("status", documentNode.get("openzaak")?.get("statusOmschrijving") ?: TextNode(""))
            replace("aantalSessieBijgewoond", deelnemerNode.get("aantalSessieBijgewoond") ?: IntNode(0))
            replace("voedselallergie", trainingsdeelnameNode.get("voedselallergie") ?: TextNode(""))
            replace("opmerking", trainingsdeelnameNode.get("opmerking") ?: TextNode(""))
            replace("trainingssessies", trainingsdeelnameNode.get("trainingssessies") ?: objectMapper.createArrayNode())
        }

        return deelnameNode
    }

    private fun getDocumentById(businessKey: String): Document {
        return runWithoutAuthorization { documentService.get(businessKey) }
    }

    companion object {
        const val TRAININGSDEELNAME_ZAKEN_NODE_NAME = "trainingsdeelnameZaken"
        const val TRAININGSDEELNAME_ZAAK_ID_NODE_NAME = "trainingsdeelnameDocumentId"
        const val TRAININGSDEELNAME_AANWEZIGHEID_NODE_NAME = "aanwezigheid"
        private val objectMapper = jacksonObjectMapper()
        private val logger = KotlinLogging.logger {}
    }
}
