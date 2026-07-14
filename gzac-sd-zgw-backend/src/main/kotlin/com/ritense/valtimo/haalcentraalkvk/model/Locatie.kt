package com.ritense.valtimo.haalcentraalkvk.model

import com.fasterxml.jackson.annotation.JsonInclude


@JsonInclude(JsonInclude.Include.NON_NULL)
data class Locatie(
    val straatnaam: String?,
    val huisnummer: Int?,
    val huisletter: String?,
    val huisnummertoevoeging: String?,
    val postcode: String?,
    val woonplaatsnaam: String?,
    val buitenlandsAdres: BuitenlandsAdres?,
    val identificatiecodeNummeraanduiding: String?,
    val datumAanvang: Datum?,
    val datumEinde: Datum?
)

