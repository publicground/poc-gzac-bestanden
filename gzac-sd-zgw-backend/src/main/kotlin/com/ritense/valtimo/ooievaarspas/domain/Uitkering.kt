package com.ritense.valtimo.ooievaarspas.domain

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Uitkering(
    var actief: Boolean? = false,
    var naam: String? = null,
    var periode: String? = null,
    var nettobedrag: Float? = null
)