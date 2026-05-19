package com.ritense.valtimo.ooievaarspas.autoconfigure

import com.ritense.document.service.DocumentService
import com.ritense.valtimo.common.service.DocumentReaderService
import com.ritense.valtimo.contract.annotation.ProcessBean
import com.ritense.valtimo.ooievaarspas.mapper.OoievaarspasAanvraagMapper
import com.ritense.valtimo.ooievaarspas.service.OoievaarspasCurrencyValuesMapperService
import com.ritense.valtimo.ooievaarspas.service.OoievaarspasOpgegevenDataToBeoordelingService
import com.ritense.valtimo.ooievaarspas.service.OoievaarspasProcessService
import com.ritense.valtimoplugins.suwinet.service.DateTimeService
import com.ritense.zakenapi.service.ZaakDocumentService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OoievaarspasAutoConfiguration {

    @Bean
    @ProcessBean
    fun ooievaarspasProcessService(
        documentService: DocumentService,
        zaakDocumentService: ZaakDocumentService,
        dateTimeService: DateTimeService,
        @Value("\${implementation.ooievaarspas.besluitType: }") besluitTypeOmschrijving: String
    ): OoievaarspasProcessService {
        return OoievaarspasProcessService(
            documentService,
            zaakDocumentService,
            dateTimeService,
            besluitTypeOmschrijving
        )
    }

    @Bean
    @ProcessBean
    fun ooievaarspasAanvraagMapper(
        documentService: DocumentService,
        documentReaderService: DocumentReaderService
    ): OoievaarspasAanvraagMapper {
        return OoievaarspasAanvraagMapper(documentService, documentReaderService)
    }

    @Bean
    @ProcessBean
    fun ooievaarspasOpgegevenDataToBeoordelingService(
        documentService: DocumentService
    ): OoievaarspasOpgegevenDataToBeoordelingService {
        return OoievaarspasOpgegevenDataToBeoordelingService(documentService)
    }

    @Bean
    @ProcessBean
    fun ooievaarspasCurrencyValuesMapperService(
        documentService: DocumentService
    ) : OoievaarspasCurrencyValuesMapperService {
        return OoievaarspasCurrencyValuesMapperService(documentService)
    }
}