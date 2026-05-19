package com.ritense.valtimo.haalcentraalapikeyauthentication.autoconfigure

import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.haalcentraalapikeyauthentication.plugin.HaalCentraalApiKeyAuthenticationPluginFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties
class HaalCentraalApiKeyAuthenticationPluginAutoconfiguration {

    @Bean
    fun haalCentraalApiKeyAuthenticationPluginFactory(
        pluginService: PluginService
    ): HaalCentraalApiKeyAuthenticationPluginFactory {
        return HaalCentraalApiKeyAuthenticationPluginFactory(pluginService)
    }
}