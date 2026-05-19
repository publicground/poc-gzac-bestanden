package com.ritense.valtimo.common.domain

import com.ritense.openzaak.service.impl.model.zaak.Zaak

data class ZaakUpdateRequestBody(
    val bronorganisatie: String,
    val zaaktype: String,
    val verantwoordelijkeOrganisatie: String,
    val startdatum: String,
    val relevanteAndereZaken: List<Zaak.RelevanteAndereZaken>,
)
