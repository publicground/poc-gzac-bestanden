package com.ritense.valtimo.haalcentraalauthenticationplugin.autoconfigure

import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.haalcentraalauthenticationplugin.client.ClientFactoryHelper
import com.ritense.valtimo.haalcentraalauthenticationplugin.client.SamlTokenClient
import com.ritense.valtimo.haalcentraalauthenticationplugin.plugin.HaalCentraalAuthenticationPluginFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties
class HaalCentraalAuthenticationPluginAutoconfiguration {

    @Bean
    fun haalCentraalAuthenticationPluginFactory(
        pluginService: PluginService,
        samlTokenClient: SamlTokenClient,
        clientFactoryHelper: ClientFactoryHelper
    ): HaalCentraalAuthenticationPluginFactory {
        return HaalCentraalAuthenticationPluginFactory(pluginService, samlTokenClient, clientFactoryHelper)
    }

    @Bean
    fun clientFactoryHelper(): ClientFactoryHelper {
        return ClientFactoryHelper()
    }

    @Bean
    fun samlTokenWebClient(
        clientFactoryHelper: ClientFactoryHelper
    ): SamlTokenClient {
        return SamlTokenClient(
            clientFactoryHelper
        )
    }
}