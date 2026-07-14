package com.ritense.valtimo.bbz.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ritense.document.domain.Document
import com.ritense.valtimo.BaseTest
import com.ritense.valtimo.TestHelper
import com.ritense.valtimo.common.service.DocumentWriterService
import com.ritense.valtimoplugins.suwinet.model.InkomstenType
import com.ritense.valtimoplugins.suwinet.model.UwvPersoonsIkvDto
import mu.KotlinLogging
import org.assertj.core.api.Assertions
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.extension.mockito.delegate.DelegateExecutionFake
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Answers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.quality.Strictness

private const val PROCESS_INSTANCE_ID = "df08455f-71ee-4637-b736-81cbd4bd551c"

@MockitoSettings(strictness = Strictness.LENIENT)
internal class BbzUwvProcessServiceTest : BaseTest() {

    private val logger = KotlinLogging.logger { }

    @Mock
    lateinit var document: Document

    @Mock
    lateinit var documentWriterService: DocumentWriterService

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    lateinit var processEngine: ProcessEngine

    @InjectMocks
    lateinit var bbzUwvProcessService: BbzUwvProcessService

    lateinit var execution: DelegateExecution

    lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        objectMapper = jacksonObjectMapper()
        execution = delegateExecution()
        bbzUwvProcessService = BbzUwvProcessService(
            documentWriterService
        )
    }

    @Test
    fun test2() {
        val inkomstenTypes =
            listOf(InkomstenType.LOONDIENST, InkomstenType.PENSIOEN, InkomstenType.UITKERING, InkomstenType.BUITENLAND)
        createUwvPersoonsIkvDto("ikv_777777770.json").let { result ->
            inkomstenTypes.map { types ->
                result.inkomsten.flatMap { inkomsten ->
                    inkomsten.opgaven.filter { it.codeSoortIkv.type == types }
                }.groupBy { it.naamRechtspersoon }.map {
                    println("${it.key} =/= opgaves = ${it.value[0].codeSoortIkv.type}")
                }
            }
        }
    }

    @Test
    fun test3() {
        createMap("ikv_777777770.json").let {
            bbzUwvProcessService.mapUwvInfo(it, "/anypath/uwv", execution.businessKey)
        }
    }

    @Test
    fun test4() {
        val inkomstenTypes = listOf(InkomstenType.LOONDIENST, InkomstenType.PENSIOEN, InkomstenType.UITKERING)

        createUwvPersoonsIkvDto("ikv_777777770.json").let { dto ->
            inkomstenTypes.map { type ->
                dto.inkomsten.flatMap { inkomsten ->
                    inkomsten.opgaven.filter { it.codeSoortIkv.type == type }
                }.groupBy { it.naamRechtspersoon }.map {
                    println("${it.key} =/= opgaves = $type size: ${it.value.size}")
                }
            }
        }
    }

    @Test
    fun test5() {

        createUwvPersoonsIkvDto("ikv_777777770.json").let { dto ->

            val uwvOut = mutableMapOf<String, List<Any>>()
            dto?.inkomsten?.flatMap { inkomsten ->
                inkomsten.opgaven
            }?.groupBy { it.codeSoortIkv.type }?.map { opgaven ->
                uwvOut.put(
                    opgaven.key.fieldName,
                    opgaven.value.groupBy { it.naamRechtspersoon }.map {
                        return@map object {
                            val werkgever = it.key
                            val opgaven = it.value
                        }
                    })
            }
            val mapper = jacksonObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
            val json = mapper.valueToTree<JsonNode>(uwvOut)
            val jout = mapper.writeValueAsString(json)
            logger.info { "----- ${jout}" }

        }

    }

    @Test
    fun test6() {

        createUwvPersoonsIkvDto("ikv_777777770.json")?.let { dto ->
            val eee = dto.inkomsten.flatMap { inkomsten ->
                inkomsten.opgaven
            }.groupBy { it.codeSoortIkv.type }.map { opgave ->
                return@map object {
                    val opgavenMap = opgave.value.groupBy { it.naamRechtspersoon }
                    val type = opgave.key
                }
            }.associate {
                it.type to it.opgavenMap.map { werkgever ->
                    return@map object {
                        val werkgever = werkgever.key
                        val opgaven = werkgever.value
                    }
                }
            }
            val mapper = jacksonObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
            val json = mapper.valueToTree<JsonNode>(eee)
            val jout = mapper.writeValueAsString(json)
            logger.info { "----- ${jout}" }
        }
    }

    @Test
    fun test1() {
        //given
        val persoonMap = createMap("ikv_111111110.json")
        //when
        bbzUwvProcessService.mapUwvInfo(
            persoonMap, "/anypath", execution.businessKey
        )

        persoonMap.keys.map {
            logger.info { "persoon map key: ${it}" }
        }

        //then
        argumentCaptor<Map<String, Any>>().apply {
            verify(documentWriterService, times(1)).writeValueToDocumentAtPath(capture(), any(), any())

            Assertions.assertThat(firstValue).isNotNull
        }
    }

    private fun createMap(inputFile: String): Map<String, Any> {
        val resource = TestHelper.resourceLoader.getResource("classpath:suwinet/data/dtos/$inputFile")
        return objectMapper.readValue(resource.inputStream)
    }


    private fun createUwvPersoonsIkvDto(inputFile: String): UwvPersoonsIkvDto {
        val resource = TestHelper.resourceLoader.getResource("classpath:suwinet/data/dtos/$inputFile")
        return objectMapper.readValue(resource.inputStream)
    }

    private fun delegateExecution(): DelegateExecution {

        return DelegateExecutionFake()

            .withBusinessKey(TestHelper.DOCUMENT_ID).withProcessInstanceId(PROCESS_INSTANCE_ID)
            .withProcessEngine(processEngine)
    }
}