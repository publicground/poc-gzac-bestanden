package com.ritense.valtimo.haalcentraalkvk.model

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class HcVestigingDto(
    val vestigingsnummer: String?,
    val typeringVestiging: String?,
    val handelsNamen: List<String>?,
    val bezoekLocatie: Locatie?,
    val postLocatie: Locatie?
)
