package com.ritense.valtimo.common.security

import com.ritense.valtimo.contract.authentication.AuthoritiesConstants.ADMIN
import com.ritense.valtimo.contract.security.config.HttpConfigurerConfigurationException
import com.ritense.valtimo.contract.security.config.HttpSecurityConfigurer
import jakarta.ws.rs.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity

class CustomUserHttpSecurityConfigurer : HttpSecurityConfigurer {

    override fun configure(http: HttpSecurity) {
        try {
            http.authorizeHttpRequests { requests ->
                requests
                    .requestMatchers(HttpMethod.GET, "/api/v1/users").hasAuthority(ADMIN)
                    .requestMatchers(HttpMethod.POST, "/api/v1/users").hasAuthority(ADMIN)
                    .requestMatchers(HttpMethod.PUT, "/api/v1/users").hasAuthority(ADMIN)
                    .requestMatchers(HttpMethod.PUT, "/api/v1/users/{userId}/activate").hasAuthority(ADMIN)
                    .requestMatchers(HttpMethod.PUT, "/api/v1/users/{userId}/deactivate").hasAuthority(ADMIN)
                    .requestMatchers(HttpMethod.GET, "/api/v1/users/email/{email}/").hasAuthority(ADMIN)
                    .requestMatchers(HttpMethod.GET, "/api/v1/users/{userId}").hasAuthority(ADMIN)
                    .requestMatchers(HttpMethod.GET, "/api/v1/users/authority/{authority}").hasAnyAuthority(ADMIN, "ROLE_AS_CONSULENT_DGT")
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/users/{userId}").hasAuthority(ADMIN)
                    .requestMatchers(HttpMethod.POST, "/api/v1/users/send-verification-email/{userId}").hasAuthority(ADMIN)
                    .requestMatchers(HttpMethod.GET, "/api/v1/object/management/configuration/{objectName}/objects").hasAnyAuthority(ADMIN, "ROLE_IV_BEHANDELAAR")
            }
        } catch (e: Exception) {
            throw HttpConfigurerConfigurationException(e)
        }
    }
}