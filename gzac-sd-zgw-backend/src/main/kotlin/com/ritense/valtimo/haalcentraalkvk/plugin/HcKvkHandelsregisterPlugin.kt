package com.ritense.valtimo.haalcentraalkvk.plugin

import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ritense.plugin.annotation.Plugin
import com.ritense.plugin.annotation.PluginAction
import com.ritense.plugin.annotation.PluginActionProperty
import com.ritense.plugin.annotation.PluginProperty
import com.ritense.processlink.domain.ActivityTypeWithEventName
import com.ritense.valtimo.haalcentraalkvk.client.ClientConfig
import com.ritense.valtimo.haalcentraalkvk.exception.KvkNumberNotFoundException
import com.ritense.valtimo.haalcentraalkvk.service.HcKvkHandelsregisterService
import mu.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import java.net.URI

@Plugin(
    key = "haalcentraalkvkhandelsregister",
    title = "HC Kvk Handelsregister Plugin",
    description = "HC Kvk Handelsregister Kamer van Koophandel API plugin"
)
@Suppress("UNUSED")
class HcKvkHandelsregisterPlugin(
    private val hcKvkHandelsregisterService: HcKvkHandelsregisterService
) {
    @PluginProperty(key = "handelsregisterBaseUrl", secret = false, required = true)
    lateinit var handelsregisterBaseUrl: URI

    @PluginProperty(key = "connectionTimeout", secret = false, required = false)
    var connectionTimeout: Int? = 10000

    @PluginProperty(key = "responseTimeout", secret = false, required = false)
    var responseTimeout: Int? = 10000

    @PluginProperty(key = "tokenServiceUrl", secret = false, required = true)
    lateinit var tokenServiceUrl: URI

    @PluginProperty(key = "keystorePath", secret = false, required = false)
    var keystorePath: String? = null

    @PluginProperty(key = "keystoreSecret", secret = true, required = false)
    var keystoreSecret: String? = null

    @PluginProperty(key = "truststorePath", secret = false, required = false)
    var truststorePath: String? = null

    @PluginProperty(key = "truststoreSecret", secret = true, required = false)
    var truststoreSecret: String? = null

    @PluginAction(
        key = "hc-zoeken-op-kvk-nummer",
        title = "HC Kvk Zoeken",
        description = "HC KvK API zoeken",
        activityTypes = [ActivityTypeWithEventName.SERVICE_TASK_START]
    )

    fun handelsregisterZoeken(
        @PluginActionProperty kvkNummer: String,
        @PluginActionProperty resultProcessVariableName: String,
        @PluginActionProperty maxVestigingen: Int = 100,
        execution: DelegateExecution
    ) {

        logger.info { "Looking for kvk nummer for case ${execution.businessKey}" }

        if (!kvkNummer.isValidKvkNumber()) {
            logger.info { "Provided Kvknumber is invalid for case ${execution.businessKey}" }
            return
        }

        try {
            hcKvkHandelsregisterService.zoekOpKvkNummer(
                getHandelsregisterClientConfig(),
                kvkNummer,
                maxVestigingen
            )?.let {
                execution.processInstance.setVariable(
                    resultProcessVariableName, objectMapper.convertValue(it)
                )
            }
        } catch(knnfe: KvkNumberNotFoundException) {
            return
        }
    }

    private fun getHandelsregisterClientConfig() =
        ClientConfig(
            handelsregisterBaseUrl = handelsregisterBaseUrl.toASCIIString(),
            connectionTimeout = connectionTimeout,
            responseTimeout = responseTimeout,
            truststorePath = truststorePath,
            truststoreSecret = truststoreSecret,
            keystorePath = keystorePath,
            keystoreSecret = keystoreSecret,
            tokenServiceUrl = tokenServiceUrl.toString()
        )

    private fun String.isValidKvkNumber() = kvkRegex.matches(this)

    companion object {
        private val kvkRegex = Regex("""\d{8}""")
        private val logger = KotlinLogging.logger { }
        private val objectMapper = jacksonObjectMapper().findAndRegisterModules()

    }
}