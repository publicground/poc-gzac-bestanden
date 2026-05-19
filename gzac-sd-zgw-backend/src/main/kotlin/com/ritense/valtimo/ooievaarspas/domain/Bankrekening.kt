package com.ritense.valtimo.ooievaarspas.domain

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Bankrekening(

    var iban: String? = null,
    var tegoed: Float? = null,
    var documenten: List<Any>? = listOf()
)