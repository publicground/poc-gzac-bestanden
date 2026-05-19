package com.ritense.valtimo.haalcentraalkvk.service

import com.ritense.valtimo.haalcentraalkvk.client.ClientConfig
import com.ritense.valtimo.haalcentraalkvk.client.HcKvkHandelsregisterClient
import com.ritense.valtimo.haalcentraalkvk.exception.KvkVestigingNotFoundException
import com.ritense.valtimo.haalcentraalkvk.model.HcKvkDto
import com.ritense.valtimo.haalcentraalkvk.model.HcMaatschappelijkeActiviteiten
import com.ritense.valtimo.haalcentraalkvk.model.HcVestigingDto
import com.ritense.valtimo.haalcentraalkvk.model.Link
import mu.KotlinLogging

class HcKvkHandelsregisterService(
    private val kvkHandelsregisterClient: HcKvkHandelsregisterClient,
) {

    fun zoekOpKvkNummer(
        clientConfig: ClientConfig,
        kvkNumber: String,
        maxVestigingen: Int
    ): HcKvkDto? {

        val organisatie = kvkHandelsregisterClient.getMaatschappelijkeActiviteiten(clientConfig, kvkNumber)
        val vestigingDtos =  organisatie?.let{getVestigingDtos(it, maxVestigingen, clientConfig)}
        val organisatieDto = organisatie?.let{ HcKvkDto( vestigingDtos, it.kvkNummer, it.naam)}

        return organisatieDto

    }

    private fun getVestigingDtos(
        organisatie: HcMaatschappelijkeActiviteiten,
        maxVestigingen: Int,
        clientConfig: ClientConfig)
        : List<HcVestigingDto> {
        val vestigingDtos = organisatie._links.vestigingen?.take(maxVestigingen)?.mapNotNull { vestiging ->
            try {
                getVestiging(vestiging, clientConfig)
            } catch (e: KvkVestigingNotFoundException) {
                null
            }
        }?.toMutableList()

        val vestigingNietNatuurlijkPersoonDto = getVestigingNietNatuurlijkPersoon(organisatie, clientConfig)

        if (vestigingDtos == null && vestigingNietNatuurlijkPersoonDto != null) {
            return listOf(vestigingNietNatuurlijkPersoonDto)
        } else if ( vestigingDtos != null && vestigingNietNatuurlijkPersoonDto == null) {
            return vestigingDtos
        } else if ( vestigingDtos != null && vestigingNietNatuurlijkPersoonDto != null ) {
            vestigingDtos.add(vestigingNietNatuurlijkPersoonDto)
            return vestigingDtos
        } else {
            return emptyList()
        }

    }

    private fun getVestigingNietNatuurlijkPersoon(
        organisatie: HcMaatschappelijkeActiviteiten,
        clientConfig: ClientConfig)
    : HcVestigingDto? {

        return if  (organisatie._links.eigenaar?.href?.contains("nietnatuurlijkpersonen") == true) {
            val nietNatuurlijkPersoon = runCatching {  kvkHandelsregisterClient.getNietNatuurlijkPersoon(
                clientConfig,
                organisatie._links.eigenaar.href
            )}.getOrNull()
            nietNatuurlijkPersoon?.bezoekLocatie?.let {
                HcVestigingDto(
                    vestigingsnummer = null,
                    typeringVestiging = null,
                    handelsNamen = listOf(),
                    postLocatie = null,
                    bezoekLocatie = nietNatuurlijkPersoon.bezoekLocatie,
                )
            }
        } else {
            null
        }
    }

    private fun getVestiging(
        vestigingLink: Link,
        clientConfig: ClientConfig
    ): HcVestigingDto? {

        logger.info("Retrieving KVK vestiging: ${vestigingLink.href}) ")

        return kvkHandelsregisterClient.getVestiging(clientConfig, vestigingLink)?.let { vestiging ->
            HcVestigingDto(
                vestigingsnummer = vestiging.vestigingsnummer,
                typeringVestiging = vestiging.typeringVestiging,
                handelsNamen = vestiging.handelsnamen,
                bezoekLocatie = vestiging.bezoekLocatie,
                postLocatie = vestiging.postLocatie
            )
        }
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}