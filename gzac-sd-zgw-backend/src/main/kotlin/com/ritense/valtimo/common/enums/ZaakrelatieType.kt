package com.ritense.valtimo.common.enums

enum class ZaakrelatieType(val relatie: String) {
    // De andere zaak gaf aanleiding tot het starten van de onderhanden zaak.
    VERVOLG("vervolg"),
    // De andere zaak is relevant voor cq. is onderwerp van de onderhanden zaak.
    ONDERWERP("onderwerp"),
    // Aan het bereiken van de uitkomst van de andere zaak levert de onderhanden zaak een bijdrage.
    BIJDRAGE("bijdrage")
}
