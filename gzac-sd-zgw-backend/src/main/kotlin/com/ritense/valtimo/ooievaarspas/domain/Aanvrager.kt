package com.ritense.valtimo.ooievaarspas.domain

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Aanvrager(

    var bsn: String? = null,
    var adres: Adres? = Adres(),
    var geslacht: String? = null,
    var leeftijd: Int? = null,
    var voornaam: String? = null,
    var achternaam: String? = null,
    var emailadres: String? = null,
    var voorletters: String? = null,
    var geboortedatum: String? = null,
    var tussenvoegsel: String? = null,
    var telefoonnummer: String? = null,

    ) {
    val volledigeNaam: String = listOfNotNull(
        voorletters,
        tussenvoegsel,
        achternaam
    )
        .filterNot { it == "" }
        .joinToString(" ")
}