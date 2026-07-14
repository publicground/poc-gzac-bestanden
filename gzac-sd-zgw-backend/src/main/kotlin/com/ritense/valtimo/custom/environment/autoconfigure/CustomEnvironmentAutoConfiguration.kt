package com.ritense.valtimo.custom.environment.autoconfigure

import com.ritense.valtimo.contract.annotation.ProcessBean
import com.ritense.valtimo.custom.environment.service.EnvironmentService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class CustomEnvironmentAutoConfiguration {

    @Bean
    @ProcessBean
    fun environmentService(
        environment: Environment
    ): EnvironmentService {
        return EnvironmentService(
            environment
        )
    }
}