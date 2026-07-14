package com.ritense.valtimo.ooievaarspas.bpmn

import com.ritense.mail.service.MailService
import com.ritense.processdocument.service.DocumentDelegateService
import com.ritense.valtimo.TestHelper
import com.ritense.valtimo.common.service.DocumentReaderService
import com.ritense.valtimo.common.service.DocumentWriterService
import com.ritense.valtimo.common.service.ZaakHandleService
import com.ritense.valtimo.common.service.ZaakPropertiesService
import com.ritense.valtimo.ooievaarspas.mapper.OoievaarspasAanvraagMapper
import com.ritense.valtimo.ooievaarspas.service.OoievaarspasOpgegevenDataToBeoordelingService
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
import org.camunda.bpm.scenario.delegate.TaskDelegate
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.eq


@Deployment(
    resources = ["config/bpmn/ooievaarspas/ooievaarspas-afhandelen-aanvraag.bpmn",
        "config/bpmn/ooievaarspas/temporary/ooievaarspas-form-sequence.bpmn",
        "config/bpmn/common/generic-update-zaak-status.bpmn",
        "config/bpmn/common/generic-afsluiten-zaak.bpmn",
        "config/bpmn/ooievaarspas/ooievaarspas-opvragen-informatie.bpmn",
        "config/bpmn/ooievaarspas/ooievaarspas-informeren-aanvrager.bpmn",
        "config/bpmn/ooievaarspas/ooievaarspas-genereer-document.bpmn",
        "config/dmn/ooievaarspas/ooievaarspas-bepalen-beschikking-template.dmn",
        "config/dmn/ooievaarspas/ooievaarspas-bepaal-zaakresultaat.dmn"]
)
@ExtendWith(MockitoExtension::class, ProcessEngineExtension::class)
@Disabled("Out of date")
class OoievaarspasAfhandelenAanvraagProcessTest {

    @Inject
    lateinit var processEngine: ProcessEngine

    @Mock
    lateinit var processScenario: ProcessScenario

    @Mock
    lateinit var zaakPropertiesService: ZaakPropertiesService

    @Mock
    lateinit var zaakHandleService: ZaakHandleService

    @Mock
    lateinit var documentDelegateService: DocumentDelegateService

    @Mock
    lateinit var ooievaarspasProcessService: OoievaarspasProcessService

    @Mock
    lateinit var dateTimeService: DateTimeService

    @Mock
    lateinit var documentReaderService: DocumentReaderService

    @Mock
    lateinit var documentWriterService: DocumentWriterService

    @Mock
    lateinit var mailService: MailService

    @Mock
    lateinit var ooievaarspasAanvraagMapper: OoievaarspasAanvraagMapper

    @Mock
    lateinit var ooievaarspasOpgegevenDataToBeoordelingService: OoievaarspasOpgegevenDataToBeoordelingService


    //Register any mock services that might be used in the process
    @BeforeEach
    fun setup() {
        Mocks.register("zaakPropertiesService", zaakPropertiesService)
        Mocks.register("zaakHandleService", zaakHandleService)
        Mocks.register("DocumentDelegateService", documentDelegateService)
        Mocks.register("ooievaarspasProcessService", ooievaarspasProcessService)
        Mocks.register("dateTimeService", dateTimeService)
        Mocks.register("documentReaderService", documentReaderService)
        Mocks.register("documentWriterService", documentWriterService)
        Mocks.register("mailService", mailService)
        Mocks.register("ooievaarspasAanvraagMapper", ooievaarspasAanvraagMapper)
        Mocks.register("ooievaarspasOpgegevenDataToBeoordelingService", ooievaarspasOpgegevenDataToBeoordelingService)
    }

    @AfterEach
    fun teardown() {
        Mocks.reset()
    }

