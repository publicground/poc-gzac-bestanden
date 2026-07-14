package com.ritense.valtimo.custom.valuelogger.autoconfigure

import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.custom.valuelogger.plugin.ValueLoggerPluginFactory
import com.ritense.valueresolver.ValueResolverService
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties
class ValueLoggerAutoConfiguration {
    @Bean
    fun valueLoggerPluginFactory(
        valueResolverService: ValueResolverService,
        pluginService: PluginService,
    ): ValueLoggerPluginFactory {

        return ValueLoggerPluginFactory(
            valueResolverService,
            pluginService
        )
    }
}