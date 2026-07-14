package com.ritense.valtimo.ooievaarspas.domain

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Bezittingen(

    var contantGeld: Float? = null,
    var bankrekeningen: List<Bankrekening> = listOf(),
    var cryptoCurrencies: List<CryptoCurrency> = listOf(),
    var aandelenObligaties: List<AandelenObligatie> = listOf()
)