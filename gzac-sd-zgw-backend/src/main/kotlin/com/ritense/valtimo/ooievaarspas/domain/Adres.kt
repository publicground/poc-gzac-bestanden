package com.ritense.valtimo.ooievaarspas.domain

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Adres(
    val straat: String? = null,
    val huisnummer: Int? = null,
    val huisletter: String? = null,
    val huisnummertoevoeging: String? = null,
    val postcode: String? = null,
    val woonplaats: String? = null
) {
    val volledigAdres = listOfNotNull(
        straat,
        huisnummer,
        huisletter,
        huisnummertoevoeging,
        postcode,
        woonplaats,
    )
        .filterNot { it == "" }
        .joinToString(" ")
}