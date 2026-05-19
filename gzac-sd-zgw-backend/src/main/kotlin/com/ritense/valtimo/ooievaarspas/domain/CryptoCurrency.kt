package com.ritense.valtimo.ooievaarspas.domain

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CryptoCurrency(

    var waarde: Float? = null,
    var omschrijving: String? = null,
    var documenten: List<Any>? = listOf()
)