package com.ritense.valtimo.waarderingmantelzorgersenvrijwilligers.domain

import java.net.URI

data class ZaakType(
    val url: URI,
    val omschrijving: String,
    val omschrijvingGeneriek: String? = null
)