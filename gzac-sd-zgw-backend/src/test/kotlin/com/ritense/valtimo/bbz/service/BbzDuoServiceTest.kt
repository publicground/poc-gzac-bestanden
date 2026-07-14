package com.ritense.valtimo.bbz.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ritense.document.domain.Document
import com.ritense.valtimo.BaseTest
import com.ritense.valtimo.TestHelper
import com.ritense.valtimo.common.service.DocumentWriterService
import com.ritense.valtimoplugins.suwinet.model.DuoPersoonsInfoDto
import com.ritense.valtimoplugins.suwinet.model.DuoStudiefinancieringInfoDto
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.extension.mockito.delegate.DelegateExecutionFake
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Answers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.kotlin.*
import org.mockito.quality.Strictness

private const val PV_DUO_PERSOONS_INFO = "duoPersoonsInformatie"
private const val PV_DUO_STUDIEFINANCIERING = "duoStudiefinancieringInfo"
private const val DUO_NODE = "tmpDuo"
private const val DUO_PERSOONS_INFO = "persoonsInformatie"
private const val DUO_STUDIEFINANCIERING_INFO = "studiefinanciering"
private const val GEGEVEN_VANUIT_BRONNEN = "gegevensVanuitBronnen"


private const val PROCESS_INSTANCE_ID = "df08455f-71ee-4637-b736-81cbd4bd551c"


@MockitoSettings(strictness = Strictness.LENIENT)
internal class BbzDuoServiceTest : BaseTest() {

    @Mock
    lateinit var document: Document

    @Mock
    lateinit var documentWriterService: DocumentWriterService

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    lateinit var processEngine: ProcessEngine

    @InjectMocks
    lateinit var bbzDuoService: BbzDuoService

    lateinit var execution: DelegateExecution

    lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        objectMapper = jacksonObjectMapper()
        execution = delegateExecution()
        bbzDuoService = BbzDuoService(
            documentWriterService
        )
    }
    @Test
    fun `should save DUO studiefinanciering with one studiefinanciering dto to document`() {
        //given
        val bsn = "333333330"
        val duoStudiefinancieringInfoDto = DuoStudiefinancieringInfoDto(bsn,
            listOf(getStudiefinanciering()))

        // TODO check if documentWriterService is called
    }

    @Test
    fun `should save DUO studiefinanciering with two studiefinancieringen dto to document`() {
        //given
        val bsn = "333333330"
        val duoStudiefinancieringInfoDto = DuoStudiefinancieringInfoDto(
            bsn,
            listOf(getStudiefinanciering(), getStudiefinanciering())
        )
        execution.setVariable(PV_DUO_STUDIEFINANCIERING,duoStudiefinancieringInfoDto)

        //when
        bbzDuoService.storeDuoStudiefinancieringInfo(null, "/anypath", execution.businessKey)
        //then
        argumentCaptor<JsonNode>().apply {
            verify(documentWriterService, times(0)).writeValueToDocumentAtPath(any(), any(), any())
        }
    }

    private fun getStudiefinanciering() = DuoStudiefinancieringInfoDto.Studiefinanciering(
        "20211101",
    "20211231",
"1",
    "1",
    null,
    null,
    null
    )

    @Test
    fun `should save empty DUO studiefinanciering dto to document`() {
        //given
        val bsn = "333333330"
        val duoStudiefinancieringInfoDto = DuoStudiefinancieringInfoDto(bsn,
            listOf())

        // TODO check if documentWriterService is called
    }

    @Test
    fun `should map empty DUO PersoonsInfo dto to document`() {
        //given
        val bsn = "333333330"
        val duoPersoonsInfoDto = DuoPersoonsInfoDto(
            bsn,
            "",
            listOf<DuoPersoonsInfoDto.DuoOnderwijsOvereenkomst>(),
            listOf<DuoPersoonsInfoDto.ResultaatOpleidingGeregistrDuo>()
        )


        //when
        bbzDuoService.storeDuoPersonalInformation(null, "/anyPath", businessKey = execution.businessKey)
        //then
        argumentCaptor<JsonNode>().apply {
            verify(documentWriterService, times(0)).writeValueToDocumentAtPath(any(), any(), any())
        }
    }


    private fun delegateExecution(): DelegateExecution {
        return DelegateExecutionFake()
            .withBusinessKey(TestHelper.DOCUMENT_ID)
            .withProcessInstanceId(PROCESS_INSTANCE_ID)
            .withProcessEngine(processEngine)
    }
}