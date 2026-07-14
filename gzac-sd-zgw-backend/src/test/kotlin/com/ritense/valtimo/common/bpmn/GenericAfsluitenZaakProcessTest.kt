package com.ritense.valtimo.ooievaarspas.bpmn

import com.ritense.valtimo.TestHelper
import com.ritense.valtimo.common.service.ZaakHandleService
import com.ritense.valtimo.common.service.ZaakPropertiesService
import jakarta.inject.Inject
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.test.Deployment
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests
import org.camunda.bpm.engine.test.mock.Mocks
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension
import org.camunda.bpm.extension.mockito.ProcessExpressions
import org.camunda.bpm.scenario.ProcessScenario
import org.camunda.bpm.scenario.Scenario
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension


@Deployment(
    resources = ["config/bpmn/common/generic-afsluiten-zaak.bpmn", "config/bpmn/common/generic-update-zaak-status.bpmn"]
)
@ExtendWith(MockitoExtension::class, ProcessEngineExtension::class)
class GenericAfsluitenZaakProcessTest {

    @Inject
    lateinit var processEngine: ProcessEngine

    @Mock
    lateinit var processScenario: ProcessScenario

    @Mock
    lateinit var zaakHandleService: ZaakHandleService

    @Mock
    lateinit var zaakPropertiesService: ZaakPropertiesService

    //Register any mock services that might be used in the process
    @BeforeEach
    fun setup() {
        Mocks.register("zaakHandleService", zaakHandleService)
        Mocks.register("zaakPropertiesService", zaakPropertiesService)
    }

    @AfterEach
    fun teardown() {
        Mocks.reset()
    }

//    @Test
    fun `happy flow`() {

        //declarations
        ProcessExpressions.registerCallActivityMock("generic-update-zaak-status")
            .deploy(processEngine)

        //scenario
        val scenario = runScenario()

        //assertion
        BpmnAwareTests.assertThat(scenario.instance(processScenario))
            .hasPassedInOrder(
                "StartZaakUpdateStartEvent",
                "UpdateZaakResultaatTask",
                "UpdateZaakStatusCallActivity",
                "EndEvent"
            ).isEnded
    }


    private fun runScenario(): Scenario {
        return Scenario.run(processScenario)
            .startByKey(
                "generic-afsluiten-zaak",
                TestHelper.DOCUMENT_ID,
                TestHelper.VARIABLE_MAP
            )
            .execute()
    }
}