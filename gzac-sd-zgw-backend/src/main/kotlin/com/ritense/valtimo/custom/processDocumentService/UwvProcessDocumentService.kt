package com.ritense.valtimo.custom.processDocumentService

import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ritense.valtimoplugins.suwinet.model.UwvPersoonsIkvDto

import mu.KotlinLogging

class UwvProcessDocumentService {

    fun mapUwvInfo(uwvInfo: Map<String, Any>?): Map<String, List<Any>> {
        if (uwvInfo == null) return emptyMap()

        val dto = try {
            objectMapper.convertValue<UwvPersoonsIkvDto>(uwvInfo)
        } catch (e: Exception) {
            logger.error(e) { "Processing the UWV information has failed." }
            return emptyMap()
        }

        fun normalize(str: String?): String = str.orEmpty().trim()

        val extraFields: Map<String, (UwvPersoonsIkvDto.Inkomsten) -> Any?> = mapOf(
            "loonheffingennummer" to { it.loonheffingennummer },
            "straatadres" to { it.straatadres },
            "cdSector" to { it.cdSector },
            "datumBeginIkv" to { it.datumBeginIkv },
            "datumEindIkv" to { it.datumEindIkv },
        )

        val extrasByWerkgever: Map<String, Map<String, Any?>> =
            dto.inkomsten
                .groupBy { normalize(it.naamRechtspersoon) }
                .mapValues { (_, rows) ->
                    val first = rows.firstOrNull()
                    extraFields.mapValues { (_, getter) -> first?.let(getter) }
                }

        val opgaven: List<Map<String, Any?>> =
            dto.inkomsten.flatMap { it.opgaven }
                .map { objectMapper.convertValue<Map<String, Any?>>(it) }

        fun typeOf(opgave: Map<String, Any?>): String =
            ((opgave["codeSoortIkv"] as? Map<*, *>)?.get("type") as? String).orEmpty().lowercase()

        fun werkgeverOf(opgave: Map<String, Any?>): String =
            normalize(opgave["naamRechtspersoon"] as? String)

        val typed: Map<String, List<Map<String, Any?>>> =
            opgaven
                .groupBy(::typeOf)
                .mapValues { (_, opsForType) ->
                    opsForType
                        .groupBy(::werkgeverOf)
                        .map { (werkgever, opsForEmployer) ->
                            val extras = extrasByWerkgever[werkgever].orEmpty()
                            mapOf(
                                "werkgever" to werkgever,
                                "opgaven" to opsForEmployer
                            ) + extras
                        }
                }

        return typed.mapValues { it.value as List<Any> }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
        private val objectMapper = jacksonObjectMapper().findAndRegisterModules()
    }
}