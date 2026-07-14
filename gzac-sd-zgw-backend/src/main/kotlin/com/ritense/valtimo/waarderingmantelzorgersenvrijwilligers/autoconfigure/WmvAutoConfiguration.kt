package com.ritense.valtimo.waarderingmantelzorgersenvrijwilligers.autoconfigure

import com.ritense.document.service.DocumentSearchService
import com.ritense.document.service.DocumentService
import com.ritense.objectmanagement.service.ObjectManagementService
import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.common.service.DocumentReaderService
import com.ritense.valtimo.common.service.MessageCorrelationService
import com.ritense.valtimo.contract.annotation.ProcessBean
import com.ritense.valtimo.waarderingmantelzorgersenvrijwilligers.service.WmvNotificationReceivedEventService
import com.ritense.valtimo.waarderingmantelzorgersenvrijwilligers.service.WmvSearchService
import com.ritense.valtimo.waarderingmantelzorgersenvrijwilligers.service.WmvWaarderingsobjectService
import com.ritense.valtimo.waarderingmantelzorgersenvrijwilligers.service.WmvZakenApiService
import com.ritense.valueresolver.ValueResolverService
import com.ritense.zakenapi.ZaakUrlProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WmvAutoConfiguration {

    @Bean
    @ProcessBean
    fun wmvWaarderingsobjectService(
        documentService: DocumentService
    ): WmvWaarderingsobjectService {
        return WmvWaarderingsobjectService(
            documentService
        )
    }

    @Bean
    fun wmvNotificationReceivedEventService(
        messageCorrelationService: MessageCorrelationService,
        objectManagementService: ObjectManagementService,
        pluginService: PluginService,
    ): WmvNotificationReceivedEventService {
        return WmvNotificationReceivedEventService(
            messageCorrelationService,
            objectManagementService,
            pluginService
        )
    }

    @Bean
    @ProcessBean
    fun wmvZakenApiService(
        pluginService: PluginService,
        zaakUrlProvider: ZaakUrlProvider,
        webclientBuilder: WebClient.Builder
    ): WmvZakenApiService {
        return WmvZakenApiService(pluginService, zaakUrlProvider, webclientBuilder)
    }

    @Bean
    @ProcessBean
    fun wmvSearchService(
        documentSearchService: DocumentSearchService,
        documentReaderService: DocumentReaderService,
        valueResolverService: ValueResolverService
    ): WmvSearchService {
        return WmvSearchService(
            documentSearchService,
            documentReaderService,
            valueResolverService
        )
    }
}