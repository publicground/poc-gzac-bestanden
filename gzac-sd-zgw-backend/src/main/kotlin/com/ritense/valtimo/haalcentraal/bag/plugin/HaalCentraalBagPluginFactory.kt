package com.ritense.valtimo.haalcentraal.bag.plugin

import com.ritense.plugin.PluginFactory
import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.haalcentraal.bag.service.HaalCentraalBagService

class HaalCentraalBagPluginFactory(
    private val haalCentraalBagService: HaalCentraalBagService,
    pluginService: PluginService
) : PluginFactory<HaalCentraalBagPlugin>(pluginService) {
    override fun create(): HaalCentraalBagPlugin {
        return HaalCentraalBagPlugin(
            haalCentraalBagService
        )
    }
}