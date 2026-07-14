package com.ritense.valtimo.ooievaarspas.domain

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Werkgever(

    var naam: String? = null,
    var periode: String? = null,
    var nettoloon: Float? = null
)