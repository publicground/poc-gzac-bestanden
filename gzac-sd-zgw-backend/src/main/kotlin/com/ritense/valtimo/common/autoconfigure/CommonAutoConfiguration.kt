package com.ritense.valtimo.common.autoconfigure

import com.fasterxml.jackson.databind.ObjectMapper
import com.ritense.document.service.DocumentService
import com.ritense.documentenapi.service.DocumentenApiService
import com.ritense.mail.service.MailService
import com.ritense.objectmanagement.repository.ObjectManagementRepository
import com.ritense.objectmanagement.service.ObjectManagementFacade
import com.ritense.openzaak.listener.EigenschappenSubmittedListener
import com.ritense.openzaak.service.DocumentenService
import com.ritense.openzaak.service.ZaakResultaatService
import com.ritense.openzaak.service.ZaakService
import com.ritense.openzaak.service.ZaakStatusService
import com.ritense.openzaak.service.impl.OpenZaakConfigService
import com.ritense.openzaak.service.impl.OpenZaakTokenGeneratorService
import com.ritense.plugin.service.PluginService
import com.ritense.processdocument.service.CorrelationService
import com.ritense.processdocument.service.ProcessDocumentAssociationService
import com.ritense.processdocument.service.impl.CamundaProcessJsonSchemaDocumentAssociationService
import com.ritense.resource.repository.OpenZaakResourceRepository
import com.ritense.resource.service.OpenZaakResourceProvider
import com.ritense.resource.service.OpenZaakService
import com.ritense.resource.service.TemporaryResourceStorageService
import com.ritense.valtimo.common.client.CustomZakenApiClient
import com.ritense.valtimo.common.client.OpenzaakClient
import com.ritense.valtimo.common.security.ActuatorSecurityConfigurer
import com.ritense.valtimo.common.security.CustomUserHttpSecurityConfigurer
import com.ritense.valtimo.common.service.ActiveProcessCheckService
import com.ritense.valtimo.common.service.CustomDocumentService
import com.ritense.valtimo.common.service.CustomFormFlowService
import com.ritense.valtimo.common.service.CustomInformatieobjectenService
import com.ritense.valtimo.common.service.CustomMailService
import com.ritense.valtimo.common.service.CustomObjectenApiService
import com.ritense.valtimo.common.service.CustomPortaalTaakService
import com.ritense.valtimo.common.service.CustomUserRoleService
import com.ritense.valtimo.common.service.CustomZaakInformatieobjectenService
import com.ritense.valtimo.common.service.CustomZaakObjectService
import com.ritense.valtimo.common.service.CustomZakenApiService
import com.ritense.valtimo.common.service.DocumentProcessService
import com.ritense.valtimo.common.service.DocumentReaderService
import com.ritense.valtimo.common.service.DocumentVariableService
import com.ritense.valtimo.common.service.DocumentWriterService
import com.ritense.valtimo.common.service.JobService
import com.ritense.valtimo.common.service.MessageCorrelationService
import com.ritense.valtimo.common.service.ProcessInstanceService
import com.ritense.valtimo.common.service.ZaakEigenschappenService
import com.ritense.valtimo.common.service.ZaakHandleService
import com.ritense.valtimo.common.service.ZaakPropertiesService
import com.ritense.valtimo.contract.annotation.ProcessBean
import com.ritense.valtimo.contract.authentication.CurrentUserService
import com.ritense.valtimo.contract.json.MapperSingleton
import com.ritense.valtimo.contract.mail.MailSender
import com.ritense.valtimo.contract.security.config.HttpSecurityConfigurer
import com.ritense.valtimo.custom.objectmanagement.web.rest.CustomObjectManagementResource
import com.ritense.valtimo.listener.CustomPortaalTaakListener
import com.ritense.valueresolver.ValueResolverService
import com.ritense.zakenapi.ResourceProvider
import com.ritense.zakenapi.ZaakUrlProvider
import com.ritense.zakenapi.link.ZaakInstanceLinkService
import com.ritense.zakenapi.service.ZaakDocumentService
import com.valtimo.keycloak.service.KeycloakUserManagementService
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import io.netty.resolver.DefaultAddressResolverGroup
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.TaskService
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.annotation.Order
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.client.RestClient
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.Connection
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.time.Duration

@Configuration
class CommonAutoConfiguration {


    @Bean
    @ProcessBean
    fun customUserRoleService(
        currentUserService: CurrentUserService,
        keycloakService: KeycloakUserManagementService
    ): CustomUserRoleService {
        return CustomUserRoleService(
            currentUserService,
            keycloakService
        )
    }

