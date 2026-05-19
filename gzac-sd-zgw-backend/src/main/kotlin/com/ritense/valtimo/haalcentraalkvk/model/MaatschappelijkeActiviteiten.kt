package com.ritense.valtimo.haalcentraalkvk.model

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class HcMaatschappelijkeActiviteiten(
    val kvkNummer: String,
    val naam: String,
    val heeftAlsEigenaar: Eigenaar?,
    val activiteiten: List<Activiteit>?,
    val manifesteertZichAls: ManifesteertZichAls?,
    val wordtGeleidVanuit: Vestiging?,
    val wordtUitgeoefendIn: List<Vestiging>?,
    val datumAanvang: Datum?,
    val datumEinde: Datum?,
    val _links: Links
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Eigenaar(
    val natuurlijkPersoon: NatuurlijkPersoon?,
    val nietNatuurlijkPersoon: NietNatuurlijkPersoon?,
    val rechtsvorm: String,
    val aansprakelijken: List<Aansprakelijk>?,
    val bestuursfuncties: List<Bestuursfunctie>?,
    val functionarissenBijzondereRechtstoestand: List<Functionaris>?,
    val gemachtigden: List<Functionaris>?,
    val publiekrechtelijkeFunctionarissen: List<Functionaris>?,
    val overigeFunctionarissen: List<Functionaris>?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class NatuurlijkPersoon(
    val burgerservicenummer: String?,
    val naam: Naam?,
    val geboorte: Geboorte?,
    val geslachtsaanduiding: String
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class NietNatuurlijkPersoon(
    val rsin: String?,
    val naamgeving: String?
)

data class Naam(
    val geslachtsnaam: String?,
    val voornamen: String?,
    val voorvoegsel: String?
)

data class Geboorte(
    val datum: Datum?,
    val plaats: String?,
    val land: Land?
)

data class Datum(
    val dag: Int?,
    val datum: String?,
    val jaar: Int?,
    val maand: Int?
)

data class Land(
    val code: String?,
    val waarde: String?,
    val omschrijving: String?
)

data class Aansprakelijk(
    val natuurlijkPersoon: NatuurlijkPersoon?,
    val nietNatuurlijkPersoon: NietNatuurlijkPersoon?,
    val naamPersoon: NaamPersoon?,
    val functietitel: String?,
    val functie: String?,
    val bevoegdheid: String?,
    val bevoegdheidOmschrijving: String?
)

data class Bestuursfunctie(
    val natuurlijkPersoon: NatuurlijkPersoon?,
    val nietNatuurlijkPersoon: NietNatuurlijkPersoon?,
    val naamPersoon: NaamPersoon?,
    val functietitel: String?,
    val functie: String?,
    val bevoegdheid: String?,
    val bevoegdheidOmschrijving: String?,
    val wordtVertegenwoordigdDoor: NatuurlijkPersoon?,
    val monistischeBestuurderRol: String?
)

data class Functionaris(
    val natuurlijkPersoon: NatuurlijkPersoon?,
    val nietNatuurlijkPersoon: NietNatuurlijkPersoon?,
    val naamPersoon: NaamPersoon?,
    val functietitel: String?,
    val functie: String?,
    val bevoegdheid: String?,
    val bevoegdheidOmschrijving: String?,
    val volmachtType: String?
)

data class NaamPersoon(
    val naam: String?
)

data class Activiteit(
    val code: String?,
    val omschrijving: String?,
    val isHoofdactiviteit: Boolean?
)

data class ManifesteertZichAls(
    val handelsnamen: List<String>?,
    val activiteiten: List<Activiteit>?,
    val wordtUitgeoefendIn: List<Vestiging>?
)

data class Vestiging(
    val vestigingsnummer: String?,
    val handelsnamen: List<String>?,
    val bezoekLocatie: Locatie?,
    val postLocatie: Locatie?
)

data class BuitenlandsAdres(
    val land: String?,
    val straatHuisnummer: String?,
    val postcodeWoonplaats: String?,
    val regio: String?
)
