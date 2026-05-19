package com.ritense.valtimo.haalcentraalauthenticationplugin.client

import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import mu.KotlinLogging
import org.springframework.http.client.reactive.ReactorClientHttpConnector

class SamlTokenClient(
    private val clientFactoryHelper: ClientFactoryHelper
)  {

    fun getToken(config: HttpClientConfig): String {
        val webClient = webClient(config)
        val response = webClient.post()
            .uri{ uriBuilder ->
                uriBuilder.path("/requestsecuritytoken/v1")
                    .queryParam("b64", true)
                    .build()
            }
            .body(BodyInserters.fromValue(SOAPBODY))
            .retrieve()
            .toEntity(String::class.java)
            .block()!!

        return response.body!!
    }

    private fun webClient(httpClientConfig: HttpClientConfig): WebClient {

        val httpClient = clientFactoryHelper.httpClient(httpClientConfig)

        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .baseUrl(httpClientConfig.tokenServiceUrl)
            .defaultHeader("Content-Type", "text/xml;charset=UTF-8")
            .defaultHeader("SOAPAction", "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Issue")
            .build()
    }

    companion object {
        val logger = KotlinLogging.logger {}
        val SOAPBODY = """
            <soapenv:Envelope
                xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:ns="http://docs.oasis-open.org/ws-sx/ws-trust/200512">
                <soapenv:Header
                    xmlns:wsa="http://www.w3.org/2005/08/addressing">
                    <wsa:Action>http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Issue</wsa:Action>
                </soapenv:Header>
                <soapenv:Body>
                    <ns:RequestSecurityToken>
                        <ns:RequestType>http://docs.oasis-open.org/ws-sx/ws-trust/200512/Issue</ns:RequestType>
                        <ns:TokenType>urn:oasis:names:tc:SAML:2.0:assertion</ns:TokenType>
                    </ns:RequestSecurityToken>
                </soapenv:Body>
            </soapenv:Envelope>
        """.trimIndent()
    }
}