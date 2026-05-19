
package com.ritense.valtimo.security

import com.ritense.valtimo.contract.authentication.AuthoritiesConstants.ACTUATOR
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties
import org.springframework.boot.actuate.autoconfigure.health.HealthEndpointProperties
import org.springframework.boot.actuate.endpoint.Show
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher


/*
* This class replaces the ActuatorSecurityFilterChainFactory class from the Valtimo backend libraries
* The goal is to make the liveness and readiness endpoint available without authentication.
* This change has also been submitted as a PR to the valtimo backend libraries. See:
* https://github.com/valtimo-platform/valtimo-issues/issues/4
* and
* https://github.com/valtimo-platform/valtimo-backend-libraries/pull/1527
*
* TODO:
* This class and the bean configuration can be removed when the PR is merged to valtimo (and we upgrade to that version).
* */

class ActuatorSecurityFilterChainFactory {

    fun createFilterChain(
        http: HttpSecurity,
        webEndpointProperties: WebEndpointProperties,
        healthEndpointProperties: HealthEndpointProperties?,
        passwordEncoder: PasswordEncoder,
        username: String,
        password: String
    ): SecurityFilterChain {
        val matchers = getActuatorMatchers(webEndpointProperties.basePath)
        val healthMatchers = getHealthMatchers(webEndpointProperties.basePath)
        http
            .securityMatcher(OrRequestMatcher(*matchers))
            .authorizeHttpRequests {
                if (healthEndpointProperties != null && (
                        //Allow access to this endpoint only if the details are not shown to anonymous users or users without ROLE_ACTUATOR
                        healthEndpointProperties.showDetails == Show.NEVER || (
                            healthEndpointProperties.showDetails == Show.WHEN_AUTHORIZED &&
                                healthEndpointProperties.roles.contains(ACTUATOR)
                            )
                        )) {
                    it.requestMatchers(*healthMatchers).permitAll()
                }
                it.requestMatchers(*matchers).hasAuthority(ACTUATOR)
            }
            .authenticationManager(actuatorAuthenticationManager(passwordEncoder, username, password))
            .httpBasic { it.realmName(ACTUATOR_REALM) }

        return http.build()
    }

    private fun actuatorAuthenticationManager(
        passwordEncoder: PasswordEncoder,
        username: String,
        password: String
    ): AuthenticationManager {
        val userDetailsService: UserDetailsService = userDetailsService(passwordEncoder, username, password)
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setPasswordEncoder(passwordEncoder)
        authenticationProvider.setUserDetailsService(userDetailsService)

        return ProviderManager(authenticationProvider)
    }

    private fun userDetailsService(
        passwordEncoder: PasswordEncoder,
        username: String,
        password: String
    ): UserDetailsService {
        val actuatorUser: UserDetails = User
            .withUsername(username)
            .password(passwordEncoder.encode(password))
            .authorities(ACTUATOR)
            .build()

        return InMemoryUserDetailsManager(actuatorUser)
    }

    private fun getActuatorMatchers(actuatorPath: String) = arrayOf(
        antMatcher(GET, actuatorPath),
        antMatcher(GET, "${actuatorPath}/configprops"),
        antMatcher(GET, "${actuatorPath}/env"),
        *getHealthMatchers(actuatorPath),
        antMatcher(GET, "${actuatorPath}/mappings"),
        antMatcher(GET, "${actuatorPath}/logfile"),
        antMatcher(GET, "${actuatorPath}/loggers"),
        antMatcher(POST, "${actuatorPath}/loggers/**"),
        antMatcher(GET, "${actuatorPath}/info"),
    )

    private fun getHealthMatchers(actuatorPath: String) =
        arrayOf(
            antMatcher(GET, "${actuatorPath}/health"),
            antMatcher(GET, "${actuatorPath}/health/liveness"),
            antMatcher(GET, "${actuatorPath}/health/readiness")
        )

    companion object {
        const val ACTUATOR_REALM = "Actuator realm"
    }
}