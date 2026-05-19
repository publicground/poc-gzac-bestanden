package com.ritense.valtimo.haalcentraalkvk.model

import com.fasterxml.jackson.annotation.JsonProperty

data class HcZoekenResponse(
    @JsonProperty("pagina")
    val pagina: Int,
    @JsonProperty("resultatenPerPagina")
    val resultatenPerPagina: Int,
    @JsonProperty("totaal")
    val totaal: Int,
    @JsonProperty("resultaten")
    val resultaten: List<HcResultaat>,
    @JsonProperty("links")
    val links: List<Link>
)

data class HcResultaat(
    @JsonProperty("kvkNummer")
    val kvkNummer: String,
    @JsonProperty("naam")
    val naam: String,
    @JsonProperty("type")
    val type: String,
    @JsonProperty("links")
    val links: List<Link>,
    @JsonProperty("vestigingsnummer")
    val vestigingsnummer: String? = null,
    @JsonProperty("adres")
    val hcAdres: HcAdres? = null
)

data class HcAdres(
    @JsonProperty("binnenlandsAdres")
    val hcBinnenlandsAdres: HcBinnenlandsAdres
)

data class HcBinnenlandsAdres(
    @JsonProperty("type")
    val type: String,
    @JsonProperty("straatnaam")
    val straatnaam: String,
    @JsonProperty("plaats")
    val plaats: String
)

