package com.ritense.valtimo.waarderingmantelzorgersenvrijwilligers.service

import com.fasterxml.jackson.databind.JsonNode
import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.waarderingmantelzorgersenvrijwilligers.domain.Page
import com.ritense.valtimo.waarderingmantelzorgersenvrijwilligers.domain.ZaakType
import com.ritense.zakenapi.ZaakUrlProvider
import com.ritense.zakenapi.ZakenApiAuthentication
import com.ritense.zakenapi.ZakenApiPlugin
import com.ritense.zakenapi.domain.rol.RolNatuurlijkPersoon
import com.ritense.zakenapi.domain.rol.RolType
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.bodyToMono
import java.util.UUID

class WmvZakenApiService(
    private val pluginService: PluginService,
    private val zaakUrlProvider: ZaakUrlProvider,
    private val webclientBuilder: WebClient.Builder
) {

    fun getZaakInitiatorBSNByBusinessKey(businessKey: String): String {
        val zaakUrl = zaakUrlProvider.getZaakUrl(UUID.fromString(businessKey))
        val zakenApiPlugin = requireNotNull(
            pluginService.createInstance(ZakenApiPlugin::class.java, ZakenApiPlugin.findConfigurationByUrl(zaakUrl))
        ) { "No plugin configuration was found for zaak with URL $zaakUrl" }

        val initiatorRol = zakenApiPlugin
            .getZaakRollen(zaakUrl, RolType.INITIATOR)
            .firstOrNull()
            ?: throw RuntimeException("Zaak does not have an Initiator Role")

        return when (val identificatie = initiatorRol.betrokkeneIdentificatie) {
            is RolNatuurlijkPersoon -> {
                requireNotNull(identificatie.inpBsn) {
                    "Zaak initiator did not have valid inpBsn BSN"
                }
            }

            else -> throw RuntimeException("Zaak initiator did not have valid inpBsn BSN")
        }
    }

    fun getAmountOfZakenByBsn(bsn: String, businessKey: String): Int {
        val zaakUrl = zaakUrlProvider.getZaakUrl(UUID.fromString(businessKey))
        val zakenApiPlugin = requireNotNull(
            pluginService.createInstance(ZakenApiPlugin::class.java, ZakenApiPlugin.findConfigurationByUrl(zaakUrl))
        ) { "No plugin configuration was found for zaak with URL $zaakUrl" }
        val zaakTypeUrls = getZaakTypenByIdentifier(
            authentication = zakenApiPlugin.authenticationPluginConfiguration,
            catalogiApiUrl = "https://${zakenApiPlugin.url.host}/catalogi/api/v1/",
            identificatie = WMV_ZAAKTYPEN_IDENTIFICATIE
        )

        return zaakTypeUrls
            .map { zaakType ->
                getAmountOfZakenForInpBsn(
                    authentication = zakenApiPlugin.authenticationPluginConfiguration,
                    zakenApiUrl = zakenApiPlugin.url.toASCIIString(),
                    zaakTypeUrl = zaakType.url.toASCIIString(),
                    bsn = bsn
                )
            }
            .reduceOrNull { count, acc -> acc + count }
            ?: 0
    }

    private fun getZaakTypenByIdentifier(
        authentication: ZakenApiAuthentication,
        catalogiApiUrl: String,
        identificatie: String
    ): List<ZaakType> {
        val result = runBlocking {
            webclientBuilder
                .clone()
                .filter(authentication)
                .baseUrl(catalogiApiUrl)
                .build()
                .get()
                .uri {
                    with(it) {
                        path("zaaktypen")
                        queryParam("identificatie", identificatie)
                        build()
                    }
                }
                .headers {
                    with(it) {
                        set("Accept-Crs", "EPSG:4326")
                        set("Content-Crs", "EPSG:4326")
                    }
                }
                .retrieve()
                .onStatus({ status -> status.isError }) { response ->
                    throw HttpClientErrorException(response.statusCode(), response.bodyToMono<String>().toString())
                }
                .awaitBody<Page<ZaakType>>()
        }

        return result.results
    }

    private fun getAmountOfZakenForInpBsn(
        authentication: ZakenApiAuthentication,
        zakenApiUrl: String,
        zaakTypeUrl: String,
        bsn: String
    ): Int {
        val result = runBlocking {
            webclientBuilder
                .clone()
                .filter(authentication)
                .baseUrl(zakenApiUrl)
                .build()
                .get()
                .uri {
                    with(it) {
                        path("zaken")
                        queryParam("zaaktype", zaakTypeUrl)
                        queryParam("rol__betrokkeneIdentificatie__natuurlijkPersoon__inpBsn", bsn)
                        build()
                    }
                }
                .headers {
                    with(it) {
                        set("Accept-Crs", "EPSG:4326")
                        set("Content-Crs", "EPSG:4326")
                    }
                }
                .retrieve()
                .onStatus({ status -> status.isError }) { response ->
                    throw HttpClientErrorException(response.statusCode(), response.bodyToMono<String>().toString())
                }
                .awaitBody<Page<JsonNode>>()
        }

        return result.count
    }

    companion object {
        private const val WMV_ZAAKTYPEN_IDENTIFICATIE = "aanvraag-mantelzorg-vrijwilligerswaardering"
    }
}