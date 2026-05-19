package com.ritense.valtimo.custom.processDocumentService

import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ritense.document.service.DocumentService
import com.ritense.valtimoplugins.suwinet.model.brp.PersoonDto
import com.ritense.valtimoplugins.suwinet.service.DateTimeService
import mu.KotlinLogging
import java.time.temporal.ChronoField

@Suppress("UNUSED")
class BrpProcessDocumentService(
    private val documentService: DocumentService,
    private val dateTimeService: DateTimeService,
    private val maxAgeKindAlsThuiswonend: Int
) {

    fun getFilteredKinderen(
        brpPersoonsgegevensKinderenInfo: List<Map<String, Any>>?,
        brpPersoonsgegevensInfo: Map<String, Any>?,
        businessKey: String
    ): List<Map<String, Any>> {

        return try {

            if (!brpPersoonsgegevensKinderenInfo.isNullOrEmpty() && brpPersoonsgegevensInfo != null) {
                val persoon: PersoonDto = objectMapper.convertValue(brpPersoonsgegevensInfo)

                brpPersoonsgegevensKinderenInfo
                    .map { objectMapper.convertValue<PersoonDto>(it) }
                    .filter { kind ->
                        kind.adresBrp == persoon.adresBrp &&
                            kind.datumOverlijden.isNullOrEmpty() &&
                            isNotToOld(
                                kind.geboortedatum,
                                getCaseCreationDate(businessKey)
                            )
                    }
                    .map { kind -> objectMapper.convertValue<Map<String, Any>>(kind) }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            logger.error("Filtering children is failed", e)
            emptyList()
        }
    }

    private fun isNotToOld(geboortedatum: String, yearOfCaseCreation: Int) =
        yearOfCaseCreation - dateTimeService.getYearFromDateString(
            geboortedatum,
            SUWINET_DATEIN_PATTERN
        ) < maxAgeKindAlsThuiswonend

    private fun getCaseCreationDate(businessKey: String): Int {
        val document = documentService.get(businessKey)
        return document.createdOn().get(ChronoField.YEAR)
    }

    companion object {
        private val logger = KotlinLogging.logger { }
        private val objectMapper = jacksonObjectMapper().findAndRegisterModules()
        private const val SUWINET_DATEIN_PATTERN = "yyyy-MM-dd"
    }
}