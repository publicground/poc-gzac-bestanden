package com.ritense.valtimo.bbz.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ritense.document.domain.Document
import com.ritense.document.domain.impl.JsonDocumentContent
import com.ritense.valtimo.BaseTest
import com.ritense.valtimo.TestHelper
import com.ritense.valtimo.common.service.DocumentWriterService
import com.ritense.valtimoplugins.suwinet.model.MotorvoertuigDto
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

private const val PROCESS_INSTANCE_ID = "df08455f-71ee-4637-b736-81cbd4bd551c"
private const val PV_VOERTUIGEN = "rdwVoertuigen"

@MockitoSettings(strictness = Strictness.LENIENT)
internal class BbzRwdServiceTest : BaseTest() {

    @Mock
    lateinit var document: Document

    @Mock
    lateinit var documentWriterService: DocumentWriterService

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    lateinit var processEngine: ProcessEngine

    @InjectMocks
    lateinit var bbzRdwVoertuigenService: BbzRdwVoertuigenService

    lateinit var execution: DelegateExecution

    lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        objectMapper = jacksonObjectMapper()
        execution = delegateExecution()
        bbzRdwVoertuigenService = BbzRdwVoertuigenService(
            documentWriterService
        )
    }

    @Test
    fun `should map voertuigen to document`() {
        //given
//        val motorvoertuigen = listOf(
//            getMotorvoertuig(),
//            getSecondSimpleMotorvoertuig()
//        )
//        loadDocumentConditionsWithContent(motorvoertuigen)
//        loadDocumentConditions()
//        execution.setVariable(PV_VOERTUIGEN,motorvoertuigen)
//
//        //when
//        bbzRdwVoertuigenService.storeVoertuigen(PV_VOERTUIGEN, execution)
//        //then
//        argumentCaptor<JsonNode>().apply {
//            verify(documentService, times(1)).modifyDocument(any(), capture())
//            Assertions.assertThat(firstValue).isEqualTo(
//                objectMapper.convertValue(
//                    vermogenVoertuigData(motorvoertuigen),
//                    JsonNode::class.java
//                )
//            )
//        }
    }

    @Test
    fun `should handle empty map voertuigen`() {
//        val emptyList = listOf<MotorvoertuigDto>()
        //       loadDocumentConditions()
        //given
        execution.setVariable(
            PV_VOERTUIGEN, listOf<String>()
        )
        //when
        bbzRdwVoertuigenService.storeVoertuigen(null, "/anyPath", execution.businessKey)
        //then
        argumentCaptor<JsonNode>().apply {
            verify(documentWriterService, times(0)).writeValueToDocumentAtPath(any(), any(), any())
        }
    }
//        argumentCaptor<JsonNode>().apply {
//            verify(documentService, times(1)).modifyDocument(any(), capture())
//            Assertions.assertThat(firstValue).isEqualTo(
//                objectMapper.convertValue(
//                    vermogenVoertuigData(emptyList),
//                    JsonNode::class.java
//                )
//            )
//        }

//    private fun loadDocumentConditions() {
//        whenever(documentService.get(any())).thenReturn(document)
//    }

    private fun vermogenVoertuigData(motorvoertuigen: List<MotorvoertuigDto>): LinkedHashMap<String, Any> {
        return linkedMapOf(
            "gegevensVanuitBronnen" to voertuigenObjectData(motorvoertuigen)
        )
    }

    private fun voertuigenObjectData(motorvoertuigen: List<MotorvoertuigDto>): LinkedHashMap<String, Any> {

        return linkedMapOf(
            "vermogen" to linkedMapOf(
                "motorVoertuigen" to motorvoertuigen
            )
        )
    }

    private fun documentAsJson(motorvoertuigen: List<MotorvoertuigDto>): JsonDocumentContent {
        val objectMapper = ObjectMapper()
        val vermogenJson = objectMapper.convertValue(vermogenVoertuigData(motorvoertuigen), JsonNode::class.java)

        return JsonDocumentContent.build(vermogenJson)
    }


//    @Test
//    fun `should map voertuig info`() {
//        //given
//        var voertuigInfo: String = ClassPathResource(
//            "testfiles/16ZDLX.json"
//        ).file.readText()
//        val voertuigInfoMap: Map<Any, Any> = this.deserialiseJsonAsMap(voertuigInfo)
//        execution = delegateExecution()
//        execution.setVariable(PV_VOERTUIGEN, voertuigInfoMap)
//        loadDocumentConditions()
//        //when
//        bbzRdwVoertuigenService.mapVoertuigen(execution)
//
//        //then
//    }

    private fun delegateExecution(): DelegateExecution {
        return DelegateExecutionFake()
            .withBusinessKey(TestHelper.DOCUMENT_ID)
            .withProcessInstanceId(PROCESS_INSTANCE_ID)
            .withProcessEngine(processEngine)
    }

    private fun getSecondSimpleMotorvoertuig(): MotorvoertuigDto.Motorvoertuig {
        val soortVoertuigNode = objectMapper.createObjectNode()
        soortVoertuigNode.put("name", "SUV")
        soortVoertuigNode.put("code", "H")
        return MotorvoertuigDto.Motorvoertuig(
            "BB22CC",
            soortVoertuigNode,
            "Hummer",
            "H1",
            "20221022",
            "20230911"
        )
    }

    private fun getMotorvoertuig(): MotorvoertuigDto.Motorvoertuig {
        val soortVoertuigNode = objectMapper.createObjectNode()
        soortVoertuigNode.put("name", "Driewieler")
        soortVoertuigNode.put("code", "D")
        return MotorvoertuigDto.Motorvoertuig(
            "AA11BB",
            soortVoertuigNode,
            "Opel",
            "Record",
            "20221022",
            "20230911"
        )
    }

    @Throws(JsonProcessingException::class, JsonMappingException::class)
    inline fun <reified K, V> deserialiseJsonAsMap(jsonData: String?): Map<K, V> {
        val typeRef: TypeReference<Map<K, V>> = object : TypeReference<Map<K, V>>() {}
        val objectMapper = jacksonObjectMapper()
        return objectMapper.readValue(jsonData, typeRef)
    }
}

//var voertuigbezitInfo: String = ClassPathResource(
//    "testfiles/VoertuigbezitInfoPersoon_111111110.json"
//).file.readText()
//val voertuigbezitInfoPersoonMap: Map<Any, Any> = this.deserialiseJsonAsMap(voertuigbezitInfo)
//execution = delegateExecution()
//execution.setVariable(PV_VOERTUIGBEZIT_INFO, voertuigbezitInfoPersoonMap)
