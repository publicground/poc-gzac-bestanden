package com.ritense.valtimo.haalcentraalauthenticationplugin.plugin

import com.ritense.plugin.annotation.Plugin
import com.ritense.plugin.annotation.PluginProperty
import com.ritense.valtimo.haalcentraalauthenticationplugin.HaalCentraalAuthentication
import com.ritense.valtimo.haalcentraalauthenticationplugin.client.ClientFactoryHelper
import com.ritense.valtimo.haalcentraalauthenticationplugin.client.HttpClientConfig
import com.ritense.valtimo.haalcentraalauthenticationplugin.client.SamlTokenClient
import org.springframework.web.client.RestClient
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFunction
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import java.net.URI

@Plugin(
    key = "haal-centraal-authentication-plugin",
    title = "Haal Centraal Authentication Plugin",
    description = "Plugin used to provide authentication to haal centraal"
)
@Suppress("UNUSED")
class HaalCentraalAuthenticationPlugin(
    private val samlTokenClient: SamlTokenClient,
    private val clientFactoryHelper: ClientFactoryHelper
): HaalCentraalAuthentication {

    @PluginProperty(key = "tokenServiceUrl", secret = false, required = true)
    lateinit var tokenServiceUrl: URI

    @PluginProperty(key = "keystorePath", secret = false, required = false)
    var keystorePath: String? = null

    @PluginProperty(key = "keystoreSecret", secret = true, required = false)
    var keystoreSecret: String? = null

    @PluginProperty(key = "truststorePath", secret = false, required = false)
    var truststorePath: String? = null

    @PluginProperty(key = "truststoreSecret", secret = true, required = false)
    var truststoreSecret: String? = null

    @PluginProperty(key = "connectionTimeout", secret = false, required = false)
    var connectionTimeout: Int? = 10000

    @PluginProperty(key = "responseTimeout", secret = false, required = false)
    var responseTimeout: Int? = 10000

    override fun filter(request: ClientRequest, next: ExchangeFunction): Mono<ClientResponse> {
        val getToken = samlTokenClient.getToken(
            getClientConfig()
        )
        val filteredRequest = ClientRequest.from(request).headers { headers ->
            headers.set("x-saml-attribute-token1", getToken)
        }.build()
        return next.exchange(filteredRequest)
    }

    override fun applyAuth(builder: RestClient.Builder): RestClient.Builder {
        val getToken = samlTokenClient.getToken(
            getClientConfig()
        )
        return builder.defaultHeaders { headers ->
            headers.setBearerAuth(getToken)
        }
    }

    override fun getAuthenticatedHttpClient(): HttpClient {
        return clientFactoryHelper.httpClient(getClientConfig())
    }

    private fun getClientConfig() =
        HttpClientConfig(
            tokenServiceUrl = tokenServiceUrl.toString(),
            truststorePath = truststorePath,
            truststoreSecret = truststoreSecret,
            keystorePath = keystorePath,
            keystoreSecret = keystoreSecret,
            connectionTimeout = connectionTimeout,
            responseTimeout = responseTimeout
        )
}