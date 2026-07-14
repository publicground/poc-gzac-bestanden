package com.ritense.valtimo.haalcentraalkvk.client

import com.ritense.valtimo.haalcentraal.shared.exception.HaalCentraalBadRequestException
import com.ritense.valtimo.haalcentraal.shared.exception.HaalCentraalNotFoundException
import com.ritense.valtimo.haalcentraalkvk.exception.KvkNumberNotFoundException
import com.ritense.valtimo.haalcentraalkvk.exception.KvkVestigingNotFoundException
import com.ritense.valtimo.haalcentraalkvk.model.HcMaatschappelijkeActiviteiten
import com.ritense.valtimo.haalcentraalkvk.model.Link
import com.ritense.valtimo.haalcentraalkvk.model.NietNatuurlijkPersoonResponse
import com.ritense.valtimo.haalcentraalkvk.model.VestigingResponse
import mu.KotlinLogging
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.net.URI

class HcKvkHandelsregisterClient(
    val webclientFactoryHelper: WebclientFactoryHelper,
    private val samlTokenWebClient: SamlTokenWebClient

) {

    inline fun <reified T : Any> get(
        uri: URI,
        clientConfig: ClientConfig,
        token: String
    ): T? {
            val httpClient = webclientFactoryHelper.httpClient(clientConfig)
            return buildWebClient(httpClient, token)
                .get()
                .uri(uri)
                .retrieve()
                .onStatus({ status -> status.value() == 404 } ) {
                    throw HaalCentraalNotFoundException("Niets gevonden")
                }
                .onStatus({ status -> status.value() == 400 }) {
                    throw HaalCentraalBadRequestException("Niets gevonden")
                }
                .bodyToMono(T::class.java)
                .block()
    }

    fun getEigenaar(
        uri: URI,
        clientConfig: ClientConfig,
        token: String
    ): NietNatuurlijkPersoonResponse {
        val httpClient = webclientFactoryHelper.httpClient(clientConfig)
        val response = buildWebClient(httpClient, token)
            .get()
            .uri(uri)
            .retrieve()
            .onStatus({ status -> status.value() == 404 }) {
                throw HaalCentraalNotFoundException("Niets gevonden")
            }
            .bodyToMono(NietNatuurlijkPersoonResponse::class.java)
            .block()
        return response!!
    }

    fun buildWebClient(httpClient: HttpClient, token: String) =
        WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .codecs { configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024) }
            .defaultHeader("x-saml-attribute-token1", token)
            .build()

    fun getMaatschappelijkeActiviteiten(
        clientConfig: ClientConfig,
        kvkNumber: String
    ): HcMaatschappelijkeActiviteiten? {
        val token = samlTokenWebClient.getToken(clientConfig)
        val uri = URI("${clientConfig.handelsregisterBaseUrl}/maatschappelijkeactiviteiten/$kvkNumber")

        return try {
            get<HcMaatschappelijkeActiviteiten>(uri, clientConfig, token)
        } catch (e: HaalCentraalNotFoundException) {
            logger.warn("Not found exception: ${e.message} for kvk number: ${kvkNumber}")
            throw KvkNumberNotFoundException(e.message!!)
        } catch (e: HaalCentraalBadRequestException) {
            logger.warn("Bad request exception: ${e.message} for kvk number: ${kvkNumber}")
            throw KvkNumberNotFoundException(e.message!!)
        }
    }

    fun getVestiging(clientConfig: ClientConfig, vestigingLink: Link): VestigingResponse? {
        val token = samlTokenWebClient.getToken(clientConfig)
        val vestigingNummer = vestigingLink.href.split("/").last()
        val uri = URI("${clientConfig.handelsregisterBaseUrl}/vestigingen/${vestigingNummer}")
        return try {
            get<VestigingResponse>(uri, clientConfig, token)
        } catch (e: HaalCentraalNotFoundException) {
            throw KvkVestigingNotFoundException(vestigingNummer)
        }
    }

    fun getNietNatuurlijkPersoon(clientConfig: ClientConfig, href: String) : NietNatuurlijkPersoonResponse? {
        val token = samlTokenWebClient.getToken(clientConfig)
        val rsin = href.split("/").last()
        val uri = URI("${clientConfig.handelsregisterBaseUrl}/nietnatuurlijkpersonen/${rsin}")
        return try {
            getEigenaar(uri, clientConfig, token)
        } catch (e: HaalCentraalNotFoundException) {
            throw KvkVestigingNotFoundException(rsin)
        }
    }

    companion object {
        val logger = KotlinLogging.logger {}
    }

}