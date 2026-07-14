package com.ritense.valtimo.haalcentraalauthenticationplugin.plugin

import com.ritense.plugin.PluginFactory
import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.haalcentraalauthenticationplugin.client.ClientFactoryHelper
import com.ritense.valtimo.haalcentraalauthenticationplugin.client.SamlTokenClient

class HaalCentraalAuthenticationPluginFactory(
    pluginService: PluginService,
    private val samlTokenClient: SamlTokenClient,
    private val clientFactoryHelper: ClientFactoryHelper
) : PluginFactory<HaalCentraalAuthenticationPlugin>(pluginService) {

    override fun create(): HaalCentraalAuthenticationPlugin {
        return HaalCentraalAuthenticationPlugin(samlTokenClient, clientFactoryHelper)
    }
}