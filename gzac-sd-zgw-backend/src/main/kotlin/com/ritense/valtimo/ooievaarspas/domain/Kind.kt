package com.ritense.valtimo.ooievaarspas.domain

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Kind(

    var bsn: String? = null,
    var voorletters: String? = null,
    var voornaam: String? = null,
    var tussenvoegsel: String? = null,
    var achternaam: String? = null,
    var geboortedatum: String? = null
) {
    val volledigeNaam: String = listOfNotNull(
        voornaam,
        tussenvoegsel,
        achternaam
    )
        .filterNot { it == "" }
        .joinToString(" ")
}