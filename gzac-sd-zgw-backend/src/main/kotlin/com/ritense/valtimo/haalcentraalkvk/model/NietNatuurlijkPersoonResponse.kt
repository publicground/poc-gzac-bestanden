package com.ritense.valtimo.haalcentraalkvk.model

data class NietNatuurlijkPersoonResponse(
    val rsin: String?,
    val naamgeving: String?,
    val bezoekLocatie: Locatie?,
    val datumAanvang: Datum?,
    val datumEinde: Datum?,
    val rechtsvorm: String?,
    val aansprakelijken: List<Aansprakelijke>?,
    val bestuursfuncties: List<Bestuursfunctie>?,
    val functionarissenBijzondereRechtstoestand: List<Functionaris>?,
    val gemachtigden: List<Gemachtigde>?,
    val publiekrechtelijkeFunctionarissen: List<Functionaris>?,
    val overigeFunctionarissen: List<Functionaris>?,
    val _links: Links?
)

data class Aansprakelijke(
    val natuurlijkPersoon: NatuurlijkPersoon?,
    val nietNatuurlijkPersoon: NietNatuurlijkPersoon?,
    val naamPersoon: NaamPersoon?,
    val functietitel: String?,
    val functie: String?,
    val bevoegdheid: String?,
    val bevoegdheidOmschrijving: String?
)

data class Gemachtigde(
    val natuurlijkPersoon: NatuurlijkPersoon?,
    val nietNatuurlijkPersoon: NietNatuurlijkPersoon?,
    val naamPersoon: NaamPersoon?,
    val functietitel: String?,
    val functie: String?,
    val volmachtType: String?
)
