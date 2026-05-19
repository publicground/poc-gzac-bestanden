package com.ritense.valtimo.common.service

import com.ritense.valtimo.contract.authentication.CurrentUserService
import com.valtimo.keycloak.service.KeycloakUserManagementService
import mu.KotlinLogging

@Suppress("UNUSED")
class CustomUserRoleService(
    private val currentUserService: CurrentUserService,
    private val keycloakService: KeycloakUserManagementService
) {

    fun hasRole(role: String): Boolean{

        try {
            val result = keycloakService.findByEmail(currentUserService.currentUser.email)
            return if (result.isEmpty) {
                false
            } else {
                result.get().roles?.contains(role)?: false
            }
        } catch (e: Exception) {
            logger.error { "current user not found in keycloak: " + e.stackTraceToString() }
            return false
        }
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}