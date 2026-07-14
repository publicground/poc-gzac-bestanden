package com.ritense.valtimo.ooievaarspas.domain

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Partner(

    var bsn: String? = null,
    var voorletters: String? = null,
    var tussenvoegsel: String? = null,
    var achternaam: String? = null,
    var identiteitsbewijs: List<Any>? = arrayListOf(),
    var geslacht: String? = null,
    var leeftijd: Int? = null,
    var geboortedatum: String? = null
) {
    val volledigeNaam: String = listOfNotNull(
        voorletters,
        tussenvoegsel,
        achternaam
    )
        .filterNot { it == "" }
        .joinToString(" ")
}