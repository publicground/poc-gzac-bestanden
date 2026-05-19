package com.ritense.valtimo.waarderingmantelzorgersenvrijwilligers

import com.ritense.mail.service.MailService
import com.ritense.processdocument.service.DocumentDelegateService
import com.ritense.valtimo.TestHelper
import com.ritense.valtimo.common.service.CustomObjectenApiService
import com.ritense.valtimo.common.service.CustomZaakObjectService
import com.ritense.valtimo.common.service.DocumentWriterService
import com.ritense.valtimo.waarderingmantelzorgersenvrijwilligers.service.WmvWaarderingsobjectService
import jakarta.inject.Inject
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.test.Deployment
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests
import org.camunda.bpm.engine.test.mock.Mocks
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension
import org.camunda.bpm.extension.mockito.ProcessExpressions
import org.camunda.bpm.scenario.ProcessScenario
import org.camunda.bpm.scenario.Scenario
import org.camunda.bpm.scenario.act.MessageIntermediateCatchEventAction
import org.camunda.bpm.scenario.delegate.EventSubscriptionDelegate
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@Deployment(
    resources = [
        "config/bpmn/waardering-mantelzorgers-vrijwilligers/wmv-verwerken-aanvraag.bpmn",
        "config/bpmn/common/generic-update-zaak-status.bpmn",
        "config/bpmn/common/generic-afsluiten-zaak.bpmn"
    ]
)

@ExtendWith(MockitoExtension::class, ProcessEngineExtension::class)
@Disabled("Out of date")
class WmvVerwerkenAanvraagProcessTest {

    @Inject
    lateinit var processEngine: ProcessEngine

    @Mock
    lateinit var processScenario: ProcessScenario

    @Mock
    lateinit var documentDelegateService: DocumentDelegateService

    @Mock
    lateinit var mailService: MailService

    @Mock
    lateinit var documentWriterService: DocumentWriterService

    @Mock
    lateinit var customObjectenApiService: CustomObjectenApiService

    @Mock
    lateinit var wmvWaarderingsobjectService: WmvWaarderingsobjectService

    @Mock
    lateinit var customZaakObjectService: CustomZaakObjectService

    //Register any mock services that might be used in the process
    @BeforeEach
    fun setup() {
        Mocks.register("documentDelegateService", documentDelegateService)
        Mocks.register("mailService", mailService)
        Mocks.register("documentWriterService", documentWriterService)
        Mocks.register("customObjectenApiService", customObjectenApiService)
        Mocks.register("wmvWaarderingsobjectService", wmvWaarderingsobjectService)
        Mocks.register("customZaakObjectService", customZaakObjectService)
    }

    @AfterEach
    fun teardown() {
        Mocks.reset()
    }

    @Test
    fun `happy flow`() {
        ProcessExpressions.registerCallActivityMock("generic-afsluiten-zaak")
            .deploy(processEngine)
        ProcessExpressions.registerCallActivityMock("generic-update-zaak-status")
            .deploy(processEngine)

        whenever(documentDelegateService.findValueByJsonPointer(any(), any()))
            .thenReturn("test@example.com")

        whenever(processScenario.waitsAtMessageIntermediateCatchEvent("NotificatieObjectGewijzigdOntvangen"))
            .thenReturn(
                MessageIntermediateCatchEventAction { message: EventSubscriptionDelegate? ->
                    message?.receive()
                })


        //scenario
        val scenario = runScenario()

        //assertion
        BpmnAwareTests.assertThat(scenario.instance(processScenario))
            .hasPassedInOrder(
                "VerzoekOntvangenStartEvent",
                "UpdateZaakstatusInBehandelingCallactivity",
                "AanmakenWaarderingsobject",
                "LinkWaarderingsobjectAanZaak",
                "VersturenOntvangstbevestiging",
                "UpdateZaakstatusInAfwachtingBehandelingCallactivity",
                "NotificatieObjectGewijzigdOntvangen",
                "AfsluitenZaak"
            ).isEnded
    }

    private fun runScenario(): Scenario {
        return Scenario.run(processScenario)
            .startByKey(
                "wmv-verwerken-aanvraag",
                TestHelper.DOCUMENT_ID
            )
            .execute()
    }
}