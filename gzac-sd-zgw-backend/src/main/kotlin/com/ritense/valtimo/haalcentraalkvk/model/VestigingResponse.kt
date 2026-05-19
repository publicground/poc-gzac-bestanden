package com.ritense.valtimo.haalcentraalkvk.model

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class VestigingResponse(
    val vestigingLocatie: Locatie?,
    val datumAanvang: DatumAanvang?,
    val activiteiten: List<VestigingActiviteit>?,
    val telefoonnummer: List<String>?,
    val wordtUitgeoefendDoor: WordtUitgeoefendDoor?,
    val typeringVestiging: String?,
    val _links: VestigingLinks?,
    val vestigingsnummer: String?,
    val handelsnamen: List<String>,
    val postLocatie: Locatie?,
    val bezoekLocatie: Locatie?
)

//@JsonInclude(JsonInclude.Include.NON_NULL)
//data class VestigingLocatie(
//    val identificatiecodeNummeraanduiding: String?,
//    val straatnaam: String?,
//    val huisnummer: Int?,
//    val huisletter: String?,
//    val postcode: String?,
//    val woonplaatsnaam: String?,
//    val datumAanvang: DatumAanvang?
//)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class DatumAanvang(
    val datum: String?,
    val jaar: Int?,
    val maand: Int?,
    val dag: Int?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class VestigingActiviteit(
    val code: String?,
    val omschrijving: String?,
    val isHoofdactiviteit: Boolean?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class WordtUitgeoefendDoor(
    val heeftAlsEigenaar: HeeftAlsEigenaar?,
    val wordtGeleidVanuit: WordtGeleidVanuit?,
    val kvkNummer: String?,
    val naam: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class HeeftAlsEigenaar(
    val rechtsvorm: String,
    val natuurlijkPersoon: VestigingNatuurlijkPersoon?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class VestigingNatuurlijkPersoon(
    val burgerservicenummer: String,
    val naam: VestigingNaam,
    val geboorte: VestigingGeboorte,
    val geslachtsaanduiding: String
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class VestigingNaam(
    val geslachtsnaam: String,
    val voornamen: String
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class VestigingGeboorte(
    val datum: DatumAanvang?,
    val plaats: String,
    val land: VestigingLand
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class VestigingLand(
    val code: String,
    val omschrijving: String
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class WordtGeleidVanuit(
    val vestigingsnummer: String,
    val handelsnamen: List<String>
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class VestigingLinks(
    val self: Href,
    val maatschappelijkeActiviteit: Href,
    val eigenaar: Href
)

data class Href(
    val href: String
)
