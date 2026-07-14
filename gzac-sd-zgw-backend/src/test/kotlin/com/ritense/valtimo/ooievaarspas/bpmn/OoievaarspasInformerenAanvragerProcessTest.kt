package com.ritense.valtimo.ooievaarspas.bpmn

import com.ritense.mail.service.MailService
import com.ritense.processdocument.service.DocumentDelegateService
import com.ritense.valtimo.TestHelper
import com.ritense.valtimo.TestHelper.CURRENT_DATE
import com.ritense.valtimo.common.service.DocumentReaderService
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
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever


@Deployment(
    resources = ["config/bpmn/ooievaarspas/ooievaarspas-informeren-aanvrager.bpmn"]
)
@ExtendWith(MockitoExtension::class, ProcessEngineExtension::class)
@Disabled("Out of date")
class OoievaarspasInformerenAanvragerProcessTest {

    @Mock
    lateinit var processScenario: ProcessScenario

    @Mock
    lateinit var documentDelegateService: DocumentDelegateService

    @Mock
    lateinit var documentReaderService: DocumentReaderService

    @Mock
    lateinit var mailService: MailService

    @Mock
    lateinit var dateTimeService: DateTimeService

    @Mock
    lateinit var ooievaarspasProcessService: OoievaarspasProcessService

    //Register any mock services that might be used in the process
    @BeforeEach
    fun setup() {
        Mocks.register("documentDelegateService", documentDelegateService)
        Mocks.register("documentReaderService", documentReaderService)
        Mocks.register("mailService", mailService)
        Mocks.register("ooievaarspasProcessService", ooievaarspasProcessService)
        Mocks.register("dateTimeService", dateTimeService)
    }

    @AfterEach
    fun teardown() {
        Mocks.reset()
    }

    //@Test
    fun `happy flow`() {

        //declarations
        whenever(documentReaderService.getValueFromDocumentAtPathOrDefault(eq("/communicatievoorkeur"), any(), any()))
            .thenReturn("Portaal")

        whenever(documentReaderService.getValueFromDocumentAtPathOrDefault(eq("/datumAanvraagOntvangen"), any(), any()))
            .thenReturn(CURRENT_DATE)

        whenever(documentReaderService.getValueFromDocumentAtPathOrDefault(eq("/aanvrager/voornaam"), any(), any()))
            .thenReturn("John")

        whenever(documentReaderService.getValueFromDocumentAtPathOrDefault(eq("/aanvrager/achternaam"), any(), any()))
            .thenReturn("Dijk")

        whenever(documentReaderService.getValueFromDocumentAtPathOrDefault(eq("/aanvrager/tussenvoegsel"), any(), any()))
            .thenReturn("")

        whenever(documentReaderService.getValueFromDocumentAtPathOrDefault(eq("/aanvrager/emailadres"), any(), any()))
            .thenReturn("John@test.nl")


        //scenario
        val scenario = runScenario()

        //assertion
        BpmnAwareTests.assertThat(scenario.instance(processScenario))
            .hasPassedInOrder(
                "StartEvent",
                "VoorbereidenProcesVariabelenTask",
                "CommunicatieVoorkeurGateway",
                "VersturenEmailNotificatieTask",
                "PortaalNotificatieVerstuurdEndEvent"
            ).isEnded

    }


    private fun runScenario(): Scenario {
        return Scenario.run(processScenario)
            .startByKey(
                "ooievaarspas-informeren-aanvrager",
                TestHelper.DOCUMENT_ID
            )
            .execute()
    }
}
