package com.ritense.valtimo.custom.environment.service

import com.ritense.authorization.annotation.RunWithoutAuthorization
import org.springframework.core.env.Environment

open class EnvironmentService(
    private val environment: Environment
) {
    @RunWithoutAuthorization
    open fun getActiveProfile(): String {
        return environment.activeProfiles.firstOrNull() ?: "dev"
    }
}