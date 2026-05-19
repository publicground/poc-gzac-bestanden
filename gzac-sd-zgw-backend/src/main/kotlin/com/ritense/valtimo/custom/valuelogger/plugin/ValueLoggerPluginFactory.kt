package com.ritense.valtimo.custom.valuelogger.plugin

import com.ritense.plugin.PluginFactory
import com.ritense.plugin.service.PluginService
import com.ritense.valueresolver.ValueResolverService

class ValueLoggerPluginFactory(
    private val valueResolverService: ValueResolverService,
    pluginService: PluginService,
) : PluginFactory<ValueLoggerPlugin>(pluginService) {
    override fun create(): ValueLoggerPlugin {
        return ValueLoggerPlugin(
            valueResolverService,
        )
    }
}
