package com.ritense.valtimo.jobretry

import com.ritense.gzac.JobExecutionPlugin
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AutoConfiguration {

    @Bean
    fun jobExecutionPlugin (): JobExecutionPlugin {
        return JobExecutionPlugin()
    }
}