    //@Test
    fun `happy flow`() {

        //declarations
        ProcessExpressions.registerCallActivityMock("ooievaarspas-form-sequence")
            .deploy(processEngine)
        ProcessExpressions.registerCallActivityMock("ooievaarspas-opvragen-informatie")
            .deploy(processEngine)
        ProcessExpressions.registerCallActivityMock("ooievaarspas-informeren-aanvrager")
            .deploy(processEngine)
        ProcessExpressions.registerCallActivityMock("generic-afsluiten-zaak")
            .deploy(processEngine)
        ProcessExpressions.registerCallActivityMock("generic-update-zaak-status")
            .deploy(processEngine)
        ProcessExpressions.registerCallActivityMock("ooievaarspas-genereer-document")
            .deploy(processEngine)

        whenever(processScenario.waitsAtUserTask("BepaalZaakbehandelaarTask"))
            .thenReturn(TaskDelegate::complete)

        whenever(documentDelegateService.findValueByJsonPointer(any(), any()))
            .thenReturn(false)

        whenever(documentReaderService.getValueFromDocumentAtPathOrDefault(any(), any(), eq("")))
            .thenReturn(TestHelper.ZONEDDATETIMESTRING_FUTURE)

        //scenario
        val scenario = runScenario()

        //assertion
        BpmnAwareTests.assertThat(scenario.instance(processScenario))
            .hasPassedInOrder(
                "ProductaanvraagOntvangenStartEvent",
                "AfhandelenAanvraagExpandedSubProcess",
                "AfhandelenAanvraagStartEvent",
                "VertalenOntvangenDataNaarDocumentTask",
                "KopierenOpgegevensDataNaarBeoordelingTask",
                "ZetAanvraagOntvangenDatumTask",
                "GenererenOntvangstbevestigingTask",
                "ZetDefaultCommunicatievoorkeurTask",
                "InformerenAanvragerCallActivity",
                "UpdateZaakstatusCallActivity",
                "BepaalZaakbehandelaarTask",
                "Gateway_0kdnrmv",
                "UpdateZaakstatusCallActivity1",
                "BeoordelenAfhandelenAanvraagFormflowCallActivity",
                "BesluitGenomenGateway",
                "GenererenRapportageCallActivity",
                "BepalenBeschikkingTemplateDMNTask",
                "GenererenBeschikkingCallActivity",
                "VastleggenBesluitTask",
                "BepalenZaakresultaatDMNTask",
                "AanvraagToegekendGateway",
                "Gateway_1ohjxeg",
                "InformerenAanvragerVersturenBesluitCallActivity",
                "AfsluitenZaakCallActivity",
                "AanvraagAfgehandeldEndEvent"
            ).isEnded

        verify(ooievaarspasAanvraagMapper, times(1)).mapAanvragInDocumentByBusinessKey(any())
    }

    //@Test
    fun `should use additional information flow once before finishing on happy flow`() {

        //declarations
        ProcessExpressions.registerCallActivityMock("ooievaarspas-opvragen-informatie")
            .deploy(processEngine)
        ProcessExpressions.registerCallActivityMock("ooievaarspas-informeren-aanvrager")
            .deploy(processEngine)
        ProcessExpressions.registerCallActivityMock("generic-afsluiten-zaak")
            .deploy(processEngine)
        ProcessExpressions.registerCallActivityMock("generic-update-zaak-status")
            .deploy(processEngine)
        ProcessExpressions.registerCallActivityMock("ooievaarspas-form-sequence")
            .deploy(processEngine)
        ProcessExpressions.registerCallActivityMock("ooievaarspas-genereer-document")
            .onExecutionAddVariable("informatieOpvragen", true)
            .deploy(processEngine)

        whenever(documentReaderService.getValueFromDocumentAtPathOrDefault(any(), any(), eq("")))
            .thenReturn("01/01/2024")

        whenever(ooievaarspasProcessService.getPropertyOfLatestVerzoekOrDefault(any(), any(), eq("")))
            .thenReturn("")

        whenever(processScenario.waitsAtUserTask("BepaalZaakbehandelaarTask"))
            .thenReturn(TaskDelegate::complete)

        whenever(documentDelegateService.findValueByJsonPointer(any(), any()))
            .thenReturn(true) //first invocation
            .thenReturn(false) //second invocation


        //scenario
        val scenario = runScenario()

        //assertion
        BpmnAwareTests.assertThat(scenario.instance(processScenario))
            .hasPassedInOrder(
                "ProductaanvraagOntvangenStartEvent",
                "AfhandelenAanvraagExpandedSubProcess",
                "AfhandelenAanvraagStartEvent",
                "VertalenOntvangenDataNaarDocumentTask",
                "KopierenOpgegevensDataNaarBeoordelingTask",
                "ZetAanvraagOntvangenDatumTask",
                "GenererenOntvangstbevestigingTask",
                "ZetDefaultCommunicatievoorkeurTask",
                "InformerenAanvragerCallActivity",
                "UpdateZaakstatusCallActivity",
                "BepaalZaakbehandelaarTask",
                "Gateway_0kdnrmv",
                "UpdateZaakstatusCallActivity1",
                "BeoordelenAfhandelenAanvraagFormflowCallActivity",
                "GenererenDocumentCallActivity",
                "OpvragenInformatieCallActivity",
                "BesluitGenomenGateway",
                "GenererenRapportageCallActivity",
                "BepalenBeschikkingTemplateDMNTask",
                "GenererenBeschikkingCallActivity",
                "VastleggenBesluitTask",
                "BepalenZaakresultaatDMNTask",
                "AanvraagToegekendGateway",
                "Gateway_1ohjxeg",
                "InformerenAanvragerVersturenBesluitCallActivity",
                "AfsluitenZaakCallActivity",
                "AanvraagAfgehandeldEndEvent"
            ).isEnded

        verify(ooievaarspasAanvraagMapper, times(1)).mapAanvragInDocumentByBusinessKey(any())
    }


    private fun runScenario(): Scenario {
        return Scenario.run(processScenario)
            .startByKey(
                "ooievaarspas-afhandelen-aanvraag",
                TestHelper.DOCUMENT_ID
            )
            .execute()
    }
}
