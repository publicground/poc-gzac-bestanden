package com.ritense.valtimo.common.security

import com.ritense.valtimo.contract.security.config.HttpConfigurerConfigurationException
import com.ritense.valtimo.contract.security.config.HttpSecurityConfigurer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import jakarta.ws.rs.HttpMethod


class ActuatorSecurityConfigurer : HttpSecurityConfigurer {

    override fun configure(http: HttpSecurity) {
        try {
            http.authorizeHttpRequests { requests ->
                requests.requestMatchers(HttpMethod.GET, "/metrics/prometheus").permitAll()
            }
        } catch (e: Exception) {
            throw HttpConfigurerConfigurationException(e)
        }
    }
}
