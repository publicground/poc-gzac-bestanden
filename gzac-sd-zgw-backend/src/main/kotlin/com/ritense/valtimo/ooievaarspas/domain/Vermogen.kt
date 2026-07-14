package com.ritense.valtimo.ooievaarspas.domain

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Vermogen(

    var welkVermogen: WelkVermogen? = null,
    var bezittingen: Bezittingen? = null,
    var schulden: List<Schuld> = listOf()
)