package com.ritense.valtimo.custom.processDocumentService.autoconfigure

import com.ritense.document.service.DocumentService
import com.ritense.valtimo.contract.annotation.ProcessBean
import com.ritense.valtimo.custom.processDocumentService.BrpProcessDocumentService
import com.ritense.valtimo.custom.processDocumentService.UwvProcessDocumentService
import com.ritense.valtimoplugins.suwinet.service.DateTimeService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ProcessDocumentAutoConfiguration {

    @Bean
    @ProcessBean
    fun brpProcessDocumentService(
        documentService: DocumentService,
        dateTimeService: DateTimeService,
        @Value("\${implementation.bbz.maxAgeKindAlsThuiswonend: }") maxAgeKindAlsThuiswonend: Int
    ): BrpProcessDocumentService {
        return BrpProcessDocumentService(
            documentService,
            dateTimeService,
            maxAgeKindAlsThuiswonend
        )
    }

    @Bean
    @ProcessBean
    fun uwvProcessDocumentService(): UwvProcessDocumentService {
        return UwvProcessDocumentService()
    }
}