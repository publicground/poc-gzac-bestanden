package com.ritense.valtimo.common.service

import com.ritense.document.domain.Document
import com.ritense.document.service.DocumentService
import com.ritense.valtimo.BaseTest
import com.ritense.valtimo.TestHelper.CASE_ID
import com.ritense.valtimo.TestHelper.DOCUMENT_ID
import com.ritense.valtimo.financieletrainers.service.TimerService
import org.assertj.core.api.Assertions.assertThat
import org.camunda.bpm.engine.ManagementService
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.runtime.Job
import org.camunda.bpm.extension.mockito.delegate.DelegateExecutionFake
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Answers
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.quality.Strictness
import java.time.Clock


private const val PROCESS_INSTANCE_ID = "df08455f-71ee-4637-b736-81cbd4bd551c"
private const val CURRENT_UITERSTE_INSCHRIJFDATUM_VALUE = "2022-07-15T00:00:00+02:00"
private const val NEW_UITERSTE_INSCHRIJFDATUM_VALUE = "2022-07-22T00:00:00+02:00"

@MockitoSettings(strictness = Strictness.LENIENT)
internal class TimerServiceTest : BaseTest() {

    @Mock
    lateinit var documentService: DocumentService

    @Mock
    lateinit var messageCorrelationService: MessageCorrelationService

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    lateinit var managementService: ManagementService

    @Mock
    lateinit var job: Job

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    lateinit var processEngine: ProcessEngine

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    lateinit var document: Document

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    lateinit var clock: Clock

    @InjectMocks
    lateinit var timerService: TimerService

    lateinit var execution: DelegateExecution

    @BeforeEach
    fun setup() {
        execution = delegateExecution()
    }

    @Test
    fun `should create message correlation`() {
        //given
        loadDocumentConditions(NEW_UITERSTE_INSCHRIJFDATUM_VALUE)

        //when
        timerService.sendTimerChangeRequestMessage(execution)

        //then
        verify(messageCorrelationService, times(1)).sendCorrelationMessageByBusinessKey(any(), any())
    }

    @Test
    fun `should get uiterste inschrijfdatum from document and return true if current date is after uiterste inschrijfdatum`() {
        //given
        loadDocumentConditions(NEW_UITERSTE_INSCHRIJFDATUM_VALUE)

        //when
        val result = timerService.checkUitersteInschrijfDatum(DOCUMENT_ID)

        //then
        assertThat(result).isTrue
    }


    private fun loadDocumentConditions(date: String?) {
        whenever(documentService.get(any())).thenReturn(document)
        whenever(document.content().asJson().findValue(anyString()).asText()).thenReturn(date)
    }

    private fun delegateExecution(): DelegateExecution {
        return DelegateExecutionFake()
            .withBusinessKey(DOCUMENT_ID)
            .withProcessInstanceId(PROCESS_INSTANCE_ID)
            .withProcessEngine(processEngine)
            .withVariables(
                mapOf(
                    "newUitersteInschrijfdatum" to CURRENT_UITERSTE_INSCHRIJFDATUM_VALUE,
                    "uitersteInschrijfdatum" to CURRENT_UITERSTE_INSCHRIJFDATUM_VALUE,
                    "trainingsObjectCaseId" to CASE_ID
                )
            )
    }
}