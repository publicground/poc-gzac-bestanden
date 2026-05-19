package com.ritense.valtimo.common.client

import com.ritense.openzaak.service.impl.OpenZaakConfigService
import com.ritense.openzaak.service.impl.OpenZaakRequestBuilder
import com.ritense.openzaak.service.impl.OpenZaakTokenGeneratorService
import com.ritense.openzaak.service.impl.model.zaak.Zaak
import com.ritense.valtimo.common.domain.ZaakUpdateRequestBody
import com.ritense.valtimo.common.enums.ZaakrelatieType
import mu.KotlinLogging
import org.springframework.boot.web.client.RestTemplateBuilder

class OpenzaakClient(
    private val openZaakConfigService: OpenZaakConfigService,
    private val openZaakTokenGeneratorService: OpenZaakTokenGeneratorService
) {

    fun linkZaakToZaak(hoofdzaak: Zaak, relevanteZaak: Zaak, aardRelatie: ZaakrelatieType): Zaak {
        val relevanteAndereZaak = Zaak.RelevanteAndereZaken(
            relevanteZaak.url,
            aardRelatie.relatie
        )

        val relevanteAndereZaken = hoofdzaak.relevanteAndereZaken?.toMutableList() ?: mutableListOf()
        relevanteAndereZaken.add(relevanteAndereZaak)

        val zaakUpdateRequestBody = ZaakUpdateRequestBody(
            hoofdzaak.bronorganisatie,
            hoofdzaak.zaaktype.toString(),
            hoofdzaak.verantwoordelijkeOrganisatie,
            hoofdzaak.startdatum,
            relevanteAndereZaken.toList()
        )

        logger.info("Linking zaak ${hoofdzaak.identificatie} " +
            "to ${relevanteZaak.identificatie} with relation type $aardRelatie"
        )

        return OpenZaakRequestBuilder(RestTemplateBuilder().build(), openZaakConfigService, openZaakTokenGeneratorService)
            .path("zaken/api/v1/zaken/" + hoofdzaak.uuid)
            .put()
            .body(zaakUpdateRequestBody)
            .build()
            .execute(Zaak::class.java)
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
