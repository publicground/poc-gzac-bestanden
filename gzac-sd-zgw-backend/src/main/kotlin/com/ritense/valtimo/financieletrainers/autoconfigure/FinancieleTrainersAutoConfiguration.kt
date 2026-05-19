package com.ritense.valtimo.financieletrainers.autoconfigure

import com.ritense.document.service.DocumentService
import com.ritense.valtimo.common.service.MessageCorrelationService
import com.ritense.valtimo.contract.annotation.ProcessBean
import com.ritense.valtimo.financieletrainers.service.DeelnameCaseService
import com.ritense.valtimo.financieletrainers.service.TimerService
import com.ritense.valtimo.financieletrainers.service.TrainingDataToDocumentService
import com.ritense.valtimo.financieletrainers.service.TrainingProcessService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FinancieleTrainersAutoConfiguration {

    @Bean
    @ProcessBean
    fun trainingProcessService(
        documentService: DocumentService
    ): TrainingProcessService {
        return TrainingProcessService(
            documentService
        )
    }

    @Bean
    @ProcessBean
    fun trainingDataToDocumentService(
        documentService: DocumentService
    ): TrainingDataToDocumentService {
        return TrainingDataToDocumentService(
            documentService
        )
    }

    @Bean
    @ProcessBean
    fun deelnameCaseService(
        commonDocumentService: DocumentService
    ): DeelnameCaseService {
        return DeelnameCaseService(
            commonDocumentService
        )
    }

    @Bean
    @ProcessBean
    fun timerService(
        documentService: DocumentService,
        messageCorrelationService: MessageCorrelationService
    ): TimerService {
        return TimerService(
            documentService,
            messageCorrelationService
        )
    }
}
