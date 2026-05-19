package com.ritense.valtimo.ooievaarspas.mapper

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.MissingNode
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ritense.authorization.AuthorizationContext.Companion.runWithoutAuthorization
import com.ritense.document.service.DocumentService
import com.ritense.valtimo.common.service.DocumentReaderService
import com.ritense.valtimo.ooievaarspas.domain.AandelenObligatie
import com.ritense.valtimo.ooievaarspas.domain.Aanvraaggegevens
import com.ritense.valtimo.ooievaarspas.domain.Aanvrager
import com.ritense.valtimo.ooievaarspas.domain.AanwezigeDocumenten
import com.ritense.valtimo.ooievaarspas.domain.Adres
import com.ritense.valtimo.ooievaarspas.domain.Bankrekening
import com.ritense.valtimo.ooievaarspas.domain.BedrijfsDocumenten
import com.ritense.valtimo.ooievaarspas.domain.Bezittingen
import com.ritense.valtimo.ooievaarspas.domain.CryptoCurrency
import com.ritense.valtimo.ooievaarspas.domain.Inkomen
import com.ritense.valtimo.ooievaarspas.domain.Kind
import com.ritense.valtimo.ooievaarspas.domain.OoievaarspasAanvraag
import com.ritense.valtimo.ooievaarspas.domain.Partner
import com.ritense.valtimo.ooievaarspas.domain.Pensioen
import com.ritense.valtimo.ooievaarspas.domain.Schuld
import com.ritense.valtimo.ooievaarspas.domain.Uitkering
import com.ritense.valtimo.ooievaarspas.domain.Uitkeringen
import com.ritense.valtimo.ooievaarspas.domain.Vermogen
import com.ritense.valtimo.ooievaarspas.domain.WelkVermogen
import com.ritense.valtimo.ooievaarspas.domain.Werkgever
import org.springframework.web.util.HtmlUtils.htmlUnescape

