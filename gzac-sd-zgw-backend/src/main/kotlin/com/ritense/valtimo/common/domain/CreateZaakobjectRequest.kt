package com.ritense.valtimo.common.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CreateZaakobjectRequest(
    @JsonProperty("zaak")
    val zaakUrl: String,
    @JsonProperty("object")
    val objectUrl: String,
    val objectType: String,
    val objectTypeOverige: String?
)