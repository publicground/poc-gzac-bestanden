package com.ritense.valtimo.haalcentraalauthenticationplugin.client

data class HttpClientConfig(
    val connectionTimeout: Int?,
    val responseTimeout: Int?,
    val tokenServiceUrl: String,
    val keystorePath: String?,
    val keystoreSecret: String?,
    val truststorePath: String?,
    val truststoreSecret: String?
)
