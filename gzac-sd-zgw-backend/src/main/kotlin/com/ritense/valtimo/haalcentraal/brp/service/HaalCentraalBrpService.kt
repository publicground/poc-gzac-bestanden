package com.ritense.valtimo.haalcentraal.brp.service

import com.ritense.valtimo.haalcentraal.brp.client.HcBrpClient
import com.ritense.valtimo.haalcentraal.brp.model.BewoningDto
import com.ritense.valtimo.haalcentraal.brp.model.BewoningenRequest
import com.ritense.valtimo.haalcentraalauthenticationplugin.HaalCentraalAuthentication
import mu.KotlinLogging
import java.net.URI
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class HaalCentraalBrpService(
    private val haalCentraalBrpClient: HcBrpClient
) {

    fun getBewoningen(
        baseUrl: URI,
        bewoningenRequest: BewoningenRequest,
        authentication: HaalCentraalAuthentication
    ): List<BewoningDto>? {
        logger.info("Retrieving bewoningen for adresseerbaarObjectIdentificatie: ${bewoningenRequest.adresseerbaarObjectIdentificatie}")

        return haalCentraalBrpClient.getBewoningen(
            baseUrl = baseUrl,
            bewoningenRequest = bewoningenRequest,
            authentication = authentication
        )?.bewoningen?.map { bewoning ->
            BewoningDto(
                adresseerbaarObjectIdentificatie = bewoning.adresseerbaarObjectIdentificatie,
                bewoners = bewoning.bewoners,
                mogelijkeBewoners = bewoning.mogelijkeBewoners,
                indicatieVeelBewoners = bewoning.indicatieVeelBewoners
            )
        }
    }

    fun getPeildatum(peildatumDatetimeString: String): String {
        return OffsetDateTime
            .parse(peildatumDatetimeString)
            .format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
