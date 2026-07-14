package com.ritense.valtimo.haalcentraalapikeyauthentication.plugin

import com.ritense.plugin.PluginFactory
import com.ritense.plugin.service.PluginService

class HaalCentraalApiKeyAuthenticationPluginFactory(
    pluginService: PluginService
) : PluginFactory<HaalCentraalApiKeyAuthenticationPlugin>(pluginService) {

    override fun create(): HaalCentraalApiKeyAuthenticationPlugin {
        return HaalCentraalApiKeyAuthenticationPlugin()
    }
}