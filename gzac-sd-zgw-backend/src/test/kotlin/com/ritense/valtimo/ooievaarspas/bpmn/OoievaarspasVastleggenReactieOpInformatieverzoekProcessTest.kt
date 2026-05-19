package com.ritense.valtimo.ooievaarspas.bpmn

import com.ritense.valtimo.TestHelper
import com.ritense.valtimo.common.service.MessageCorrelationService
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
import org.mockito.kotlin.times
import org.mockito.kotlin.any
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify

@Deployment(
    resources = ["config/bpmn/ooievaarspas/ooievaarspas-vastleggen-reactie-op-informatieverzoek.bpmn"]
)
@ExtendWith(MockitoExtension::class, ProcessEngineExtension::class)
@Disabled("Out of date")
class OoievaarspasVastleggenReactieOpInformatieverzoekProcessTest {

    @Mock
    lateinit var processScenario: ProcessScenario

    @Mock
    lateinit var messageCorrelationService: MessageCorrelationService


    @BeforeEach
    fun setup() {
        Mocks.register("messageCorrelationService", messageCorrelationService)
    }

    @AfterEach
    fun teardown() {
        Mocks.reset()
    }

    //@Test
    fun `happy flow`() {

        //scenario
        val scenario = runScenario()

        //assertion
        BpmnAwareTests.assertThat(scenario.instance(processScenario))
            .hasPassedInOrder(
                "StartVastleggenReactieStartEvent",
                "ReactieIsVastgelegdEndEventEndEvent"
            ).isEnded

        verify(messageCorrelationService, times(1)).sendCorrelationMessageByBusinessKey(any(), any())

    }

    private fun runScenario(): Scenario {
        return Scenario.run(processScenario)
            .startByKey(
                "ooievaarspas-vastleggen-reactie-op-informatieverzoek",
                TestHelper.DOCUMENT_ID
            )
            .execute()
    }
}
