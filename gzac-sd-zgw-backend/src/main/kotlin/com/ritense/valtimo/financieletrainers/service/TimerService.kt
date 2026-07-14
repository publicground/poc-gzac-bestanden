package com.ritense.valtimo.financieletrainers.service

import com.ritense.authorization.AuthorizationContext.Companion.runWithoutAuthorization
import com.ritense.document.service.DocumentService
import com.ritense.valtimo.common.service.MessageCorrelationService
import mu.KotlinLogging
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.runtime.Job
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

class TimerService(
    private val documentService: DocumentService,
    private val messageCorrelationService: MessageCorrelationService
) {

    //TODO: refactor time parsing in another feature
    fun checkUitersteInschrijfDatum(documentId: String): Boolean {
        val document = runWithoutAuthorization { documentService.get(documentId) }
        val uitersteInschrijfDatumAsString = document.content().asJson().findValue(UITERSTE_INSCHRIJFDATUM_KEY).asText()
        val uitersteInschrijfDatum =
            dateToLocalDate(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(uitersteInschrijfDatumAsString))

        return LocalDate.now().isAfter(uitersteInschrijfDatum)
    }

    fun sendTimerChangeRequestMessage(execution: DelegateExecution) {
        logger.debug { "Sending Message $TIMER_MESSAGE_VALUE" }

        messageCorrelationService.sendCorrelationMessageByBusinessKey(
            businessKey = execution.businessKey,
            message = TIMER_MESSAGE_VALUE
        )
    }

    private fun changeTimer(
        processInstanceId: String,
        engine: ProcessEngine,
        newDueDate: Date
    ): Date {

        //get the timer
        val timerJob: Job = engine.managementService
            .createJobQuery()
            .timers()
            .processInstanceId(processInstanceId)
            .singleResult()

        logger.debug {
            "Changing the date of timer ${timerJob.id} from ${timerJob.duedate} to $newDueDate " +
                "for process instance $processInstanceId"
        }

        //change the due date
        engine.managementService.setJobDuedate(timerJob.id, newDueDate)

        return timerJob.duedate
    }

    private fun getUitersteInschrijfdatumFromDocument(documentId: String): String {
        val document = runWithoutAuthorization { documentService.get(documentId) }
        return document.content().asJson().findValue(UITERSTE_INSCHRIJFDATUM_KEY).asText()
    }

    private fun dateToLocalDate(date: Date): LocalDate {
        return Instant.ofEpochMilli(date.time)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    companion object {
        private val logger = KotlinLogging.logger {}

        private const val TIMER_MESSAGE_VALUE = "MESSAGE_DATUM_GEWIJZIGD"
        private const val UITERSTE_INSCHRIJFDATUM_KEY = "uitersteInschrijfdatum"
    }
}