    @Bean
    @Order(10)
    @ConditionalOnMissingBean(ActuatorSecurityConfigurer::class)
    fun actuatorSecurityConfigurer(): ActuatorSecurityConfigurer {
        return ActuatorSecurityConfigurer()
    }

    @Bean
    @ProcessBean
    fun messageCorrelationService(
        runtimeService: RuntimeService,
        documentService: DocumentService,
        associationService: ProcessDocumentAssociationService
    ): MessageCorrelationService {
        return MessageCorrelationService(
            runtimeService,
            documentService,
            associationService
        )
    }


    @Bean
    @ProcessBean
    fun activeProcessCheckService(
        runtimeService: RuntimeService
    ): ActiveProcessCheckService {
        return ActiveProcessCheckService(
            runtimeService
        )
    }

    @Bean
    @ProcessBean
    fun zaakPropertiesService(
        documentService: DocumentService,
        zaakDocumentService: ZaakDocumentService,
        pluginService: PluginService
    ): ZaakPropertiesService {
        return ZaakPropertiesService(
            documentService,
            zaakDocumentService,
            pluginService
        )
    }

    @Bean
    @ProcessBean
    fun openzaakClient(
        openZaakConfigService: OpenZaakConfigService,
        openZaakTokenGeneratorService: OpenZaakTokenGeneratorService
    ): OpenzaakClient {
        return OpenzaakClient(openZaakConfigService, openZaakTokenGeneratorService)
    }

    @Bean
    @ProcessBean
    fun zaakHandleService(
        zaakInstanceLinkService: ZaakInstanceLinkService,
        openzaakClient: OpenzaakClient,
        zaakService: ZaakService,
        zaakStatusService: ZaakStatusService,
        zaakResultaatService: ZaakResultaatService,
        documentService: DocumentService
    ): ZaakHandleService {
        return ZaakHandleService(
            zaakInstanceLinkService,
            openzaakClient,
            zaakService,
            zaakStatusService,
            documentService,
            zaakResultaatService
        )
    }

    @Bean
    @ProcessBean
    fun documentWriterService(
        documentService: DocumentService
    ): DocumentWriterService {
        return DocumentWriterService(
            documentService
        )
    }

    @Bean
    @ProcessBean
    fun documentReaderService(
        documentService: DocumentService
    ): DocumentReaderService {
        return DocumentReaderService(
            documentService
        )
    }

    @Bean
    @ProcessBean
    fun jobService(): JobService {
        return JobService()
    }

    @Bean
    @ProcessBean
    fun customZaakObjectService(
        openZaakConfigService: OpenZaakConfigService,
        openZaakTokenGeneratorService: OpenZaakTokenGeneratorService,
        zaakDocumentService: ZaakDocumentService
    ): CustomZaakObjectService {
        return CustomZaakObjectService(
            openZaakConfigService,
            openZaakTokenGeneratorService,
            zaakDocumentService
        )
    }

    @Bean
    @ProcessBean
    fun customObjectenApiService(
        objectManagementRepository: ObjectManagementRepository,
        pluginService: PluginService
    ): CustomObjectenApiService {
        return CustomObjectenApiService(
            objectManagementRepository,
            pluginService
        )
    }

    @Bean
    @ProcessBean
    fun customPortaalTaakListener(
        runtimeService: RuntimeService
    ): CustomPortaalTaakListener {
        return CustomPortaalTaakListener(runtimeService)
    }

    @Bean
    @ProcessBean
    fun customPortaalTaakService(
        objectManagementFacade: ObjectManagementFacade,
        objectManagementRepository: ObjectManagementRepository,
        pluginService: PluginService,
        zaakDocumentService: ZaakDocumentService
    ): CustomPortaalTaakService {
        return CustomPortaalTaakService(
            objectManagementFacade,
            objectManagementRepository,
            pluginService,
            zaakDocumentService
        )
    }

    @Bean
    @ProcessBean
    fun customMailService(
        mailService: MailService,
        mailSender: MailSender,
        openZaakResourceRepository: OpenZaakResourceRepository,
        documentenService: DocumentenService
    ): CustomMailService {
        return CustomMailService(
            mailService,
            mailSender,
            openZaakResourceRepository,
            documentenService
        )
    }

