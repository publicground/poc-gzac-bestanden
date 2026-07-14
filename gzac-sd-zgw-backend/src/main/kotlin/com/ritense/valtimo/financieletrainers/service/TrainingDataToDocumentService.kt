package com.ritense.valtimo.financieletrainers.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.ritense.authorization.AuthorizationContext.Companion.runWithoutAuthorization
import com.ritense.document.domain.Document
import com.ritense.document.service.DocumentService
import mu.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TrainingDataToDocumentService(
    private val documentService: DocumentService
) {

    //TODO: this function needs to move somewhere else or the service should be renamed.
    //For the placeholders in smart documents templates.
    fun setTrainingInformationToProcessVariables(execution: DelegateExecution, trainingObject: ObjectNode) {

        val trainingNode = trainingObject.get(TRAINING_KEY)
        val trainingNaam = trainingNode.get("naam").textValue()
        val trainingstype = trainingNode.get("trainingstype").textValue()
        val trainingssessies = trainingNode.get("trainingssessies")

        val eersteTrainer = trainingNode.get("trainers").get(0)
        val naamTrainer = eersteTrainer.get("volledigeNaam").asText()
        val telefoonEersteTrainer = eersteTrainer.get("telefoonnummer").asText()
        val emailEersteTrainer = eersteTrainer.get("emailadres").asText()

        execution.setVariable("trainingNaam", trainingNaam)
        execution.setVariable("trainingstype", trainingstype)
        execution.setVariable("trainingssessies", trainingssessies)
        execution.setVariable("naamTrainer", naamTrainer)
        execution.setVariable("telefoonnummerTrainer", telefoonEersteTrainer)
        execution.setVariable("emailTrainer", emailEersteTrainer)
        execution.setVariable("startdatumLaatsteSessie", getStartdatumLaatseSessie(trainingNode))
    }

    private fun getStartdatumLaatseSessie(trainingNode: JsonNode): String {
        val trainingssessies = trainingNode.get("trainingssessies")
        val laatseTrainingssessie = trainingssessies[trainingssessies.size() - 1]

        val startdatumNode = laatseTrainingssessie.get("sessieDatum").asText()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        return formatter.format(ZonedDateTime.parse(startdatumNode))
    }

    fun getTrainingscreatieDocumentId(trainingsbeheerDocumentId: String): String {

        val trainingsbeheerDocument = getDocumentById(trainingsbeheerDocumentId)

        return trainingsbeheerDocument.content().asJson().get("trainingscreatieDocumentId").asText()
    }

    private fun getDocumentById(businessKey: String): Document {
        return runWithoutAuthorization { documentService.get(businessKey) }
    }


    companion object {
        private val logger = KotlinLogging.logger {}

        const val CONNECTOR_CASE_ID_KEY = "trainingsObjectCaseId"
         const val TRAINING_KEY = "training"
    }
}