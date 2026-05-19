package com.ritense.valtimo.ooievaarspas.bpmn

import com.ritense.processdocument.service.DocumentDelegateService
import com.ritense.valtimo.TestHelper
import com.ritense.valtimo.common.service.ZaakHandleService
import com.ritense.valtimo.common.service.ZaakPropertiesService
import com.ritense.valtimo.ooievaarspas.service.OoievaarspasProcessService
import com.ritense.valtimoplugins.suwinet.service.DateTimeService
import jakarta.inject.Inject
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.test.Deployment
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests
import org.camunda.bpm.engine.test.mock.Mocks
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension
import org.camunda.bpm.extension.mockito.ProcessExpressions
import org.camunda.bpm.scenario.ProcessScenario
import org.camunda.bpm.scenario.Scenario
import org.camunda.bpm.scenario.act.EventBasedGatewayAction
import org.camunda.bpm.scenario.delegate.EventBasedGatewayDelegate
import org.camunda.bpm.scenario.delegate.TaskDelegate
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.mockito.quality.Strictness


@Deployment(
    resources = ["config/bpmn/ooievaarspas/ooievaarspas-opvragen-informatie.bpmn",
        "config/bpmn/ooievaarspas/ooievaarspas-informeren-aanvrager.bpmn",
        "config/bpmn/ooievaarspas/ooievaarspas-controleren-afhandeltermijn.bpmn",
        "config/bpmn/ooievaarspas/ooievaarspas-genereer-document.bpmn",
        "config/bpmn/common/generic-update-zaak-status.bpmn"]

)
@ExtendWith(MockitoExtension::class, ProcessEngineExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
@Disabled("Out of date")
class OoievaarspasOpvragenInformatieProcessTest {

    @Inject
    lateinit var processEngine: ProcessEngine

    @Mock
    lateinit var processScenario: ProcessScenario

    @Mock
    lateinit var documentDelegateService: DocumentDelegateService

    @Mock
    lateinit var zaakPropertiesService: ZaakPropertiesService

    @Mock
    lateinit var zaakHandleService: ZaakHandleService

    @Mock
    lateinit var dateTimeService: DateTimeService

    @Mock
    lateinit var ooievaarspasProcessService: OoievaarspasProcessService

    //Register any mock services that might be used in the process
    @BeforeEach
    fun setup() {
        Mocks.register("documentDelegateService", documentDelegateService)
        Mocks.register("zaakPropertiesService", zaakPropertiesService)
        Mocks.register("zaakHandleService", zaakHandleService)
        Mocks.register("documentDelegateService", documentDelegateService)
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
        ProcessExpressions.registerCallActivityMock("generic-update-zaak-status")
            .deploy(processEngine)

        ProcessExpressions.registerCallActivityMock(TestHelper.PROCESS_DEFINITION_ID_INFORMEREN_AANVRAGER_PROCESS)
            .onExecutionAddVariable("portaalnotificatieVerstuurd", false)
            .deploy(processEngine)

        ProcessExpressions.registerCallActivityMock("ooievaarspas-controleren-afhandeltermijn")
            .deploy(processEngine)

        ProcessExpressions.registerCallActivityMock("ooievaarspas-genereer-document")
            .deploy(processEngine)

        whenever(processScenario.waitsAtEventBasedGateway("KlantReactieEventBasedGateway"))
            .thenReturn(
                EventBasedGatewayAction { gateway: EventBasedGatewayDelegate ->
                    gateway.getEventSubscription("ReactieOntvangenEvent").receive()
                })

        whenever(documentDelegateService.findValueByJsonPointer(eq("/communicatievoorkeur"), any()))
            .thenReturn("Post")

        whenever(ooievaarspasProcessService.getPropertyOfLatestVerzoekOrDefault(any(), any(), any()))
            .thenReturn(TestHelper.ZONEDDATETIMESTRING_FUTURE)

        //scenario
        val scenario = communicationViaEmailOrPostScenario()

        //assertion
        BpmnAwareTests.assertThat(scenario.instance(processScenario))
            .hasPassedInOrder(
                "StartEvent",
                "ControlerenInformatieverzoekTask",
                "InformatieverzoekMetHersteltermijnGateway",
                "ZetStartHersteltermijnGateway",
                "InformerenAanvragerCallActivity",
                "UpdateZaakstatusCallActivity",
                "OphalenReactietermijnUitHetDocumentTask",
                "PortaalNotificatieVerstuurdGateway",
                "KlantReactieEventBasedGateway",
                "ReactieOntvangenEvent",
                "ControlerenAfhandeltermijnCallActivity",
                "GenererenReactieOntvangstbevestigingCallActivity",
                "InformerenAanvragerViaPostCallActivity",
                "InformatieAangeleverdEndEvent1"
            ).isEnded

    }


    //@Test
    fun `should ask for additional information via portal`() {

        //declarations
        ProcessExpressions.registerCallActivityMock("generic-update-zaak-status")
            .deploy(processEngine)

        ProcessExpressions.registerCallActivityMock("ooievaarspas-controleren-afhandeltermijn")
            .deploy(processEngine)

        whenever(processScenario.waitsAtUserTask("AanleverenInformatieTask"))
            .thenReturn(TaskDelegate::complete)

        whenever(documentDelegateService.findValueByJsonPointer(eq("/communicatievoorkeur"), any()))
            .thenReturn("Portaal")

        ProcessExpressions.registerCallActivityMock(TestHelper.PROCESS_DEFINITION_ID_INFORMEREN_AANVRAGER_PROCESS)
            .onExecutionAddVariable("portaalnotificatieVerstuurd", true)
            .deploy(processEngine)

        whenever(ooievaarspasProcessService.getPropertyOfLatestVerzoekOrDefault(any(), any(), any()))
            .thenReturn(TestHelper.ZONEDDATETIMESTRING_FUTURE)

        //scenario
        val scenario = communicationViaPortalScenario()

        //assertion
        BpmnAwareTests.assertThat(scenario.instance(processScenario))
            .hasPassedInOrder(
                "StartEvent",
                "ControlerenInformatieverzoekTask",
                "InformatieverzoekMetHersteltermijnGateway",
                "InformerenAanvragerCallActivity",
                "UpdateZaakstatusCallActivity",
                "OphalenReactietermijnUitHetDocumentTask",
                "PortaalNotificatieVerstuurdGateway",
                "AanleverenInformatieTask",
                "ControlerenAfhandeltermijnPortaalCallActivity",
                "InformerenAanvrageViaPortaalCallActivity",
                "InformatieAangeleverdEndEvent"
            ).isEnded

    }


    private fun communicationViaPortalScenario(): Scenario {
        return Scenario.run(processScenario)
            .startByKey(
                "ooievaarspas-opvragen-informatie",
                TestHelper.DOCUMENT_ID
            )
            .execute()
    }

    private fun communicationViaEmailOrPostScenario(): Scenario {
        return Scenario.run(processScenario)
            .startByKey(
                "ooievaarspas-opvragen-informatie",
                TestHelper.DOCUMENT_ID
            )
            .execute()
    }
}