    @Bean
    fun webclientBuilder(): WebClient.Builder {
        val objectMapper = MapperSingleton.get()
        val httpClient = HttpClient
            .create(
                ConnectionProvider.builder("asd")
                    .maxIdleTime(Duration.ofSeconds(48))
                    .maxLifeTime(Duration.ofSeconds(48))
                    .pendingAcquireTimeout(Duration.ofSeconds(48))
                    .evictInBackground(Duration.ofSeconds(48))
                    .build()
            )
            .resolver(DefaultAddressResolverGroup.INSTANCE)
            .doOnConnected { conn: Connection ->
                conn.addHandlerLast(
                    ReadTimeoutHandler(30)
                )
                conn.addHandlerLast(
                    WriteTimeoutHandler(30)
                )
            }

        return WebClient
            .builder()
            .clientConnector(
                ReactorClientHttpConnector(httpClient)
            )
            .codecs { configurer ->
                configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024)
                configurer.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper))
                configurer.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper))
            }
    }


    @Bean
    @ProcessBean
    fun customFormFlowService(
        valueResolverService: ValueResolverService,
        taskService: TaskService,
        objectMapper: ObjectMapper
    ): CustomFormFlowService {
        return CustomFormFlowService(
            valueResolverService,
            taskService,
            objectMapper
        )
    }

    @Bean
    @ProcessBean
    fun customZaakInformatieobjectenService(
        zaakDocumentService: ZaakDocumentService,
        pluginService: PluginService
    ): CustomZaakInformatieobjectenService {
        return CustomZaakInformatieobjectenService(
            zaakDocumentService,
            pluginService
        )
    }

    @Bean
    @ProcessBean
    @Primary
    fun openZaakResourceProvider(
        openZaakService: OpenZaakService
    ): ResourceProvider {
        return OpenZaakResourceProvider(openZaakService)
    }

    @Bean
    @ProcessBean
    fun zaakEigenschappenService(
        documentService: DocumentService,
        eigenschappenSubmittedListener: EigenschappenSubmittedListener
    ): ZaakEigenschappenService {
        return ZaakEigenschappenService(documentService, eigenschappenSubmittedListener)
    }

    @Bean
    @ConditionalOnMissingBean(CustomZakenApiClient::class)
    fun customZakenApiClient(
        restClientBuilder: RestClient.Builder
    ): CustomZakenApiClient {
        return CustomZakenApiClient(restClientBuilder)
    }

    @Bean
    @ProcessBean
    @ConditionalOnMissingBean(CustomZakenApiService::class)
    fun customZakenApiService(
        pluginService: PluginService,
        zaakUrlProvider: ZaakUrlProvider,
        zakenApiClient: CustomZakenApiClient,
        zaakdocumentService: ZaakDocumentService
    ): CustomZakenApiService {
        return CustomZakenApiService(
            pluginService,
            zaakUrlProvider,
            zakenApiClient,
            zaakdocumentService
        )
    }

    @Bean
    @ProcessBean
    fun processInstanceService(
        documentService: DocumentService,
        documentAssociationService: CamundaProcessJsonSchemaDocumentAssociationService,
        runtimeService: RuntimeService
    ): ProcessInstanceService {
        return ProcessInstanceService(
            documentService,
            documentAssociationService,
            runtimeService
        )
    }

    @Bean
    @ProcessBean
    fun documentVariableService(): DocumentVariableService {
        return DocumentVariableService()
    }

    @Bean
    @ProcessBean
    fun customInformatieobjectenService(
        temporaryResourceStorageService: TemporaryResourceStorageService,
        documentenApiService: DocumentenApiService,
        zaakDocumentService: ZaakDocumentService
    ): CustomInformatieobjectenService {
        return CustomInformatieobjectenService(
            temporaryResourceStorageService,
            documentenApiService,
            zaakDocumentService
        );
    }

    @Order(201)
    @Bean
    fun customUserHttpSecurityConfigurer(): HttpSecurityConfigurer {
        return CustomUserHttpSecurityConfigurer()
    }

    @Bean
    @ConditionalOnMissingBean(CustomObjectManagementResource::class)
    fun customObjectManagementResource(
        objectManagementFacade: ObjectManagementFacade
    ): CustomObjectManagementResource {
        return CustomObjectManagementResource(
            objectManagementFacade
        )
    }

    @Bean
    @ProcessBean
    fun documentProcessService(
        correlationService: CorrelationService,
        documentService: DocumentService
    ): DocumentProcessService {
        return DocumentProcessService(
            correlationService,
            documentService
        )
    }

    @Bean
    @ProcessBean
    fun customDocumentService(
        documentService: DocumentService
    ): CustomDocumentService {
        return CustomDocumentService(
            documentService
        )
    }
}