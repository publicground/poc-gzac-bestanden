package com.ritense.valtimo.bbz.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ritense.document.domain.Document
import com.ritense.valtimo.BaseTest
import com.ritense.valtimo.TestHelper
import com.ritense.valtimo.common.service.DocumentWriterService
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

private const val PV_KADASTER_OBJECTS = "kadastraleObjecten"
private const val PROCESS_INSTANCE_ID = "df08455f-71ee-4637-b736-81cbd4bd551c"

@MockitoSettings(strictness = Strictness.LENIENT)
internal class BbzKadasterServiceTest : BaseTest() {

    @Mock
    lateinit var document: Document

    @Mock
    lateinit var documentWriterService: DocumentWriterService

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    lateinit var processEngine: ProcessEngine

    @InjectMocks
    lateinit var bbzKadasterProcessService: BbzKadasterService

    lateinit var execution: DelegateExecution

    lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        objectMapper = jacksonObjectMapper()
        execution = delegateExecution()
        bbzKadasterProcessService = BbzKadasterService(
            documentWriterService
        )
    }

    @Test
    fun `should map empty kadaster dto list to document`() {
        //given

    //    val kadastralebjecten = mapOf( "dto" to listOf<KadastraleObjectenDto>())
        loadDocumentConditions()
//        execution.setVariable(PV_KADASTER_OBJECTS,kadastralebjecten)
//
//        //when
//        bbzKadasterProcessService.storeKadastraleObjecten(kadastralebjecten, "/anyPath", execution.businessKey)
        //then
        argumentCaptor<JsonNode>().apply {
            verify(documentWriterService, times(1)).writeValueToDocumentAtPath(any(), any(), any())
//            Assertions.assertThat(firstValue).isEqualTo(
//                objectMapper.convertValue(
//                    gegevensVanuitBronnen(kadastralebjecten),
//                    JsonNode::class.java
//                )
//            )
        }
    }

//    private fun gegevensVanuitBronnen(kadastralebjecten: List<KadastraleObjectenDto>): LinkedHashMap<String, Any> {
//        return linkedMapOf(
//            "gegevensVanuitBronnen" to vermogen(kadastralebjecten)
//        )
//    }
//
//    private fun vermogen(kadastralebjecten: List<KadastraleObjectenDto>): LinkedHashMap<String, Any> {
//        return linkedMapOf(
//            "vermogen" to onroerendeGoederen(kadastralebjecten)
//        )
//    }
//
//    private fun onroerendeGoederen(kadastralebjecten: List<KadastraleObjectenDto>): LinkedHashMap<String, Any> {
//        return linkedMapOf(
//            "onroerendeGoederen" to kadastralebjecten
//        )
//    }

    private fun loadDocumentConditions() {
//        whenever(documentService.get(any())).thenReturn(document)
    }

    private fun delegateExecution(): DelegateExecution {
        return DelegateExecutionFake()
            .withBusinessKey(TestHelper.DOCUMENT_ID)
            .withProcessInstanceId(PROCESS_INSTANCE_ID)
            .withProcessEngine(processEngine)
    }

}