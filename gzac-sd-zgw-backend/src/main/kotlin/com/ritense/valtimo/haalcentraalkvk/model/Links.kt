package com.ritense.valtimo.haalcentraalkvk.model

data class Links(
    val self: Link,
    val eigenaar: Link?,
    val vestigingen: List<Link>?,
    val aansprakelijken: List<Link>?,
    val bestuursfuncties: List<Link>?,
    val functionarissenBijzondereRechtstoestand: List<Link>?,
    val gemachtigden: List<Link>?,
    val publiekrechtelijkeFunctionarissen: List<Link>?,
    val overigeFunctionarissen: List<Link>?,
    val maatschappelijkeActiviteit: Href?
)

data class Link(
    val href: String,
    val templated: Boolean,
    val title: String?
)