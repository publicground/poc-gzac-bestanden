package com.ritense.valtimo.haalcentraalkvk.client

data class ClientConfig(
    val handelsregisterBaseUrl: String,
    val connectionTimeout: Int?,
    val responseTimeout: Int?,
    val tokenServiceUrl: String,
    val keystorePath: String?,
    val keystoreSecret: String?,
    val truststorePath: String?,
    val truststoreSecret: String?
)
