package com.ritense.valtimo.bbz.autoconfigure

import com.ritense.document.service.DocumentService
import com.ritense.formflow.repository.FormFlowStepInstanceRepository
import com.ritense.valtimo.bbz.service.BbzBrpService
import com.ritense.valtimo.bbz.service.BbzDuoService
import com.ritense.valtimo.bbz.service.BbzFormFlowService
import com.ritense.valtimo.bbz.service.BbzKadasterService
import com.ritense.valtimo.bbz.service.BbzRdwVoertuigenService
import com.ritense.valtimo.bbz.service.BbzSvbService
import com.ritense.valtimo.bbz.service.BbzTaskService
import com.ritense.valtimo.bbz.service.BbzUwvProcessService
import com.ritense.valtimo.common.service.CustomPortaalTaakService
import com.ritense.valtimo.common.service.DocumentWriterService
import com.ritense.valtimo.contract.annotation.ProcessBean
import com.ritense.valtimoplugins.suwinet.service.DateTimeService
import com.ritense.valueresolver.ValueResolverService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BbzAutoConfiguration {

    @Bean
    @ProcessBean
    fun bbzDuoProcessService(
        documentWriterService: DocumentWriterService
    ): BbzDuoService {
        return BbzDuoService(documentWriterService)
    }

    @Bean
    @ProcessBean
    fun uwvProcessService(
        documentWriterService: DocumentWriterService
    ): BbzUwvProcessService {
        return BbzUwvProcessService(documentWriterService)
    }

    @Bean
    @ProcessBean
    fun bbzBrpService(
        documentWriterService: DocumentWriterService,
        documentService: DocumentService,
        @Value("\${implementation.bbz.maxAgeKindAlsThuiswonend: }") maxAgeKindAlsThuiswonend: Int
    ): BbzBrpService {
        return BbzBrpService(
            documentWriterService,
            documentService,
            DateTimeService(),
            maxAgeKindAlsThuiswonend
        )
    }

    @Bean
    @ProcessBean
    fun bbzSvbService(
        documentWriterService: DocumentWriterService
    ): BbzSvbService {
        return BbzSvbService(documentWriterService)
    }

    @Bean
    @ProcessBean
    fun bbzRdwVoertuigenService(
        documentWriterService: DocumentWriterService
    ): BbzRdwVoertuigenService {
        return BbzRdwVoertuigenService(documentWriterService)
    }

    @Bean
    @ProcessBean
    fun bbzKadasterProcessService(
        documentWriterService: DocumentWriterService
    ): BbzKadasterService {
        return BbzKadasterService(documentWriterService)
    }

    @Bean
    @ProcessBean
    fun bbzTaskService(
        documentService: DocumentService,
        customPortaalTaakService: CustomPortaalTaakService
    ): BbzTaskService {
        return BbzTaskService(documentService, customPortaalTaakService)
    }

    @Bean
    fun bbzFormFlowService(
        valueResolverService: ValueResolverService,
        formFlowStepInstanceRepository: FormFlowStepInstanceRepository
    ): BbzFormFlowService {
        return BbzFormFlowService(valueResolverService, formFlowStepInstanceRepository)
    }

}
