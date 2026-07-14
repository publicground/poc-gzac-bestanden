package com.ritense.valtimo.ooievaarspas.domain

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Uitkeringen(
    var aow: Uitkering? = null,
    var anw: Uitkering? = null,
    var bijstand: Uitkering? = null,
    var wao: Uitkering? = null,
    var ww: Uitkering? = null,
    var wajong: Uitkering? = null,
    var wia: Uitkering? = null,
    var ziektewet: Uitkering? = null
)