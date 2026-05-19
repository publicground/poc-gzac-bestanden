package com.ritense.valtimo.ooievaarspas.bpmn

import com.ritense.valtimo.TestHelper
import com.ritense.valtimo.common.service.ZaakHandleService
import com.ritense.valtimo.common.service.ZaakPropertiesService
import org.camunda.bpm.engine.test.Deployment
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests
import org.camunda.bpm.engine.test.mock.Mocks
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension
import org.camunda.bpm.scenario.ProcessScenario
import org.camunda.bpm.scenario.Scenario
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify


@Deployment(
    resources = ["config/bpmn/common/generic-update-zaak-status.bpmn"]
)
@ExtendWith(MockitoExtension::class, ProcessEngineExtension::class)
class GenericUpdateZaakStatusProcessTest {

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

        //scenario
        val scenario = runScenario()

        //assertion
        BpmnAwareTests.assertThat(scenario.instance(processScenario))
            .hasPassedInOrder(
                "StartZaakUpdateStartEvent",
                "ZetZaakstatusTask",
                "UpdateZaakPropertiesInDocumentTask",
                "EndEvent"
            ).isEnded

        verify(zaakHandleService, times(1)).setZaakStatus(any(), any())
        verify(zaakPropertiesService, times(1))
            .storeSelectedZaakPropertiesInDocumentRoot(any(), any(), any(), any())
    }


    private fun runScenario(): Scenario {
        return Scenario.run(processScenario)
            .startByKey(
                "generic-update-zaak-status",
                TestHelper.DOCUMENT_ID,
                TestHelper.VARIABLE_MAP
            )
            .execute()
    }
}