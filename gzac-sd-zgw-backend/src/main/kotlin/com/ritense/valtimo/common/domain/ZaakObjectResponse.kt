package com.ritense.valtimo.common.domain

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode

class ZaakObjectResponse (
    var url: String?,
    var uuid: String?,
    var zaak: String,
    @JsonProperty("object")
    var objectUrl: String?,
    var objectType: String,
    var objectTypeOverige: String?,
    var relatieomschrijving: String?,
    var objectidentificatie: JsonNode?,
)
