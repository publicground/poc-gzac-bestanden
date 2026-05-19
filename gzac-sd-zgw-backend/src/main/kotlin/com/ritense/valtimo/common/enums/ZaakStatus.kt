package com.ritense.valtimo.common.enums

enum class ZaakStatus(
    val status: String
) {
    AANGEMELD("Aangemeld"),
    AANVRAAG_INGEDIEND("Aanvraag ingediend"),
    AANVRAAG_ONTVANGEN("Aanvraag ontvangen"),
    AANWEZIG("Aanwezig"),
    AFGEHANDELD("Afgehandeld"),
    AFGEROND("Afgerond"),
    AFWEZIG("Afwezig"),
    GEANNULEERD("Geannuleerd"),
    GEPLAND("Gepland"),
    GEREGISTREERD("Geregistreerd"),
    GESLOTEN_VOOR_INSCHRIJVINGEN("Gesloten voor inschrijving"),
    INGETROKKEN("Ingetrokken"),
    INVALID("Invalid"),
    IN_BEHANDELING_GENOMEN("In behandeling genomen"),
    IN_BEHANDELING("In behandeling"),
    INFORMATIEVERZOEK_UITGEZET("Informatieverzoek uitgezet"),
    NIET_AFGEROND("Niet afgerond"),
    OPEN_VOOR_INSCHRIJVINGEN("Open voor inschrijving"),
    VASTLEGGEN_BESLUIT("Vastleggen besluit"),
    IN_AFWACHTING_VAN_WEBSHOP("in afwachting van webshop"),
    GEEN_ANVULLENDE_INFORMATIE_ONTVANGEN("Geen aanvullende informatie ontvangen"),
    ANVULLENDE_INFORMATIE_ONTVANGEN("Aanvullende informatie ontvangen"),
}