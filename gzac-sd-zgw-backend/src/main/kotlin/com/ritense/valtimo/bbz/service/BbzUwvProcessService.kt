package com.ritense.valtimo.bbz.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ritense.valtimo.bbz.exception.BbzParseToDtoException
import com.ritense.valtimo.common.service.DocumentWriterService
import com.ritense.valtimoplugins.suwinet.model.UwvPersoonsIkvDto
import mu.KotlinLogging

class BbzUwvProcessService(
    private val documentWriterService: DocumentWriterService
) {


    /**
     * Store UWV inkomsten gegevens from Suwinet call UWVPersoonsIkvInfo
     */
    fun mapUwvInfo(uwvInfo: Map<String, Any>?, targetPath: String, businessKey: String) {

        try {
            uwvInfo
                ?.let { uwvInput ->
                    jacksonObjectMapper().convertValue(uwvInput, UwvPersoonsIkvDto::class.java)?.let { dto ->

                        val ikvMap = dto.inkomsten.flatMap { inkomsten ->
                            inkomsten.opgaven
                        }.groupBy { it.codeSoortIkv.type }.map { opgaven ->
                            return@map object {
                                val opgavenMap = opgaven.value.groupBy { it.naamRechtspersoon }
                                val type = opgaven.key.fieldName
                            }
                        }.associate {
                            it.type to it.opgavenMap.map { werkgever ->
                                return@map object {
                                    val werkgever = werkgever.key
                                    val opgaven = werkgever.value
                                }
                            }
                        }

                        documentWriterService.writeValueToDocumentAtPath(ikvMap, targetPath, businessKey)
                    } ?: throw BbzParseToDtoException("Unable to convert inkomsten verhoudingen map to Dto")
                } ?: logger.debug { "No UWV Inkomsten Verhoudingen found for document with ID $businessKey" }
        } catch (e: Exception) {
            logger.error("Exiting scope due to nested error.", e)
            return
        }

    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}