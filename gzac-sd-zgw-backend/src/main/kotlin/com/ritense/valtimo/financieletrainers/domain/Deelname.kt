package com.ritense.valtimo.financieletrainers.domain

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Deelname(
    val trainingsdeelname: Trainingsdeelname,
    val deelnemer: Deelnemer
) {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class Trainingsdeelname(
        val trainingsbeheerDocumentId: String? = null,
        val voedselallergie: String? = null,
        val referentie: String? = null,
        val opmerking: String? = null,
        val toevoegingOpmerking: String? = null
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class Deelnemer(
        val adres: Adres = Adres(),
        val geslacht: String? = null,
        val voornaam: String? = null,
        val achternaam: String? = null,
        val geboortedatum: String? = null,
        val tussenvoegsel: String? = null,
        val telefoonnummer: String? = null,
        val emailadres: String? = null
    ) {
        var volledigeNaam: String = listOfNotNull(
            voornaam,
            tussenvoegsel,
            achternaam
        ).filter { it != "" }.joinToString(" ")
    }

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
        ).filter { it != "" }.joinToString(" ")
    }
}