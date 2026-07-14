package com.ritense.valtimo.ooievaarspas.domain

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class WelkVermogen(

    var bankrekeningen: Boolean? = null,
    var aandelenObligaties: Boolean? = null,
    var crypto: Boolean? = null,
    var contanten: Boolean? = null
)