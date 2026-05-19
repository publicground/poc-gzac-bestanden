package com.ritense.valtimo.haalcentraal.bag.client

import com.ritense.valtimo.haalcentraal.bag.exception.AddressNotFoundException
import com.ritense.valtimo.haalcentraal.bag.model.AddressRequest
import com.ritense.valtimo.haalcentraal.bag.model.AddressResponse
import com.ritense.valtimo.haalcentraal.shared.HaalCentraalWebClient
import com.ritense.valtimo.haalcentraal.shared.exception.HaalCentraalBadRequestException
import com.ritense.valtimo.haalcentraal.shared.exception.HaalCentraalNotFoundException
import com.ritense.valtimo.haalcentraalauthenticationplugin.HaalCentraalAuthentication
import mu.KotlinLogging
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

class HaalCentraalBagClient(
    private val webClient: HaalCentraalWebClient
) {

    fun getAdresseerbaarObjectIdentificatie(
        baseUrl: URI,
        addressRequest: AddressRequest,
        authentication: HaalCentraalAuthentication
    ): AddressResponse? {

        val uri = UriComponentsBuilder.fromUri(baseUrl)
            .path("/adressen")
            .queryParam("postcode", addressRequest.postcode)
            .queryParam("huisnummer", addressRequest.huisnummer)
            .apply {
                addressRequest.huisnummertoevoeging?.let { queryParam("huisnummertoevoeging", it) }
                addressRequest.huisletter?.let { queryParam("huisletter", it) }
                queryParam("exacteMatch", addressRequest.exacteMatch)
            }
            .build()
            .toUri()

        return try {
            webClient.get<AddressResponse>(uri, authentication)
        } catch (e: HaalCentraalNotFoundException) {
            logger.warn("Not found exception: ${e.message} for postcode: ${addressRequest.postcode} en huisnummer: ${addressRequest.huisnummer}")
            throw AddressNotFoundException(e.message!!)
        } catch (e: HaalCentraalBadRequestException) {
            logger.warn("Bad request exception: ${e.message} for postcode: ${addressRequest.postcode} en huisnummer: ${addressRequest.huisnummer}")
            throw AddressNotFoundException(e.message!!)
        }
    }

    companion object {
        val logger = KotlinLogging.logger {}
    }
}