package com.ritense.valtimo.haalcentraalkvk.model

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class HcKvkDto(
    val vestigingen: List<HcVestigingDto>?,
    val kvkNummer: String,
    val naam: String
)
