package com.ritense.valtimo.health

import com.ritense.valtimo.security.ActuatorSecurityFilterChainFactory
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator
import org.springframework.boot.actuate.autoconfigure.health.HealthEndpointProperties
import org.springframework.boot.actuate.health.HealthContributor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
class HealthCheckConfig {

    @Bean
    @ConditionalOnEnabledHealthIndicator("livenessState")
    fun livenessStateHealthIndicator(meterRegistry: MeterRegistry): HealthContributor {
        return CustomHealthIndicator(meterRegistry)
    }

    @Order(49)
    @Bean
    fun actuatorSecurityFilterChain(
        httpSecurity: HttpSecurity?,
        webEndpointProperties: WebEndpointProperties?,
        healthEndpointProperties: HealthEndpointProperties?,
        passwordEncoder: PasswordEncoder?,
        @Value("\${spring-actuator.username}") username: String?,
        @Value("\${spring-actuator.password}") password: String?
    ): SecurityFilterChain {
        return ActuatorSecurityFilterChainFactory().createFilterChain(
            httpSecurity!!,
            webEndpointProperties!!,
            healthEndpointProperties,
            passwordEncoder!!,
            username!!,
            password!!
        )
    }

}