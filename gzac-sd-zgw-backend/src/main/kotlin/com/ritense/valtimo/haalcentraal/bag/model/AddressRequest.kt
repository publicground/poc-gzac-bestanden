package com.ritense.valtimo.haalcentraal.bag.model

data class AddressRequest(
    val postcode: String,
    val huisnummer: Int,
    val huisnummertoevoeging: String?,
    val huisletter: String?,
    val exacteMatch: Boolean,
)
