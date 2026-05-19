package com.ritense.valtimo.common.enums

enum class ZaakResultaat(
    val resultaat: String
) {
    AFGEBROKEN("Afgebroken"),
    DEELNAME_AFGEBROKEN("Deelname afgebroken"),
    GEANNULEERD("Geannuleerd"),
    GELEVERD("Geleverd"),
    GEWEIGERD("Geweigerd"),
    NIET_GELEVERD("Niet geleverd"),
    TRAINING_GEANNULEERD("Training geannuleerd"),
    TRAINING_GECREEERD("Training gecreëerd"),
    TRAINING_GELEVERD("Training geleverd"),
    TRAINING_VERSTREKT("Training verstrekt"),
    VERSTREKT("Verstrekt")
}