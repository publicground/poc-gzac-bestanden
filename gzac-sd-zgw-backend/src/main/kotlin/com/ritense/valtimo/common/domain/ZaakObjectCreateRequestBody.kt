package com.ritense.valtimo.common.domain

import com.fasterxml.jackson.annotation.JsonProperty

class ZaakObjectCreateRequestBody(
    var zaak: String,
    @JsonProperty("object")
    var objectUrl: String,
    var objectType: String,
    var objectTypeOverige: String,
)
