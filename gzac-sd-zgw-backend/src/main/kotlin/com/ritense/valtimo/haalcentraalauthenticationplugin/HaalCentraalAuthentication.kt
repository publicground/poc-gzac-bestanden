package com.ritense.valtimo.haalcentraalauthenticationplugin

import com.ritense.plugin.annotation.PluginCategory
import org.springframework.web.client.RestClient
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import reactor.netty.http.client.HttpClient

@PluginCategory("haal-centraal-authentication-plugin")
interface HaalCentraalAuthentication : ExchangeFilterFunction {

    fun applyAuth(builder: RestClient.Builder): RestClient.Builder

    fun getAuthenticatedHttpClient(): HttpClient
}