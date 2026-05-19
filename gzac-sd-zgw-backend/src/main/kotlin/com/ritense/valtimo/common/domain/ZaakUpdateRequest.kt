package com.ritense.valtimo.common.domain

import com.ritense.zakenapi.domain.RelevanteZaak
import com.ritense.zgw.Rsin
import java.time.LocalDate

data class ZaakUpdateRequest(
    val bronorganisatie: Rsin,
    val zaaktype: String,
    val verantwoordelijkeOrganisatie: Rsin,
    val startdatum: LocalDate,
    val relevanteAndereZaken: List<RelevanteZaak>,
)