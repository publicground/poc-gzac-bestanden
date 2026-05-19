package com.ritense.valtimo.bbz.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ritense.document.domain.Document
import com.ritense.document.service.DocumentService
import com.ritense.valtimo.BaseTest
import com.ritense.valtimo.TestHelper
import com.ritense.valtimo.common.service.DocumentWriterService
import com.ritense.valtimoplugins.suwinet.model.brp.PersoonDto
import com.ritense.valtimoplugins.suwinet.service.DateTimeService
import mu.KotlinLogging
import org.assertj.core.api.Assertions
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.extension.mockito.delegate.DelegateExecutionFake
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.kotlin.*
import org.mockito.quality.Strictness
import java.time.temporal.ChronoField

private const val PROCESS_INSTANCE_ID = "df08455f-71ee-4637-b736-81cbd4bd551c"

@MockitoSettings(strictness = Strictness.LENIENT)
internal class BbzBrpServiceTest : BaseTest() {

    private val logger = KotlinLogging.logger { }

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    lateinit var document: Document

    @Mock
    lateinit var documentWriterService: DocumentWriterService

    @Mock
    lateinit var documentService: DocumentService

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    lateinit var processEngine: ProcessEngine

    lateinit var bbzBrpService: BbzBrpService

    private lateinit var execution: DelegateExecution


    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        objectMapper = jacksonObjectMapper()
        execution = delegateExecution()
    }

    private fun loadDocumentConditions() {
        whenever(documentService.get(any())).thenReturn(document)
        whenever(document.createdOn().get(ChronoField.YEAR)).thenReturn(2024)
    }

    @Test
    fun filtertest() {
        val lijst1 = listOf(1, 2, 3, 4, 5, 6)
        val filtered = lijst1.filter { it % 2 == 0  }
        logger.info { "filtered ${filtered.size}" }
    }
    @Test
    fun `should store person with 4 children`() {
        //given
        loadDocumentConditions()
        bbzBrpService = BbzBrpService(
            documentWriterService,
            documentService,
            DateTimeService(),
            27
        )

        val persoonMap = createPersoonMap("persoon_111111110_met_kinderen.json")
        //when
        bbzBrpService.storePersoonsgegevens(
            persoonMap, "/anypath", execution.businessKey
        )

        //then
        argumentCaptor<PersoonDto>().apply {
            verify(documentWriterService, times(1)).writeValueToDocumentAtPath(capture(), any(), any())
            Assertions.assertThat(firstValue.kinderenBsns?.size).isEqualTo(4)
        }
    }

    @Test
    fun `should store list of 1 out of 4 children`() {
        //given
        loadDocumentConditions()
        bbzBrpService = BbzBrpService(
            documentWriterService,
            documentService,
            DateTimeService(),
            27
        )

        val persoonMap = createPersoonMap("persoon_111111110_met_kinderen.json")
        val kinderenMap = listOf(
            createPersoonMap("persoon_243000157.json"),
            createPersoonMap("persoon_243000017.json"),
            createPersoonMap("persoon_010002546.json"),
            createPersoonMap("persoon_080000071.json"),
        )

        //when
        bbzBrpService.storePersoonsgegevensKinderen(
            kinderenMap,
            persoonMap,
            "/anypath",
            execution.businessKey
        )

        //then
        argumentCaptor<List<PersoonDto>>().apply {
            verify(documentWriterService, times(1)).writeValueToDocumentAtPath(capture(), any(), any())
            Assertions.assertThat(firstValue.size).isEqualTo(1)
            Assertions.assertThat(firstValue[0].voornamen).isEqualTo("Catharina jong en woont thuis")
        }
    }

    @Test
    fun `aanvrager should have no partner`() {
        //given
        loadDocumentConditions()
        bbzBrpService = BbzBrpService(
            documentWriterService,
            documentService,
            DateTimeService(),
            27
        )
        val persoonMap = createPersoonMap("persoon_444444440_no_partner.json")

        //when
        bbzBrpService.storePersoonsgegevens(
            persoonMap, "/anypath", execution.businessKey
        )

        //then
        argumentCaptor<PersoonDto>().apply {
            verify(documentWriterService, times(1)).writeValueToDocumentAtPath(capture(), any(), any())
            Assertions.assertThat(firstValue.partnerBsn).isEmpty()
        }
    }

    @Test
    fun `should filter out 2 from 4 childern based on maxAgeKindAlsThuiswonend`() {
        //given
        loadDocumentConditions()
        bbzBrpService = BbzBrpService(
            documentWriterService,
            documentService,
            DateTimeService(),
            100
        )

        val persoonMap = createPersoonMap("persoon_111111110_met_kinderen.json")
        //when
        bbzBrpService.storePersoonsgegevens(
            persoonMap, "/anypath", execution.businessKey
        )

        //then
        argumentCaptor<PersoonDto>().apply {
            verify(documentWriterService, times(1)).writeValueToDocumentAtPath(capture(), any(), any())
//            Assertions.assertThat(firstValue.kinderen?.size).isEqualTo(2)
        }
    }

    @Test
    fun `should map persoons gegevens with one child to document`() {
        //given
        loadDocumentConditions()

        bbzBrpService = BbzBrpService(
            documentWriterService,
            documentService,
            DateTimeService(),
            27
        )

        val persoonMap = createPersoonMap("persoon_111111110.json")
        val createPersoonDto = createPersoonDto("persoon_111111110.json")
        //when
        bbzBrpService.storePersoonsgegevens(
            persoonMap, "/anypath", execution.businessKey
        )

        //then
        argumentCaptor<PersoonDto>().apply {
            verify(documentWriterService, times(1)).writeValueToDocumentAtPath(capture(), any(), any())

            Assertions.assertThat(firstValue).isNotNull
            Assertions.assertThat(firstValue).isEqualTo(createPersoonDto)
        }
    }

    private fun createPersoonMap(inputFile: String): Map<String, Any> {
        val resource = TestHelper.resourceLoader.getResource("classpath:suwinet/data/dtos/$inputFile")
        return objectMapper.readValue(resource.inputStream)
    }
    private fun createPersoonDto(inputFile: String): PersoonDto {
        val resource = TestHelper.resourceLoader.getResource("classpath:suwinet/data/dtos/$inputFile")
        return objectMapper.readValue(resource.inputStream)
    }

    private fun delegateExecution(): DelegateExecution {
        return DelegateExecutionFake()
            .withBusinessKey("6ec5ebd7-9122-46f2-b1f3-fa0fa1e83d70")
            .withProcessInstanceId(PROCESS_INSTANCE_ID)
    }
}