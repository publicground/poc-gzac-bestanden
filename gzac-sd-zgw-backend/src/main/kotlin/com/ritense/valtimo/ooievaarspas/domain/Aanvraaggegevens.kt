package com.ritense.valtimo.ooievaarspas.domain

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Aanvraaggegevens(

    var heeftBijstandsuitkering: String = "nvt",
    var heeftSchuldhulpverlening: String = "nvt",
    var heeftInwonendePartner: String = "nvt",
    var heeftInwonendeKinderen: String = "nvt",
    var aanvragerIsStudent: String = "nvt",
    var aanvragerIsOndernemer: String = "nvt",
    var partnerIsStudent: String = "nvt",
    var partnerIsOndernemer: String = "nvt",
    var heeftHuurinkomsten: String = "nvt",
    var heeftPartnerHuurinkomsten: String = "nvt",
    var heeftWerkgever: String = "nvt",
    var heeftPartnerWerkgever: String = "nvt",
    var heeftPensioen: String = "nvt",
    var heeftPartnerPensioen: String = "nvt",
    var heeftKinderalimentatie: String = "nvt",
    var heeftPartnerKinderalimentatie: String = "nvt",
    var heeftSchulden: String = "nvt"
)