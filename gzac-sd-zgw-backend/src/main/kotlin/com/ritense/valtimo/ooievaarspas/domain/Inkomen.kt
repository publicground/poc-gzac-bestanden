package com.ritense.valtimo.ooievaarspas.domain

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Inkomen(

    var pensioenen: List<Pensioen> = arrayListOf(),
    var werkgevers: List<Werkgever> = arrayListOf(),
    var uitkeringen: Uitkeringen? = null,
    var bedrijfsDocumenten: BedrijfsDocumenten? = null,
    var bedragOnderhuur: Float? = null,
    var nettoKinderalimentatie: Float? = null
)