package com.ritense.valtimo.bbz.domain

data class Informatieverzoek(
    val verzoek: Map<String, Any?>,
    var reactie: Map<String, Any?>?
)
