package com.ritense.valtimo.ooievaarspas.bpmn

import com.ritense.valtimo.TestHelper
import com.ritense.valtimo.TestHelper.ZONEDDATETIMESTRING_END
import com.ritense.valtimo.common.service.DocumentReaderService
import com.ritense.valtimo.common.service.MessageCorrelationService
import com.ritense.valtimo.ooievaarspas.service.OoievaarspasProcessService
import com.ritense.valtimoplugins.suwinet.service.DateTimeService
import org.camunda.bpm.engine.test.Deployment
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests
import org.camunda.bpm.engine.test.mock.Mocks
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension
import org.camunda.bpm.scenario.ProcessScenario
import org.camunda.bpm.scenario.Scenario
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


@Deployment(
    resources = ["config/bpmn/ooievaarspas/ooievaarspas-controleren-afhandeltermijn.bpmn"]
)
@ExtendWith(MockitoExtension::class, ProcessEngineExtension::class)
@Disabled("Out of date")
class OoievaarspasControlerenAfhandeltermijnProcessTest {

    @Mock
    lateinit var processScenario: ProcessScenario

    @Mock
    lateinit var messageCorrelationService: MessageCorrelationService

    @Mock
    lateinit var ooievaarspasProcessService: OoievaarspasProcessService

    @Mock
    lateinit var dateTimeService: DateTimeService

    @Mock
    lateinit var documentReaderService: DocumentReaderService

    //Register any mock services that might be used in the process
    @BeforeEach
    fun setup() {
        Mocks.register("ooievaarspasProcessService", ooievaarspasProcessService)
        Mocks.register("messageCorrelationService", messageCorrelationService)
        Mocks.register("dateTimeService", dateTimeService)
        Mocks.register("documentReaderService", documentReaderService)
    }

    @AfterEach
    fun teardown() {
        Mocks.reset()
    }

    //@Test
    fun `with hersteltermijn flow and no escalation`() {

        //declarations
        whenever(ooievaarspasProcessService.getHersteltermijnVanLaatsteInformatieverzoek(any()))
            .thenReturn(true)

        whenever(documentReaderService.getValueFromDocumentAtPathOrDefault(any(),any(),any()))
            .thenReturn(false)

        whenever(ooievaarspasProcessService.setStopHersteltermijnForLatestVerzoekInDocument(any()))
            .thenReturn(ZONEDDATETIMESTRING_END)

        //scenario
        val scenario = runScenario()

        //assertion
        BpmnAwareTests.assertThat(scenario.instance(processScenario))
            .hasPassedInOrder(
                "StartEvent",
                "ControlerenInformatieverzoekTask",
                "InformatieverzoekMetHersteltermijnGateway",
                "ControlerenHersteltermijnTask",
                "HersteltermijnAlAfgelopenGateway",
                "StopHersteltermijnTask",
                "BerekenTijdsverschilHersteltermijnTask",
                "AanpassenAfhandeltermijnEvent",
                "Gateway_090455e",
                "EndEvent"
            ).isEnded

        verify(ooievaarspasProcessService, times(1)).getHersteltermijnVanLaatsteInformatieverzoek(any())
        verify(ooievaarspasProcessService, times(1)).setStopHersteltermijnForLatestVerzoekInDocument(any())
        verify(messageCorrelationService, times(1)).sendCorrelationMessageByBusinessKeyWithVariable(any(),any(),any(),any())
        verify(documentReaderService, times(1)).getValueFromDocumentAtPathOrDefault(any(),any(),any())

    }

    //@Test
    fun `with hersteltermijn flow and escalation`() {

        //declarations
        whenever(ooievaarspasProcessService.getHersteltermijnVanLaatsteInformatieverzoek(any()))
            .thenReturn(true)

        whenever(documentReaderService.getValueFromDocumentAtPathOrDefault(any(),any(),any()))
            .thenReturn(true)

        //scenario
        val scenario = runScenario()

        //assertion
        BpmnAwareTests.assertThat(scenario.instance(processScenario))
            .hasPassedInOrder(
                "StartEvent",
                "ControlerenInformatieverzoekTask",
                "InformatieverzoekMetHersteltermijnGateway",
                "ControlerenHersteltermijnTask",
                "HersteltermijnAlAfgelopenGateway",
                "Gateway_090455e",
                "EndEvent"
            ).isEnded

        verify(ooievaarspasProcessService, times(1)).getHersteltermijnVanLaatsteInformatieverzoek(any())
        verify(documentReaderService, times(1)).getValueFromDocumentAtPathOrDefault(any(),any(),any())

    }

    //@Test
    fun `without hersteltermijn flow`() {

        //declarations
        whenever(ooievaarspasProcessService.getHersteltermijnVanLaatsteInformatieverzoek(any()))
            .thenReturn(false)

        //scenario
        val scenario = runScenario()

        //assertion
        BpmnAwareTests.assertThat(scenario.instance(processScenario))
            .hasPassedInOrder(
                "StartEvent",
                "ControlerenInformatieverzoekTask",
                "InformatieverzoekMetHersteltermijnGateway",
                "Gateway_090455e",
                "EndEvent"
            ).isEnded

        verify(ooievaarspasProcessService, times(1)).getHersteltermijnVanLaatsteInformatieverzoek(any())

    }


    private fun runScenario(): Scenario {
        return Scenario.run(processScenario)
            .startByKey(
                "ooievaarspas-controleren-afhandeltermijn",
                TestHelper.DOCUMENT_ID
            )
            .execute()
    }
}