class OoievaarspasAanvraagMapper(
    private val documentService: DocumentService,
    private val documentReaderService: DocumentReaderService
) {

    fun mapAanvragInDocumentByBusinessKey(businessKey: String) {

        documentReaderService.getDocumentById(businessKey)
            .apply {
                val ooievaarspasAanvraag = mapAanvraag(
                    this.content().asJson()
                )
                val mappedAanvraag: JsonNode = jacksonObjectMapper().convertValue(
                    ooievaarspasAanvraag
                )

                runWithoutAuthorization {
                    documentService.modifyDocument(
                        this,
                        mappedAanvraag
                    )
                }
            }
    }

    private fun mapAanvraag(jsonNode: JsonNode): OoievaarspasAanvraag {

        return jsonNode.let {
            val persoonsgegevens: JsonNode? = when (val digidNode = it.at("/naw-uitgebreid-digid/digid")) {
                is MissingNode -> null
                else -> digidNode
            } ?: when (val anoniemNode = it.at("/naw-uitgebreid-digid/anoniem")) {
                is MissingNode -> null
                else -> anoniemNode
            }

            OoievaarspasAanvraag(
                Aanvrager(
                    bsn = persoonsgegevens?.getNodeValueByPathOrNull("/persoongegevensPrefill/BsnPrefill")
                        ?: persoonsgegevens?.getNodeValueByPathOrNull("/persoonsgegevens/Bsn"),
                    voornaam = persoonsgegevens?.getNodeValueByPathOrNull("/persoongegevensPrefill/voornaamPrefill")
                        ?: persoonsgegevens?.getNodeValueByPathOrNull("/persoonsgegevens/voornaam"),
                    tussenvoegsel = persoonsgegevens?.getNodeValueByPathOrNull("/persoongegevensPrefill/voorvoegselPrefill")
                        ?: persoonsgegevens?.getNodeValueByPathOrNull("/persoonsgegevens/voorvoegsels"),
                    achternaam = persoonsgegevens?.getNodeValueByPathOrNull("/persoongegevensPrefill/achternaamPrefill")
                        ?: persoonsgegevens?.getNodeValueByPathOrNull("/persoonsgegevens/achternaam"),
                    voorletters = persoonsgegevens?.getNodeValueByPathOrNull("/persoongegevensPrefill/voorlettersPrefill")
                        ?: persoonsgegevens?.getNodeValueByPathOrNull("/persoonsgegevens/voorletters"),
                    geboortedatum = persoonsgegevens?.getNodeValueByPathOrNull("/persoongegevensPrefill/geboortedatumPrefill")
                        ?: persoonsgegevens?.getNodeValueByPathOrNull("/persoonsgegevens/geboortedatum"),
                    adres = Adres(
                        straat = persoonsgegevens?.getNodeValueByPathOrNull("/adresgegevensPrefillGroep/straatPrefill")
                            ?: persoonsgegevens?.getNodeValueByPathOrNull("/adresgegevens/straatnaam"),
                        huisnummer = persoonsgegevens?.getNodeValueByPathOrNull("/adresgegevensPrefillGroep/huisnummerPrefill")
                            ?: persoonsgegevens?.getNodeValueByPathOrNull("/adresgegevens/huisnummer"),
                        huisnummertoevoeging = persoonsgegevens?.getNodeValueByPathOrNull("/adresgegevensPrefillGroep/toevoegingPrefill")
                            ?: persoonsgegevens?.getNodeValueByPathOrNull("/adresgegevens/toevoegingHuisnummer"),
                        huisletter = persoonsgegevens?.getNodeValueByPathOrNull("/adresgegevensPrefillGroep/HuisletterPrefill")
                            ?: persoonsgegevens?.getNodeValueByPathOrNull("/adresgegevens/huisletter"),
                        postcode = persoonsgegevens?.getNodeValueByPathOrNull("/adresgegevensPrefillGroep/postcodePrefill")
                            ?: persoonsgegevens?.getNodeValueByPathOrNull("/adresgegevens/postcode"),
                        woonplaats = unescapeHtml(persoonsgegevens?.getNodeValueByPathOrNull("/adresgegevensPrefillGroep/plaatsPrefill")
                            ?: persoonsgegevens?.getNodeValueByPathOrNull("/adresgegevens/plaats"))
                    ),
                    telefoonnummer = it.getNodeValueByPathOrNull("/naw-uitgebreid-digid/contactgegevens/Telefoonnummer"),
                    emailadres = it.getNodeValueByPathOrNull("/naw-uitgebreid-digid/contactgegevens/email"),
                ),
                partner = Partner(
                    bsn = it.getNodeValueByPathOrNull("/ooievaarspas-gezinssamenstelling/uwGezinssituatie/gegevensVanUwPartner/burgerservicenummerPartner"),
                    geboortedatum = it.getNodeValueByPathOrNull("/ooievaarspas-gezinssamenstelling/uwGezinssituatie/gegevensVanUwPartner/geboortedatumPartner"),
                    voorletters = it.getNodeValueByPathOrNull("/ooievaarspas-gezinssamenstelling/uwGezinssituatie/gegevensVanUwPartner/voorletterPartner"),
                    tussenvoegsel = it.getNodeValueByPathOrNull("/ooievaarspas-gezinssamenstelling/uwGezinssituatie/gegevensVanUwPartner/voorvoegselPartner"),
                    achternaam = it.getNodeValueByPathOrNull("/ooievaarspas-gezinssamenstelling/uwGezinssituatie/gegevensVanUwPartner/achternaamPartner"),
                    identiteitsbewijs = it.getNodeValueByPathOrNull("/ooievaarspas-gezinssamenstelling/uwGezinssituatie/gegevensVanUwPartner/idPartner")
                ),
                kinderen = it.getNodeValueByPathOrNull<List<JsonNode>?>("/ooievaarspas-gezinssamenstelling/uwGezinssituatie/gegevensVanUwKinderen/kinderen")
                    ?.map {
                        Kind(
                            voorletters = it.getNodeValueByPathOrNull("/voorletterKind"),
                            voornaam = it.getNodeValueByPathOrNull("/voornamenKind"),
                            tussenvoegsel = it.getNodeValueByPathOrNull("/voorvoegselKind"),
                            achternaam = it.getNodeValueByPathOrNull("/achternaamKind"),
                            bsn = it.getNodeValueByPathOrNull("/burgerservicenummerKind"),
                            geboortedatum = it.getNodeValueByPathOrNull("/geboortedatumKind")
                        )
                    }
                    ?: listOf(),
                aanvraaggegevens = Aanvraaggegevens(
                    heeftBijstandsuitkering = it.getNodeValueByPathOrNull<String?>("/ooievaarspas-uw-situatie/OntvangtBijstandsuitkering")
                        .asJaNeeOrNvt(),
                    heeftSchuldhulpverlening = it.getNodeValueByPathOrNull<String?>("/ooievaarspas-uw-situatie/inSchuldhulpverlening")
                        .asJaNeeOrNvt(),
                    heeftInwonendePartner = it.getNodeValueByPathOrNull<String?>("/ooievaarspas-gezinssamenstelling/uwGezinssituatie/inwonendePartner")
                        .asJaNeeOrNvt(),
                    heeftInwonendeKinderen = it.getNodeValueByPathOrNull<String?>("/ooievaarspas-gezinssamenstelling/uwGezinssituatie/inwonendeKinderen18")
                        .asJaNeeOrNvt(),
                    aanvragerIsStudent = it.getNodeValueByPathOrNull<String?>("/ooievaarspas-gezinssamenstelling/uwStudiesituatie/HOStudent")
                        .asJaNeeOrNvt(),
                    partnerIsStudent = it.getNodeValueByPathOrNull<String?>("/ooievaarspas-gezinssamenstelling/uwStudiesituatie/HOStudentPartner")
                        .asJaNeeOrNvt(),
                    aanvragerIsOndernemer = it.getNodeValueByPathOrNull<String?>("/ooievaarspas-uw-inkomsten/ondernemer/bentUOndernemer")
                        .asJaNeeOrNvt(),
                    heeftHuurinkomsten = it.getNodeValueByPathOrNull<String?>("/ooievaarspas-uw-inkomsten/inkomenUitOnderhuur/heeftUInkomenUitOnderhuur")
                        .asJaNeeOrNvt(),
                    heeftWerkgever = it.getNodeValueByPathOrNull<String?>("/ooievaarspas-uw-inkomsten/heeftUEenWerkgever")
                        .asJaNeeOrNvt(),
                    heeftPensioen = it.getNodeValueByPathOrNull<String?>("/ooievaarspas-uw-inkomsten/pensioenenVeldenGroep/krijgtUPensioen")
                        .asJaNeeOrNvt(),
                    heeftKinderalimentatie = it.getNodeValueByPathOrNull<String?>("/ooievaarspas-uw-inkomsten/alimentatie/krijgtUKinderalimentatie")
                        .asJaNeeOrNvt(),
                    partnerIsOndernemer = it.getNodeValueByPathOrNull<String?>("/ooievaarspas-inkomsten-partner/OndernemerPartner/isUwPartnerOndernemer")
                        .asJaNeeOrNvt(),
                    heeftPartnerHuurinkomsten = it.getNodeValueByPathOrNull<String?>("/ooievaarspas-inkomsten-partner/inkomenUitOnderhuurPartner/heeftUwPartnerInkomenUitOnderhuur")
                        .asJaNeeOrNvt(),
                    heeftPartnerWerkgever = it.getNodeValueByPathOrNull<String?>("/ooievaarspas-inkomsten-partner/inkomstenVanUwPartner/heeftUwPartnerEenWerkgever")
                        .asJaNeeOrNvt(),
                    heeftPartnerPensioen = it.getNodeValueByPathOrNull<String?>("/ooievaarspas-inkomsten-partner/pensioenenPartnerVeldenGroep/krijgtUwPartnerPensioen")
                        .asJaNeeOrNvt(),
                    heeftPartnerKinderalimentatie = it.getNodeValueByPathOrNull<String?>("/ooievaarspas-inkomsten-partner/alimentatiePartnerGroep/krijgtUwPartnerKinderAlimentatie")
                        .asJaNeeOrNvt()
                ),
                inkomenKlant = Inkomen(
                    werkgevers = it.getNodeValueByPathOrNull<List<JsonNode>?>("/ooievaarspas-uw-inkomsten/loondienstWerkgevers/werkgevers")
                        ?.map {
                            Werkgever(
                                naam = it.getNodeValueByPathOrNull("/naamWerkgever"),
                                periode = it.getNodeValueByPathOrNull("/periodeNettoLoon"),
                                nettoloon = it.getNodeValueByPathOrNull("/nettoLoon")
                            )
                        }
                        ?: listOf(),
                    uitkeringen = Uitkeringen(
                        aow = Uitkering(
                            actief = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/uitkeringenVeldenGroep/uitkeringen/aow"),
                            naam = "AOW",
                            periode = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/inkomenGroepAOW/periodeBedragAOW"),
                            nettobedrag = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/inkomenGroepAOW/nettoBedragAOW")
                        ),
                        anw = Uitkering(
                            actief = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/uitkeringenVeldenGroep/uitkeringen/anw"),
                            naam = "ANW",
                            periode = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/inkomenGroepANW/periodeBedragANW"),
                            nettobedrag = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/inkomenGroepANW/nettoBedragANW")
                        ),
                        bijstand = Uitkering(
                            actief = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/uitkeringenVeldenGroep/uitkeringen/bijstand"),
                            naam = "Bijstand",
                            periode = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/inkomenGroepBijstand/periodeBedragBijstand"),
                            nettobedrag = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/inkomenGroepBijstand/nettoBedragBijstand")
                        ),
                        wao = Uitkering(
                            actief = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/uitkeringenVeldenGroep/uitkeringen/wao"),
                            naam = "WAO",
                            periode = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/inkomenGroepWAO/periodeBedragWAO"),
                            nettobedrag = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/inkomenGroepWAO/nettoBedragWAO")
                        ),
                        ww = Uitkering(
                            actief = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/uitkeringenVeldenGroep/uitkeringen/ww"),
                            naam = "WW",
                            periode = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/inkomenGroepWW/periodeBedragWW"),
                            nettobedrag = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/inkomenGroepWW/nettoBedragWW")
                        ),
                        wajong = Uitkering(
                            actief = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/uitkeringenVeldenGroep/uitkeringen/wajong"),
                            naam = "Wajong",
                            periode = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/inkomenGroepWajong/periodeBedragWajong"),
                            nettobedrag = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/inkomenGroepWajong/nettoBedragWajong")
                        ),
                        wia = Uitkering(
                            actief = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/uitkeringenVeldenGroep/uitkeringen/wia"),
                            naam = "WIA",
                            periode = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/inkomenGroepWIA/periodeBedragWIA"),
                            nettobedrag = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/inkomenGroepWIA/nettoBedragWIA")
                        ),
                        ziektewet = Uitkering(
                            actief = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/uitkeringenVeldenGroep/uitkeringen/ziektewet"),
                            naam = "Ziektewet",
                            periode = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/inkomenGroepZiektewet/periodeBedragZiektewet"),
                            nettobedrag = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/inkomenGroepZiektewet/nettoBedragZiektewet")
                        )
                    ),
                    pensioenen = it.getNodeValueByPathOrNull<List<JsonNode>?>("/ooievaarspas-uw-inkomsten/pensioenenVeldenGroep/pensioenenGroep/pensioenen")
                        ?.map {
                            Pensioen(
                                nettobedrag = it.getNodeValueByPathOrNull("/nettoPensioen")
                            )
                        }
                        ?: listOf(),
                    nettoKinderalimentatie = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/alimentatie/nettoKinderalimentatie"),
                    bedragOnderhuur = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/inkomenUitOnderhuur/bedragOnderhuur"),
                    bedrijfsDocumenten = BedrijfsDocumenten(
                        aanwezigeDocumenten = AanwezigeDocumenten(
                            jaarrekening = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/ondernemer/uploadBedrijfsDocumenten/a"),
                            balans = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/ondernemer/uploadBedrijfsDocumenten/b"),
                            aangifte = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/ondernemer/uploadBedrijfsDocumenten/c"),
                            aanslag = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/ondernemer/uploadBedrijfsDocumenten/d"),
                            afschriften = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/ondernemer/uploadBedrijfsDocumenten/e")
                        ),
                        jaarrekening = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/ondernemer/jaarrekening"),
                        balans = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/ondernemer/balans"),
                        aangifte = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/ondernemer/aangifte"),
                        aanslag = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/ondernemer/aanslag"),
                        afschriften = it.getNodeValueByPathOrNull("/ooievaarspas-uw-inkomsten/ondernemer/afschriften")
                    )
                ),
                inkomenPartner = Inkomen(
                    werkgevers = it.getNodeValueByPathOrNull<List<JsonNode>?>("/ooievaarspas-inkomsten-partner/loondienstWerkgeverSPartner/werkgeversPartner")
                        ?.map {
                            Werkgever(
                                naam = it.getNodeValueByPathOrNull("/naamWerkgeverPartner"),
                                periode = it.getNodeValueByPathOrNull("/periodeNettoLoonPartner"),
                                nettoloon = it.getNodeValueByPathOrNull("/nettoLoonPartner")
                            )
                        }
                        ?: listOf(),
                    uitkeringen = Uitkeringen(
                        aow = Uitkering(
                            actief = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/uitkeringenPartnerVeldenGroep/uitkeringenPartner/aow"),
                            naam = "AOW",
                            periode = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/uitkeringenPartnerVeldenGroep/aowPartner/periodeBedragAOWPartner"),
                            nettobedrag = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/uitkeringenPartnerVeldenGroep/aowPartner/nettoBedragAOWPartner")
                        ),
                        anw = Uitkering(
                            actief = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/uitkeringenPartnerVeldenGroep/uitkeringenPartner/anw"),
                            naam = "ANW",
                            periode = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/uitkeringenPartnerVeldenGroep/ANWPartner/periodeBedragANWPartner"),
                            nettobedrag = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/uitkeringenPartnerVeldenGroep/ANWPartner/nettoBedragANWPartner")
                        ),
                        bijstand = Uitkering(
                            actief = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/uitkeringenPartnerVeldenGroep/uitkeringenPartner/bijstand"),
                            naam = "Bijstand",
                            periode = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/uitkeringenPartnerVeldenGroep/BijstandPartner/periodeBedragBijstandPartner"),
                            nettobedrag = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/uitkeringenPartnerVeldenGroep/BijstandPartner/nettoBedragBijstandPartner")
                        ),
                        wao = Uitkering(
                            actief = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/uitkeringenPartnerVeldenGroep/uitkeringenPartner/wao"),
                            naam = "WAO",
                            periode = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/uitkeringenPartnerVeldenGroep/WAOPartner/periodeBedragWAOPartner"),
                            nettobedrag = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/uitkeringenPartnerVeldenGroep/WAOPartner/nettoBedragWAOPartner")
                        ),
                        ww = Uitkering(
                            actief = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/uitkeringenPartnerVeldenGroep/uitkeringenPartner/ww"),
                            naam = "WW",
                            periode = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/uitkeringenPartnerVeldenGroep/WWPartner/periodeBedragWWPartner"),
                            nettobedrag = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/uitkeringenPartnerVeldenGroep/WWPartner/nettoBedragWWPartner")
                        ),
                        wajong = Uitkering(
                            actief = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/uitkeringenPartnerVeldenGroep/uitkeringenPartner/wajong"),
                            naam = "Wajong",
                            periode = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/uitkeringenPartnerVeldenGroep/WajongPartner/periodeBedragWajongPartner"),
                            nettobedrag = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/uitkeringenPartnerVeldenGroep/WajongPartner/nettoBedragWajongPartner")
                        ),
                        wia = Uitkering(
                            actief = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/uitkeringenPartnerVeldenGroep/uitkeringenPartner/wia"),
                            naam = "WIA",
                            periode = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/uitkeringenPartnerVeldenGroep/WIAPartner/periodeBedragWIAPartner"),
                            nettobedrag = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/uitkeringenPartnerVeldenGroep/WIAPartner/nettoBedragWIAPartner")
                        ),
                        ziektewet = Uitkering(
                            actief = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/uitkeringenPartnerVeldenGroep/uitkeringenPartner/ziektewet"),
                            naam = "Ziektewet",
                            periode = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/uitkeringenPartnerVeldenGroep/ZiektewetPartner/periodeBedragZiektewetPartner"),
                            nettobedrag = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/uitkeringenPartnerVeldenGroep/ZiektewetPartner/nettoBedragZiektewetPartner")
                        )
                    ),
                    pensioenen = it.getNodeValueByPathOrNull<List<JsonNode>?>("/ooievaarspas-inkomsten-partner/pensioenenPartnerVeldenGroep/PensioenPartner/pensioenenPartner")
                        ?.map {
                            Pensioen(
                                nettobedrag = it.getNodeValueByPathOrNull("/nettoPensioenPartner")
                            )
                        }
                        ?: listOf(),
                    nettoKinderalimentatie = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/alimentatiePartnerGroep/nettoKinderalimentatiePartner"),
                    bedragOnderhuur = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/inkomenUitOnderhuurPartner/bedragOnderhuurPartner"),
                    bedrijfsDocumenten = BedrijfsDocumenten(
                        aanwezigeDocumenten = AanwezigeDocumenten(
                            jaarrekening = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/OndernemerPartner/uploadBedrijfsDocumentenPartner/a"),
                            balans = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/OndernemerPartner/uploadBedrijfsDocumentenPartner/b"),
                            aangifte = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/OndernemerPartner/uploadBedrijfsDocumentenPartner/c"),
                            aanslag = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/OndernemerPartner/uploadBedrijfsDocumentenPartner/d"),
                            afschriften = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/OndernemerPartner/uploadBedrijfsDocumentenPartner/e")
                        ),
                        jaarrekening = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/OndernemerPartner/JaarrekeningPartner"),
                        balans = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/OndernemerPartner/BalansPartner"),
                        aangifte = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/OndernemerPartner/AangiftePartner"),
                        aanslag = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/OndernemerPartner/AanslagPartner"),
                        afschriften = it.getNodeValueByPathOrNull("/ooievaarspas-inkomsten-partner/OndernemerPartner/AfschriftPartner")
                    )
                ),
                vermogen = Vermogen(
                    welkVermogen = WelkVermogen(
                        bankrekeningen = it.getNodeValueByPathOrNull("/ooievaarspas-bezit-en-schulden/uwVermogenGroep/welkVermogen/a"),
                        aandelenObligaties = it.getNodeValueByPathOrNull("/ooievaarspas-bezit-en-schulden/uwVermogenGroep/welkVermogen/b"),
                        crypto = it.getNodeValueByPathOrNull("/ooievaarspas-bezit-en-schulden/uwVermogenGroep/welkVermogen/c"),
                        contanten = it.getNodeValueByPathOrNull("/ooievaarspas-bezit-en-schulden/uwVermogenGroep/welkVermogen/d")
                    ),
                    bezittingen = Bezittingen(
                        bankrekeningen = it.getNodeValueByPathOrNull<List<JsonNode>?>("/ooievaarspas-bezit-en-schulden/rekeningGroep/rekeningen")
                            ?.map {
                                Bankrekening(
                                    iban = it.getNodeValueByPathOrNull("/iban"),
                                    tegoed = it.getNodeValueByPathOrNull("/saldo"),
                                    documenten = it.getNodeValueByPathOrNull("/uploadAfschrift")
                                )
                            }
                            ?: listOf(),
                        aandelenObligaties = it.getNodeValueByPathOrNull<List<JsonNode>?>("/ooievaarspas-bezit-en-schulden/obligatieGroep/obligaties")
                            ?.map {
                                AandelenObligatie(
                                    waarde = it.getNodeValueByPathOrNull("/waardeObligatie"),
                                    documenten = it.getNodeValueByPathOrNull("/fileObligatie")
                                )
                            }
                            ?: listOf(),
                        cryptoCurrencies = it.getNodeValueByPathOrNull<List<JsonNode>?>("/ooievaarspas-bezit-en-schulden/cryptoGroep/crypto")
                            ?.map {
                                CryptoCurrency(
                                    waarde = it.getNodeValueByPathOrNull("/waardeCrypto"),
                                    documenten = it.getNodeValueByPathOrNull("/filecrypto")
                                )
                            }
                            ?: listOf(),
                        contantGeld = it.getNodeValueByPathOrNull("/ooievaarspas-bezit-en-schulden/contanten/contantGeld")
                    ),
                    schulden = it.getNodeValueByPathOrNull<List<JsonNode>?>("/ooievaarspas-bezit-en-schulden/schuldenGroep/vragenSchulden/schulden")
                        ?.map {
                            Schuld(
                                bedrag = it.getNodeValueByPathOrNull("/schuld"),
                                schuldeiser = it.getNodeValueByPathOrNull("/bijWieHeeftUSchuldOfBetalingsachterstand"),
                                documenten = it.getNodeValueByPathOrNull("/fileBetalingsachterstand")
                            )
                        }
                        ?: listOf()
                ),
                postOntvangen = it.getNodeValueByPathOrFalse("/post-ooievaarspas/postOntvangen")
            )
        }
    }

    private inline fun <reified T> JsonNode.getNodeValueByPathOrNull(path: String): T? {
        return when (val nodeAtPath = this.at(path)) {
            is MissingNode -> null
            else -> jacksonObjectMapper().convertValue(nodeAtPath)
        }
    }

    private fun JsonNode.getNodeValueByPathOrFalse(path: String): Boolean {
        return when (val nodeAtPath = this.at(path)) {
            is MissingNode -> false
            else -> jacksonObjectMapper().convertValue(nodeAtPath)
        }
    }

    /**
     * Attempt to convert this [String] into a [Boolean].
     * @return [Boolean] value corresponding to this [String] or null.
     */
    private fun String.asBooleanOrNull(): Boolean? {
        return when (this) {
            "ja", "j" -> true
            "nee", "n" -> false
            else -> null
        }
    }

    private fun String?.asJaNeeOrNvt(): String {
        return when (this) {
            "ja", "j" -> "ja"
            "nee", "n" -> "nee"
            else -> "nvt"
        }
    }

    private fun unescapeHtml(escapedInput: String?): String {
        if (escapedInput != null) {
            return htmlUnescape(escapedInput)
        }
        return ""
    }
}