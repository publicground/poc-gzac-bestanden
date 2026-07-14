package com.ritense.valtimo.haalcentraalkvk.plugin

import com.ritense.plugin.PluginFactory
import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.haalcentraalkvk.service.HcKvkHandelsregisterService

class HcKvkHandelsregisterPluginFactory(
    private val hcKvkHandelsregisterService: HcKvkHandelsregisterService,
    pluginService: PluginService,
) : PluginFactory<HcKvkHandelsregisterPlugin>(pluginService) {

    override fun create(): HcKvkHandelsregisterPlugin {
        return HcKvkHandelsregisterPlugin(
            hcKvkHandelsregisterService
        )
    }
}