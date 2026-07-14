<#ftl output_format="HTML">
<!DOCTYPE html>
<html lang="nl">
<head>
    <meta charset="UTF-8"/>
    <title>Samenvatting aanvraag PW/IOAW levensonderhoud</title>

    <style>
        @page {
            size: A4;
            margin: 3cm 2cm 3cm 2cm;

            @top-center { content: element(page-header); }
            @bottom-center { content: element(page-footer); }
        }

        #page-header {
            position: running(page-header);
            font-size: 12px;
            border-bottom: 1px solid #999;
            padding-bottom: 5px;
            line-height: 50px;
            height: 50px;
        }

        .timestamp {
            float: right;
        }

        #page-footer {
            position: running(page-footer);
            font-size: 10px;
            border-top: 1px solid #999;
            padding-top: 5px;
            text-align: center;
        }

        #page-footer .page-count:before {
            content: "Pagina " counter(page) " van " counter(pages);
        }

        .onderwerp {
            width: 100%;
            background-color: #e8f0f8;
            color: #154273;
            border-left: 4px solid #154273;
            padding: 10px 12px;
            margin-bottom: 20px;
            font-weight: bold;
        }

        .sectie { margin-bottom: 18px; }

        .sectie h2 {
            font-size: 9pt;
            font-weight: bold;
            color: #000000;
            margin-bottom: 8px;
            text-transform: uppercase;
            letter-spacing: .3px;
        }

        .persoon-table {
            width: 100%;
            border-collapse: collapse;
        }

        .persoon-table td {
            padding: 2px 0;
            vertical-align: top;
        }

        .label {
            font-weight: bold;
            width: 40%;
        }

        .value {
            width: 60%;
        }

        .table {
            width: 100%;
            border-collapse: collapse;
            border: 1px solid #154273;
            margin-top: 8px;
        }

        .table th, .table td {
            border: 1px solid #154273;
            padding: 6px;
        }

        .table thead th {
            background-color: #e8f0f8;
            color: #154273;
        }

        .divider {
            background-color: #e8f0f8;
            color: #154273;
        }

        .style-multiline {
            white-space: pre-wrap;
        }

        .float-right {
            float: right;
        }

        table.table {
            page-break-inside: avoid;
        }
    </style>
</head>

<body>
<#assign brp = (pv.brpPersoonsgegevensInfo)!{} />
<#assign duoPersoonsInformatie = (pv.duoPersoonsInformatie)!{} />

<#assign gv                         = pv.gegevensVaststelling!{} />
<#assign dossierType                = pv.dossierType!{} />
<#assign aanvrager                  = (gv.aanvrager)!{} />
<#assign partner                    = (gv.partner)!{} />
<#assign vermogen                   = (gv.vermogen)!{} />
<#assign inkomstenSamenvattingAanvrager = (gv.inkomstenAanvrager)!{} />
<#assign inkomstenAanvrager         = (inkomstenSamenvattingAanvrager.inkomstenSamenvatting)!{} />
<#assign vrijlatingAanvrager         = (gv.inkomstenAanvrager.vrijlating)!{} />
<#assign inkomstenSamenvattingPartner = (gv.inkomstenPartner)!{} />
<#assign inkomstenPartner           = (inkomstenSamenvattingPartner.inkomstenSamenvatting)!{} />
<#assign vrijlatingPartner          = (gv.inkomstenPartner.vrijlating)!{} />
<#assign leefsituatie               = (gv.leefsituatie)!{} />
<#assign woonsituatie               = (gv.woonsituatie)!{} />
<#assign uitbetaling                = (gv.uitbetaling)!{} />
<#assign redenAanvraag              = (gv.redenAanvraag)!{} />
<#assign beschikbaarheidAanvrager   = (gv.beschikbaarheidAanvrager)!{} />
<#assign beschikbaarheidPartner     = (gv.beschikbaarheidPartner)!{} />
<#assign additioneleVeldenAanvrager = (gv.additioneleVeldenAanvrager)!{} />
<#assign additioneleVeldenPartner   = (gv.additioneleVeldenPartner)!{} />
<#assign additioneleVeldenAanvraag  = (gv.additioneleVeldenAanvraag)!{} />
<#assign uitgebreideMotivering = (gv.uitgebreideMotivering.motivering)!'' />
<#assign vermogensComponenten = pv.vermogensComponenten!{} />
<#assign contactpersoon = (gv.contactpersoon)!{} />
<#assign vastleggenVoorschot  = (gv.vastleggenVoorschot)![] />
<#assign voorschot   = (vastleggenVoorschot?has_content)?then(vastleggenVoorschot?last, {}) />
<#assign zaaknummer  = (pv.zaaknummer)!{} />
<#assign besluiten = (pv.besluiten)![] />
<#assign besluit   = (besluiten?has_content)?then(besluiten?last, {}) />
<#assign besluitDienst = (besluit.dienst![])?has_content?then(besluit.dienst?first, {}) />
<#assign verstrekkingsvormType = (besluitDienst.leveringsopdracht![])?has_content?then((besluitDienst.leveringsopdracht?first.verstrekkingsvorm.type)!"", "") />

<#assign partnerExist = ((aanvrager.heeftPartner!'') == 'JA')?then(true, false) />

<#assign redenAanvraag = (gv.redenAanvraag)!{} />
<#-- Voorliggende voorzieningen en uitsluitingsgronden komen uit het eerste besluit (in behandeling genomen) -->
<#assign besluitInBehandeling = (besluiten?has_content)?then(besluiten?first, {}) />
<#assign besluitInBehandelingDienst = (besluitInBehandeling.dienst![])?has_content?then(besluitInBehandeling.dienst?first, {}) />
<#assign aanvragerBeoordeling = (besluitInBehandelingDienst.aanvrager)!{} />
<#assign partnerBeoordeling   = (besluitInBehandelingDienst.partner)!{} />
<#assign aanvragerVoorliggendeVoorzieningen = (aanvragerBeoordeling.voorliggendeVoorzieningen)![] />
<#assign aanvragerUitsluitingsgronden       = (aanvragerBeoordeling.uitsluitingsgronden)![] />
<#assign partnerVoorliggendeVoorzieningen   = (partnerBeoordeling.voorliggendeVoorzieningen)![] />
<#assign partnerUitsluitingsgronden         = (partnerBeoordeling.uitsluitingsgronden)![] />

<#assign verstrekkingsvorm = {
"ON": "Uitkering om niet",
"RLL": "Lening",
"KH": "Krediethypotheek"
} />
<#assign genomenBesluit = {
"TOEGEKEND": "Toegekend",
"AFGEWEZEN": "Afgewezen",
"INGETROKKEN": "Ingetrokken",
"BUITEN_BEHANDELING_GESTELD": "Buiten behandeling gesteld"
} />
<#assign jaNeeCodes = {
"1": "Ja",
"2": "Nee"
} />
<#assign jaNee = {
"JA": "Ja",
"NEE": "Nee"
} />
<#assign wijzeUitbetaling = {
"VOLLEDIG_AANVRAGER": "Volledig op mijn rekening",
"VERDEELD_AANVRAGER_PARTNER": "Verdeeld over mijzelf en mijn partner",
"VOLLEDIG_PARTNER": "Volledig op rekening van mijn partner",
"DERDE": "Volledig op rekening van een derde partij"
}/>

<#assign relatieTypes = {
"partner":  "Partner",
"exPartner": "Ex-partner",
"ouderKind": "Ouder Kind",
"kindOuder":  "Kind Ouder",
"medebewoner": "Medebewoner"
}/>

<#assign vrijlatingLabels = {
"VERLENGING_LKS_15": "Verlenging vrijlating 15% op inkomen bij LKS",
"ALLEENSTAANDE_OUDER_KIND_15": "Vrijlating op inkomsten all.ouder+kind<12jaar",
"LKS_15": "Vrijlating 15% op inkomen bij LKS",
"MEDISCH_15": "Vrijlating 15% op inkomsten medisch",
"ARBEID_25": "Vrijlating 25% op inkomsten uit arbeid"
} />

<#assign redenGewensteIngangsdatum = {
"WACHTEN_BESLISSING_INSTANTIE": "Wachten beslissing instantie",
"OPNAME_INSTELLING": "Opname instelling",
"WACHTEN_DIGID": "Wachten DigiD",
"ANDERE_REDEN": "Andere reden afwijkende startdatum"
} />

<#assign fullName =
((brp.voornamen)!'')
+ ((brp.voorvoegsel)?has_content?then(' ' + brp.voorvoegsel,''))
+ ((brp.achternaam)?has_content?then(' ' + brp.achternaam,''))
/>
<#function showDate d>
    <#if d?has_content>
        <#if d?contains("-")>
            <#return d?date("yyyy-MM-dd")?string("dd-MM-yyyy") />
        <#else>
            <#return d?date("yyyyMMdd")?string("dd-MM-yyyy") />
        </#if>
    <#else>
        <#return "-" />
    </#if>
</#function>

<div id="page-header">Samenvatting aanvraag PW/IOAW levensonderhoud&nbsp;<span class="timestamp">${.now?string("dd-MM-yyyy HH:mm:ss")}</span></div>
<div id="page-footer"><span class="page-count"></span></div>

<div class="sectie">
    <table class="table">
        <tr class="divider"><td colspan="2">Samenvatting aanvraag PW/IOAW levensonderhoud</td></tr>
        <tr>
            <td class="label">Zaaknummer</td>
            <td>${zaaknummer}</td>
        </tr>
        <tr class="even">
            <td class="label">Naam aanvrager</td>
            <td>
                <#if aanvrager??>
                    ${aanvrager.voorletters!""} ${aanvrager.voorvoegsel!""} ${aanvrager.achternaam!""}
                    <#if aanvrager.voornamen??> (${aanvrager.voornamen})</#if>
                </#if>
            </td>
        </tr>
        <#if partnerExist>
            <tr>
                <td class="label">Naam partner</td>
                <td>
                    ${partner.voorletters!""} ${partner.voorvoegsel!""} ${partner.achternaam!""}
                    <#if partner.voornamen??> (${partner.voornamen})</#if>
                </td>
            </tr>
        </#if>
        <tr class="divider"><td></td><td></td></tr>
        <tr class="even">
            <td class="label">Aanvraagdatum</td>
            <td>${gv.startDatum?has_content?then(showDate(gv.startDatum), '-')}</td>
        </tr>
        <tr>
            <td class="label">Meldingsdatum</td>
            <td>${gv.meldingsDatum?has_content?then(showDate(gv.meldingsDatum), '-')}</td>
        </tr>
        <tr class="even">
            <td class="label">Reden aanvraag</td>
            <td>
                <#if redenAanvraag?? && redenAanvraag.reden??>
                    ${redenAanvraag.reden.name!""}
                </#if>
            </td>
        </tr>
        <tr class="divider"><td></td><td></td></tr>
        <tr>
            <td class="label">Besluit</td>
            <td>${genomenBesluit[(besluit.genomenBesluit)!'']!'-'}</td>
        </tr>
        <tr class="even">
            <td class="label">Datum besluit</td>
            <td>${showDate(besluit.besluitDatum)}</td>
        </tr>
        <tr>
            <td class="label">Naam beslisser</td>
            <td>${besluit.beslisser!""}</td>
        </tr>
        <#if besluit.genomenBesluit == 'TOEGEKEND'>
            <tr class="even">
                <td class="label">Ingangsdatum</td>
                <td>
                    <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.bs221ingangsdatum??>
                        ${additioneleVeldenAanvrager.bs221ingangsdatum.ingangsdatum?has_content?then(additioneleVeldenAanvrager.bs221ingangsdatum.ingangsdatum?split("-")?reverse?join("-"), '-')}
                    </#if>
                </td>
            </tr>
            <tr>
                <td class="label">Uitkeringsnorm</td>
                <td>
                    <#if additioneleVeldenAanvraag?? && additioneleVeldenAanvraag.bs219norm??>
                        ${additioneleVeldenAanvraag.bs219norm.basisnorm!""}
                    <#else>
                        Alleenstaande / Gezamenlijk / Kostendelers
                    </#if>
                </td>
            </tr>
            <tr class="even">
                <td class="label">Aantal rechthebbenden</td>
                <td>
                    <#if additioneleVeldenAanvraag?? && additioneleVeldenAanvraag.bs219norm??>
                        ${additioneleVeldenAanvraag.bs219norm.aantalRechthebbenden!""}
                    </#if>
                </td>
            </tr>
            <tr>
                <td class="label">Aantal kostendelers</td>
                <td>
                    <#if additioneleVeldenAanvraag?? && additioneleVeldenAanvraag.bs219norm??>
                        ${additioneleVeldenAanvraag.bs219norm.aantalKostendelers!""}
                    </#if>
                </td>
            </tr>
            <tr class="even">
                <td class="label">Hoogte (gezamenlijke) norm</td>
                <td>
                    <#if additioneleVeldenAanvraag?? && additioneleVeldenAanvraag.bs219norm??>
                        &#8364; ${additioneleVeldenAanvraag.bs219norm.hoogteNorm?string("0.00")!""}
                    </#if>
                </td>
            </tr>
            <tr>
                <td class="label">Bijstandsvorm</td>
                <td>${verstrekkingsvorm[verstrekkingsvormType]!"-"}</td>
            </tr>
            <tr class="even">
                <td class="label">Maatregel van toepassing</td>
                <td></td>
            </tr>
            <#if additioneleVeldenAanvraag?? && additioneleVeldenAanvraag.bs219norm?? && additioneleVeldenAanvraag.bs219norm.toelichting?has_content>
                <tr>
                    <td class="label">Toelichting</td>
                    <td>${additioneleVeldenAanvraag.bs219norm.toelichting}</td>
                </tr>
            </#if>
        </#if>
        <#if besluit.genomenBesluit == 'AFGEWEZEN'>
            <tr class="even">
                <td class="label">Reden afwijzen</td>
                <td>${besluit.redenAfwijzing!""}</td>
            </tr>
        </#if>
        <#if besluit.genomenBesluit == 'INGETROKKEN'>
            <tr class="even">
                <td class="label">Hoe is de intrekking ontvangen</td>
                <td>${besluit.intrekking.ingetrokkenVia!""}</td>
            </tr>
            <tr class="even">
                <td class="label">Datum intrekking</td>
                <td>${besluit.intrekking.datumIngetrokken?has_content?then(showDate(besluit.intrekking.datumIngetrokken), '-')}</td>
            </tr>
        </#if>
        <#if besluit.genomenBesluit == 'BUITEN_BEHANDELING_GESTELD'>
            <tr class="even">
                <td class="label">Datum informatie verzoek uitgezet</td>
                <td>${besluit.buitenBehandelingStellen.datumUitgezet?has_content?then(showDate(besluit.buitenBehandelingStellen.datumUitgezet), '-')}</td>
            </tr>
            <tr class="even">
                <td class="label">Uiterlijke inleverdatum</td>
                <td>${besluit.buitenBehandelingStellen.datumUitgezet?has_content?then(showDate(besluit.buitenBehandelingStellen.datumUitgezet), '-')}</td>
            </tr>
            <tr class="even">
                <td class="label">Ontbrekende stukken</td>
                <td>${besluit.buitenBehandelingStellen.ontbrekendeStukken!""}</td>
            </tr>
            <#if besluit.buitenBehandelingStellen.toelichting?has_content>
                <tr class="even">
                    <td class="label">Toelichting</td>
                    <td>${besluit.buitenBehandelingStellen.toelichting}</td>
                </tr>
            </#if>
        </#if>
        <tr class="divider"><td></td><td></td></tr>
        <tr>
            <td class="label">Vastgesteld (gezamenlijk) vermogen</td>
            <td>
                <#if vermogen?? && vermogen.berekendVermogen??>
                    &#8364; ${vermogen.berekendVermogen?string("0.00")}
                </#if>
            </td>
        </tr>
        <tr class="even">
            <td class="label">Vastgesteld (gezamenlijk) inkomen</td>
            <td>
                <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.bs224bGezamelijkInkomen??>
                    &#8364; ${additioneleVeldenAanvrager.bs224bGezamelijkInkomen.gezamelijkInkomen?string("0.00")}
                </#if>
            </td>
        </tr>
        <tr class="divider"><td></td><td></td></tr>
        <tr>
            <td class="label">Voorschot uitgekeerd</td>
            <td>${jaNee[(voorschot.voorschotUitkeren)!'']!'-'}</td>
        </tr>
        <tr class="even">
            <td class="label">Datum voorschot verstrekt</td>
            <td>${showDate(voorschot.datumBeoordeeld!"")}</td>
        </tr>
        <#if voorschot.toelichting?has_content>
            <tr>
                <td class="label">Toelichting voorschot</td>
                <td>${voorschot.toelichting}</td>
            </tr>
        </#if>
        <tr class="divider"><td></td><td></td></tr>
        <tr class="even">
            <td class="label">Overmaken aan (derde)</td>
            <td>
                <#if uitbetaling.wijzeVanUitbetaling?has_content>
                    ${jaNeeCodes[(uitbetaling.wijzeVanUitbetaling)!'']!'-'}
                </#if>
            </td>
        </tr>
        <tr>
            <td class="label">IBAN</td>
            <td>
                <#if uitbetaling?? && uitbetaling.aanvrager?? && uitbetaling.aanvrager.bankrekening??>
                    ${uitbetaling.aanvrager.bankrekening.iban!""} &nbsp;
                    (${uitbetaling.aanvrager.bankrekening.tenaamstelling!""})
                </#if>
                <#if uitbetaling?? && uitbetaling.partner?? && uitbetaling.partner.bankrekening??>
                    ${uitbetaling.partner.bankrekening.iban!""} &nbsp;
                    (${uitbetaling.partner.bankrekening.tenaamstelling!""})
                </#if>
                <#if uitbetaling?? && uitbetaling.derden?? && uitbetaling.derden.bankrekening??>
                    ${uitbetaling.derden.bankrekening.iban!""} &nbsp;
                    (${uitbetaling.derden.bankrekening.tenaamstelling!""})
                </#if>
            </td>
        </tr>
    </table>
</div>

<!-- ============================================================ -->
<!-- CONTACT                                                       -->
<!-- ============================================================ -->
<h2>Contact</h2>

<table class="table">
    <tr class="divider"><td colspan="2">Contactgegevens aanvrager</td></tr>
    <tr>
        <td class="label">Telefoonnummer</td>
        <td>
            <#if aanvrager?? && aanvrager.contactgegevens??>
                ${aanvrager.contactgegevens.telefoon!""}
            </#if>
        </td>
    </tr>
    <tr class="even">
        <td class="label">E-mail</td>
        <td>
            <#if aanvrager?? && aanvrager.contactgegevens??>
                ${aanvrager.contactgegevens.email!""}
            </#if>
        </td>
    </tr>
    <tr>
        <td class="label">Communicatievoorkeur</td>
        <td>${aanvrager.communicatievoorkeur!""}</td>
    </tr>
    <tr class="divider"><td colspan="2">Contactgegevens derde</td></tr>
    <tr>
        <td class="label">Naam contactpersoon</td>
        <td>${contactpersoon.naamContactpersoon!"-"}</td>
    </tr>
    <tr class="even">
        <td class="label">Telefoonnummer contactpersoon</td>
        <td>${contactpersoon.telefoon!"-"}</td>
    </tr>
    <tr>
        <td class="label">Relatie tot aanvrager</td>
        <#assign relatieTotAanvragerLabels = {
            "FAMILIE": "Familie",
            "BUURMAN_VROUW": "Buurman / -vrouw",
            "KENNIS": "Kennis",
            "HULPVERLENER": "Hulpverlener",
            "BEWINDVOERDER": "Bewindvoerder",
            "BOEKHOUDER": "Boekhouder"
        } />
        <td>${relatieTotAanvragerLabels[contactpersoon.relatieTotAanvrager!'']!"-"}</td>
    </tr>
</table>

<!-- ============================================================ -->
<!-- VASTGESTELDE GEGEVENS AANVRAGER                              -->
<!-- ============================================================ -->
<h2>Vastgestelde gegevens (${aanvrager.voornamen!""} ${aanvrager.voorvoegsel!""} ${aanvrager.achternaam!""})</h2>

<!-- Persoon -->
<table class="table">
    <tr class="divider">
        <td>Persoon</td>
        <td>
            <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.vastleggingPersoon??>
                <#if additioneleVeldenAanvrager.vastleggingPersoon.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                (${showDate(additioneleVeldenAanvrager.vastleggingPersoon.datumVastgesteld)})
            <#else>Nog vast te stellen
            </#if>
        </td>
    </tr>
    <tr class="odd">
        <td class="label">BSN</td>
        <td>${aanvrager.bsn!""}</td>
    </tr>
    <tr class="even">
        <td class="label">Naam</td>
        <td>${aanvrager.voorletters!""} ${aanvrager.voorvoegsel!""} ${aanvrager.achternaam!""}</td>
    </tr>
    <tr class="odd">
        <td class="label">Geboortedatum</td>
        <td>${aanvrager.geboortedatum?has_content?then(aanvrager.geboortedatum?split("-")?reverse?join("-"), '-')}</td>
    </tr>
    <tr class="even">
        <td class="label">Beschikbaarheid (u/week)</td>
        <td>
            <#if beschikbaarheidAanvrager??>
                ${beschikbaarheidAanvrager.uurPerWeek!""}
            </#if>
        </td>

    </tr>
    <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.vastleggingPersoon?? && additioneleVeldenAanvrager.vastleggingPersoon.toelichtingConsulent?has_content>
        <tr class="odd">
            <td class="label">Toelichting</td>
            <td>${additioneleVeldenAanvrager.vastleggingPersoon.toelichtingConsulent}</td>
        </tr>
    </#if>
</table>

<!-- Adres -->
<h2>Adres</h2>
<table class="table">
    <tr class="divider">
        <td>Adres</td>
        <td>
            <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.verblijfadres??>
                <#if additioneleVeldenAanvrager.verblijfadres.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                (${showDate(additioneleVeldenAanvrager.verblijfadres.datumVastgesteld)})
            <#else>Nog vast te stellen
            </#if>
        </td>
    </tr>
    <tr class="odd">
        <td class="label">Verblijfadres</td>
        <td>
            <#if aanvrager?? && aanvrager.verblijfadres??>
                ${aanvrager.verblijfadres.straatnaam!""} ${aanvrager.verblijfadres.huisnummer!""}${aanvrager.verblijfadres.huisletter!""},
                ${aanvrager.verblijfadres.postcode!""} ${aanvrager.verblijfadres.woonplaatsnaam!""}
            </#if>
        </td>
    </tr>
    <tr class="even">
        <td class="label">Correspondentieadres</td>
        <td></td>
    </tr>
    <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.verblijfadres?? && additioneleVeldenAanvrager.verblijfadres.toelichtingConsulent?has_content>
        <tr class="odd">
            <td class="label">Toelichting</td>
            <td>${additioneleVeldenAanvrager.verblijfadres.toelichtingConsulent}</td>
        </tr>
    </#if>
</table>

<!-- Nationaliteit -->
<table class="table">
    <tr class="divider">
        <td>Nationaliteit / Verblijfstatus</td>
        <td>
            <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.bs213verblijfstitel??>
                <#if additioneleVeldenAanvrager.bs213verblijfstitel.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                (${showDate(additioneleVeldenAanvrager.bs213verblijfstitel.datumVastgesteld)})
            <#else>Nog vast te stellen
            </#if>
        </td>
    </tr>
    <tr class="odd">
        <td class="label">Nationaliteit</td>
        <td>
            <#if aanvrager?? && aanvrager.nationaliteiten?? && aanvrager.nationaliteiten?has_content>
                <#list aanvrager.nationaliteiten as nat>
                    ${nat.name!""}<#if nat?has_next>, </#if>
                </#list>
            </#if>
        </td>
    </tr>
    <tr class="even">
        <td class="label">Verblijfstitel / -code</td>
        <td>
            <#if aanvrager?? && aanvrager.verblijfstitel?? && aanvrager.verblijfstitel.codeVerblijfstitel?? && aanvrager.verblijfstitel.codeVerblijfstitel.name?has_content && aanvrager.verblijfstitel.codeVerblijfstitel.code?has_content>
                ${aanvrager.verblijfstitel.codeVerblijfstitel.name} (${aanvrager.verblijfstitel.codeVerblijfstitel.code})
            <#else>
                —
            </#if>
        </td>
    </tr>
    <tr class="odd">
        <td class="label">Gemeenschapsrecht</td>
        <td>
            <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.bs213verblijfstitel??>
                <#if additioneleVeldenAanvrager.bs213verblijfstitel.isSprakeVanGemeenschapsrecht??>
                    ${additioneleVeldenAanvrager.bs213verblijfstitel.isSprakeVanGemeenschapsrecht?string("Ja","Nee")}
                <#else>—</#if>
            </#if>
        </td>
    </tr>
    <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.bs213verblijfstitel?? && additioneleVeldenAanvrager.bs213verblijfstitel.toelichtingConsulent?has_content>
        <tr class="even">
            <td class="label">Toelichting</td>
            <td>${additioneleVeldenAanvrager.bs213verblijfstitel.toelichtingConsulent}</td>
        </tr>
    </#if>
</table>

<!-- Voorliggende voorzieningen aanvrager -->
<table class="table">
    <tr class="divider">
        <td colspan="2">Voorliggende voorzieningen</td>
        <td>
            <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.bs444VoorliggendeVoorzieningen??>
                <#if additioneleVeldenAanvrager.bs444VoorliggendeVoorzieningen.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                (${showDate(additioneleVeldenAanvrager.bs444VoorliggendeVoorzieningen.datumVastgesteld)})
            <#else>Nog vast te stellen
            </#if>
        </td>
    </tr>
    <tr>
        <th>Signaal</th>
        <th>Voorziening</th>
        <th>Omschrijving</th>
    </tr>
    <#assign aanvragerVoorliggendeVoorzieningenZichtbaar = aanvragerVoorliggendeVoorzieningen?filter(v -> (v.mogelijkRecht!"") != "Niet beoordeeld") />
    <#if aanvragerVoorliggendeVoorzieningenZichtbaar?has_content>
        <#list aanvragerVoorliggendeVoorzieningenZichtbaar as voorziening>
            <tr class="${voorziening?is_odd_item?string("odd","even")}">
                <td><#if voorziening.signalering??>${voorziening.signalering?string("Ja","Nee")}<#else>-</#if></td>
                <td>${voorziening.voorziening!"-"}</td>
                <td>${voorziening.mogelijkRecht!"-"}</td>
            </tr>
            <#if voorziening.toelichting?has_content>
                <tr class="${voorziening?is_odd_item?string("odd","even")}">
                    <td class="label">Toelichting</td>
                    <td colspan="2" class="style-multiline">${voorziening.toelichting}</td>
                </tr>
            </#if>
        </#list>
    <#else>
        <tr><td colspan="3">Geen voorliggende voorzieningen</td></tr>
    </#if>
</table>

<!-- Uitsluitingsgronden aanvrager -->
<table class="table">
    <tr class="divider">
        <td colspan="2">Uitsluitingsgronden</td>
        <td>
            <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.bs218Uitsluitingsgronden??>
                <#if additioneleVeldenAanvrager.bs218Uitsluitingsgronden.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                (${showDate(additioneleVeldenAanvrager.bs218Uitsluitingsgronden.datumVastgesteld)})
            <#else>Nog vast te stellen
            </#if>
        </td>
    </tr>
    <tr>
        <th>Signaal</th>
        <th>Uitsluitingsgrond</th>
        <th>Omschrijving</th>
    </tr>
    <#assign aanvragerUitsluitingsgrondenZichtbaar = aanvragerUitsluitingsgronden?filter(u -> (u.uitsluiting!"") != "Niet Bepaald") />
    <#if aanvragerUitsluitingsgrondenZichtbaar?has_content>
        <#list aanvragerUitsluitingsgrondenZichtbaar as uitsluitingsgrond>
            <tr class="${uitsluitingsgrond?is_odd_item?string("odd","even")}">
                <td><#if uitsluitingsgrond.signalering??>${uitsluitingsgrond.signalering?string("Ja","Nee")}<#else>-</#if></td>
                <td>${uitsluitingsgrond.uitsluitingsgrond!"-"}</td>
                <td>${uitsluitingsgrond.uitsluiting!"-"}</td>
            </tr>
            <#if uitsluitingsgrond.toelichting?has_content>
                <tr class="${uitsluitingsgrond?is_odd_item?string("odd","even")}">
                    <td class="label">Toelichting</td>
                    <td colspan="2" class="style-multiline">${uitsluitingsgrond.toelichting}</td>
                </tr>
            </#if>
        </#list>
    <#else>
        <tr><td colspan="3">Geen uitsluitingsgronden</td></tr>
    </#if>
</table>

<!-- Woon- en leefsituatie -->
<table class="table">
    <tr class="divider">
        <td>Woon- en leefsituatie</td>
        <td>
            <#if additioneleVeldenAanvraag?? && additioneleVeldenAanvraag.leefEnWoonsituatie??>
                <#if additioneleVeldenAanvraag.leefEnWoonsituatie.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                (${showDate(additioneleVeldenAanvraag.leefEnWoonsituatie.datumVastgesteld)})
            <#else>Nog vast te stellen
            </#if>
        </td>
    </tr>
    <tr class="odd">
        <td class="label">Woonsituatie</td>
        <#assign woonsituatieLabels = {
            "ZELFSTANDIGE_HUURWONING": "Zelfstandige huurwoning",
            "HUURCONTRACT": "Huurcontract",
            "WOON_IN": "Woon in",
            "KOOPWONING": "Koopwoning",
            "WOONWAGEN": "Woonwagen",
            "ZORGINSTELLING": "Zorginstelling",
            "MAATSCHAPPELIJKE_OPVANG": "Maatschappelijke opvang",
            "ZELFSTANDIG_WONEND": "Zelfstandig wonend",
            "VERBLIJF_INSTELLING": "Verblijf instelling"
        } />
        <td>${woonsituatieLabels[woonsituatie.woonsituatie!'']!(woonsituatie.woonsituatie!'')}</td>
    </tr>
    <tr class="even">
        <td class="label">Leefsituatie</td>
        <#assign leefsituatieLabels = {
            "INSTITUTIONEEL_HUISHOUDEN": "Institutioneel huishouden",
            "ALLEENSTAANDE_OUDER": "Alleenstaande ouder",
            "ALLEENSTAAND": "Alleenstaand",
            "SAMENWONEND_KINDEREN": "Samenwonend met kinderen",
            "SAMENWONEND": "Samenwonend"
        } />
        <td>${leefsituatieLabels[leefsituatie.leefsituatie!'']!(leefsituatie.leefsituatie!'')}</td>
    </tr>
    <tr class="odd">
        <td class="label">Medebewoners op adres</td>
        <td>${medebewonersOpAdres!""}</td>
    </tr>
    <tr class="even">
        <td class="label">Thuiswonende kinderen</td>
        <td>${aanvrager.heeftThuiswonendeKinderen!""}</td>
    </tr>
    <#if additioneleVeldenAanvraag?? && additioneleVeldenAanvraag.leefEnWoonsituatie?? && additioneleVeldenAanvraag.leefEnWoonsituatie.toelichtingLeefsituatie?has_content>
        <tr class="odd">
            <td class="label">Toelichting</td>
            <td>${additioneleVeldenAanvraag.leefEnWoonsituatie.toelichtingLeefsituatie}</td>
        </tr>
    </#if>
</table>

<!-- Relaties aanvrager -->
<table class="table">
    <tr class="divider">
        <td>Relaties</td>
        <td colspan="2">
            <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.bs216relaties??>
                <#if additioneleVeldenAanvrager.bs216relaties.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                (${showDate(additioneleVeldenAanvrager.bs216relaties.datumVastgesteld)})
            <#else>Nog vast te stellen
            </#if>
        </td>
    </tr>
    <#if aanvrager?? && aanvrager.relaties?? && aanvrager.relaties?has_content>
        <#list aanvrager.relaties as relatie>
            <tr class="${relatie?is_odd_item?string("odd","even")}">
                <td>Naam</td>
                <td>${relatie.voornamen!""} ${relatie.tussenvoegsel!""} ${relatie.achternaam!""}</td>
                <td rowspan="7">
                    <ul>
                        <#if (relatie.woontOpHetzelfdeAdresAlsAanvrager)!false>
                            <li>Woont op hetzelfde adres</li>
                        </#if>
                        <#if (relatie.indicatieGezamelijkeHuishouding)!false>
                            <li>Is onderdeel van gezamelijke huishouding</li>
                        </#if>
                        <#if (relatie.indicatieOnderhoudsplichtPartner)!false>
                            <li>Is onderhoudsplichtig</li>
                        </#if>
                        <#if (relatie.indicatieCoOuder)!false>
                            <li>Is co-ouder</li>
                        </#if>
                        <#if (relatie.indicatieOnderhoudsplichtOuderKind)!false>
                            <li>Is onderhoudsplichtig</li>
                        </#if>
                        <#if (relatie.isKindEenMedebewoner)!false>
                            <li>Is ook medebewoner</li>
                        </#if>
                        <#if (relatie.afgelopen2JaarPartnerGeweest)!false>
                            <li>Is afgelopen 2 jaar partner geweest</li>
                        </#if>
                        <#if (relatie.samenEenErkendKindHebben)!false>
                            <li>Heeft samen een kind</li>
                        </#if>
                        <#if (relatie.samenlevingscontract)!false>
                            <li>Heeft een samenlevingscontract</li>
                        </#if>
                        <#if (relatie.afgelopenTweeJaarEeeHuishouden)!false>
                            <li>Was afgelopen 2 jaar één huishouden</li>
                        </#if>
                        <#if (relatie.delenKostenHuur)!false>
                            <li>Deelt kosten huur</li>
                        </#if>
                        <#if (relatie.delenVasteLasten)!false>
                            <li>Deelt vaste lasten</li>
                        </#if>
                        <#if (relatie.delenkostenBoodschappen)!false>
                            <li>Deelt kosten boodschappen</li>
                        </#if>
                        <#if (relatie.samenEten)!false>
                            <li>Eet samen</li>
                        </#if>
                        <#if (relatie.samenOpVakantie)!false>
                            <li>Gaat samen op vakantie</li>
                        </#if>
                        <#if (relatie.samenTakenHuishouden)!false>
                            <li>Deelt taken huishouden</li>
                        </#if>
                        <#if (relatie.tijdelijkVerblijf)!false>
                            <li>Betreft een tijdelijke verblijf</li>
                        </#if>
                        <#if (relatie.indicatieZorgbehoefte)!false>
                            <li>Heeft een zorgbehoefte</li>
                        </#if>
                        <#if (relatie.kamerHuur)!false>
                            <li>Heeft een commerciële relatie</li>
                        </#if>
                        <#if (relatie.leeftijdOnder27)!false>
                            <li>Leeftijd onder 27</li>
                        </#if>
                        <#if (relatie.studiefinanciering)!false>
                            <li>Studiefinanciering</li>
                        </#if>
                    </ul>
                </td>
            </tr>
            <tr class="${relatie?is_odd_item?string("odd","even")}">
                <td>Begindatum</td>
                <td>${relatie.datumAanvangRelatie?has_content?then(showDate(relatie.datumAanvangRelatie), '-')}</td>
            </tr>
            <tr class="${relatie?is_odd_item?string("odd","even")}">
                <td>Einddatum</td>
                <td>${relatie.datumEindeRelatie?has_content?then(showDate(relatie.datumEindeRelatie), '-')}</td>
            </tr>
            <tr class="${relatie?is_odd_item?string("odd","even")}">
                <td>Geboortedatum</td>
                <td>${relatie.geboortedatum?has_content?then(showDate(relatie.geboortedatum), '-')}</td>
            </tr>
            <tr class="${relatie?is_odd_item?string("odd","even")}">
                <td>Relatietype</td>
                <td>${relatieTypes[(relatie.relatietype)!'']!'-'}</td>
            </tr>
            <#if relatie.relatietype == 'ouderKind'>
                <tr class="${relatie?is_odd_item?string("odd","even")}">
                    <td>Kinderbijslag</td>
                    <td><#if relatie.indicatieKostendeler?? && relatie.indicatieKostendeler><span class="value-yes">Ja</span><#else><span class="value-no">Nee</span></#if></td>
                </tr>
            </#if>
            <tr class="${relatie?is_odd_item?string("odd","even")}">
                <td>Kostendeler</td>
                <td><#if relatie.indicatieKostendeler?? && relatie.indicatieKostendeler><span class="value-yes">Ja</span><#else><span class="value-no">Nee</span></#if></td>
            </tr>
            <#if relatie.toelichtingRelatieTypeAnders??>
                <tr class="${relatie?is_odd_item?string("odd","even")}">
                    <td>Toelichting relatie medebewoner</td>
                    <td>${relatie.toelichtingRelatieTypeAnders!""}</td>
                </tr>
            </#if>
            <#if relatie?has_next>
                <tr class="divider"><td colspan="3"></td></tr>
            </#if>
        </#list>
    <#else>
        <tr><td colspan="3">Er zijn geen relaties vastgesteld</td></tr>
    </#if>
    <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.bs216relaties??>
        <tr>
            <td>Toelichting relaties algemeen</td>
            <td colspan="2" class="toelichting style-multiline">${additioneleVeldenAanvrager.bs216relaties.toelichting!""}</td>
        </tr>
    </#if>
</table>

<!-- ============================================================ -->
<!-- INKOMEN Aanvrager                                            -->
<!-- ============================================================ -->
<#if dossierType == 'aanvraag-algemene-bijstand-dcm'>
    <#if inkomstenAanvrager?has_content>
        <h2>Inkomsten aanvrager</h2>
    </#if>
    <#if inkomstenAanvrager?has_content && inkomstenAanvrager.arbeid?has_content && inkomstenAanvrager.arbeid.inkomstenComponenten?has_content>
        <table  class="table">
            <tr class="divider">
                <td  colspan="2">Inkomsten uit arbeid</td>
                <td  colspan="2">
                    <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.bs224InkomstenAanvrager??>
                        <#if additioneleVeldenAanvrager.bs224InkomstenAanvrager.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                        (${showDate(additioneleVeldenAanvrager.bs224InkomstenAanvrager.datumVastgesteld)})
                    <#else>Nog vast te stellen
                    </#if>
                </td>
            </tr>
            <#list inkomstenAanvrager.arbeid.inkomstenComponenten as inkomsten>
                <tr>
                    <td colspan="2">Naam bedrijf</td>
                    <td colspan="2">${inkomsten.naam!""}</td>
                </tr>
                <tr>
                    <td colspan="2">Loonheffingsnummer</td>
                    <td colspan="2">${inkomsten.loonheffingsnummer!""}</td>
                </tr>
                <tr>
                    <td colspan="2">Soort inkomen</td>
                    <td colspan="2">${inkomsten.primairInkomstencomponentType.name!""}</td>
                </tr>
                <tr>
                    <td colspan="2">Periodiciteit uitbetaling</td>
                    <td colspan="2">${inkomsten.periodiciteitUitbetaling.name!""}</td>
                </tr>
                <tr>
                    <td colspan="2">Soort contract</td>
                    <td colspan="2">${inkomsten.soortContract.name!""}</td>
                </tr>
                <tr>
                    <td colspan="2">
                        Reservering vakantiegeld
                        <span class="float-right">
                        <#if inkomsten.indicatieVakantiegeld!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                    <td colspan="2">
                        Loonbeslag
                        <span class="float-right">
                        <#if inkomsten.indicatieLoonbeslag!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        LHK toegepast
                        <span class="float-right">
                        <#if inkomsten.indicatieHeffingskorting!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                    <td colspan="2">
                        Uren vermindering
                        <span class="float-right">
                        <#if inkomsten.indicatieUrenVermindering!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                </tr>
                <tr><td colspan="4">&nbsp;</td></tr>
                <tr>
                    <td>Begindatum periode</td>
                    <td>Einddatum periode</td>
                    <td>Bruto inkomen</td>
                    <td>Netto inkomen</td>
                </tr>
                <tr>
                    <td>${inkomsten.periodeStartdatum?has_content?then(showDate(inkomsten.periodeStartdatum), '-')}</td>
                    <td>${inkomsten.periodeEinddatum?has_content?then(showDate(inkomsten.periodeEinddatum), '-')}</td>
                    <td><#if inkomsten.brutoInkomsten??>&euro; ${inkomsten.brutoInkomsten?string("0.00")}<#else>-</#if></td>
                    <td><#if inkomsten.nettoInkomsten??>&euro; ${inkomsten.nettoInkomsten?string("0.00")}<#else>-</#if></td>
                </tr>
                <tr><td colspan="4">&nbsp;</td></tr>

                <#if inkomsten.indicatieAVWBBudget>
                    <tr>
                        <td colspan="2">${inkomsten.avwbCategorie.name!""}</td>
                        <td><#if inkomsten.avwbBrutoInkomsten??>&euro; ${inkomsten.avwbBrutoInkomsten?string("0.00")}<#else>-</#if></td>
                        <td><#if inkomsten.avwbNettoInkomsten??>&euro; ${inkomsten.avwbNettoInkomsten?string("0.00")}<#else>-</#if></td>
                    </tr>
                </#if>
                <#if inkomsten.indicatieVakantiegeld>
                    <tr>
                        <td colspan="2">Vakantiegeld</td>
                        <td><#if inkomsten.vakantiegeldBrutoInkomsten??>&euro; ${inkomsten.vakantiegeldBrutoInkomsten?string("0.00")}<#else>-</#if></td>
                        <td><#if inkomsten.vakantiegeldNettoInkomsten??>&euro; ${inkomsten.vakantiegeldNettoInkomsten?string("0.00")}<#else>-</#if></td>
                    </tr>
                </#if>
                <#if inkomsten.indicatieMaaltijdvergoeding>
                    <tr>
                        <td colspan="2">Maaltijdvergoeding</td>
                        <td><#if inkomsten.maaltijdvergoedingBrutoInkomsten??>&euro; ${inkomsten.maaltijdvergoedingBrutoInkomsten?string("0.00")}<#else>-</#if></td>
                        <td><#if inkomsten.maaltijdvergoedingNettoInkomsten??>&euro; ${inkomsten.maaltijdvergoedingNettoInkomsten?string("0.00")}<#else>-</#if></td>
                    </tr>
                </#if>
                <#if inkomsten.indicatieOnkostenvergoeding>
                    <tr>
                        <td colspan="2">Onkostenvergoeding</td>
                        <td><#if inkomsten.onkostenvergoedingBrutoInkomsten??>&euro; ${inkomsten.onkostenvergoedingBrutoInkomsten?string("0.00")}<#else>-</#if></td>
                        <td><#if inkomsten.onkostenvergoedingNettoInkomsten??>&euro; ${inkomsten.onkostenvergoedingNettoInkomsten?string("0.00")}<#else>-</#if></td>
                    </tr>
                </#if>
                <#if inkomsten.indicatieReiskostenvergoeding>
                    <tr>
                        <td colspan="2">Reiskostenvergoeding</td>
                        <td><#if inkomsten.reiskostenvergoedingBrutoInkomsten??>&euro; ${inkomsten.reiskostenvergoedingBrutoInkomsten?string("0.00")}<#else>-</#if></td>
                        <td><#if inkomsten.reiskostenvergoedingNettoInkomsten??>&euro; ${inkomsten.reiskostenvergoedingNettoInkomsten?string("0.00")}<#else>-</#if></td>
                    </tr>
                </#if>
                <#if inkomsten.indicatieVergoedingInNatura>
                    <tr>
                        <td colspan="2">Vergoeding in natura</td>
                        <td><#if inkomsten.naturaBrutoInkomsten??>&euro; ${inkomsten.naturaBrutoInkomsten?string("0.00")}<#else>-</#if></td>
                        <td><#if inkomsten.naturaNettoInkomsten??>&euro; ${inkomsten.naturaNettoInkomsten?string("0.00")}<#else>-</#if></td>
                    </tr>
                </#if>
                <#if inkomsten?has_next>
                    <tr class="divider"><td colspan="4">&nbsp;</td></tr>
                </#if>
            </#list>
        </table>

        <#if (inkomstenAanvrager.arbeid.totaalNettomaandelijkseInkomstenVanEenStap)?has_content>
            <table class="table">
                <tr class="divider">
                    <td>Totaal netto inkomsten uit arbeid</td>
                </tr>
                <tr><td>&euro; ${inkomstenAanvrager.arbeid.totaalNettomaandelijkseInkomstenVanEenStap?string("0.00")}</td></tr>
            </table>
        </#if>

        <table class="table">
            <tr class="divider">
                <td>Toelichting</td>
            </tr>
            <tr><td>${inkomstenAanvrager.arbeid.toelichting!""}</td></tr>
        </table>
    </#if>

    <#if inkomstenAanvrager?has_content && inkomstenAanvrager.uitkeringen?has_content && inkomstenAanvrager.uitkeringen.inkomstenComponenten?has_content>
        <table  class="table">
            <tr class="divider">
                <td  colspan="3">Inkomsten uit uitkering, pensioen en of studiefinanciering </td>
                <td  colspan="3">
                    <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.bs224InkomstenAanvrager??>
                        <#if additioneleVeldenAanvrager.bs224InkomstenAanvrager.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                        (${showDate(additioneleVeldenAanvrager.bs224InkomstenAanvrager.datumVastgesteld)})
                    <#else>Nog vast te stellen
                    </#if>
                </td>
            </tr>
            <#list inkomstenAanvrager.uitkeringen.inkomstenComponenten as inkomsten>
                <tr>
                    <td colspan="3">Naam bedrijf</td>
                    <td colspan="3">${inkomsten.naam!""}</td>
                </tr>
                <tr>
                    <td colspan="3">Loonheffingsnummer</td>
                    <td colspan="3">${inkomsten.loonheffingsnummer!""}</td>
                </tr>
                <tr>
                    <td colspan="3">Soort inkomen</td>
                    <td colspan="3">${inkomsten.primairInkomstencomponentType.name!""}</td>
                </tr>
                <tr>
                    <td colspan="3">Periodiciteit uitbetaling</td>
                    <td colspan="3">${inkomsten.periodiciteitUitbetaling.name!""}</td>
                </tr>
                <tr>
                    <td colspan="3">
                        Reservering vakantiegeld
                        <span class="float-right">
                           <#if inkomsten.indicatieVakantiegeld!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                    <td colspan="3">
                        Beslag
                        <#if (inkomsten.indicatieBeslagUitkering!false) || (inkomsten.indicatieBeslagOpPensioen!false)>Ja<#else>Nee</#if>
                    </td>
                </tr>
                <tr>
                    <td colspan="3">
                        LHK toegepast
                        <span class="float-right">
                        <#if inkomsten.indicatieHeffingskorting!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                    <td colspan="3">
                        Uren vermindering
                        <span class="float-right">
                        <#if inkomsten.indicatieUrenVermindering!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                </tr>
                <tr>
                    <td colspan="3">
                        Toeslag
                        <span class="float-right">
                         <#if inkomsten.indicatieToeslag!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                    <td colspan="3">
                        Verlaging maatregel
                        <span class="float-right">
                          <#if inkomsten.indicatieVerlagingDoorMaatregel!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                </tr>
                <tr>
                    <td colspan="6">
                        Verlaging boete
                        <span class="float-right">
                     <#if inkomsten.indicatieVerlagingDoorBoete!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                </tr>
                <tr><td colspan="6">&nbsp;</td></tr>
                <tr>
                    <td rowspan="2">Begindatum periode</td>
                    <td rowspan="2">Einddatum periode</td>
                    <td colspan="2">Inkomen</td>
                    <td colspan="2">Vakantiegeld</td>
                </tr>
                <tr>
                    <td>Bruto bedrag</td>
                    <td>Netto bedrag</td>
                    <td>Bruto bedrag</td>
                    <td>Netto bedrag</td>
                </tr>
                <tr>
                    <td>${inkomsten.periodeStartdatum?has_content?then(showDate(inkomsten.periodeStartdatum), '-')}</td>
                    <td>${inkomsten.periodeEinddatum?has_content?then(showDate(inkomsten.periodeEinddatum), '-')}</td>
                    <td><#if inkomsten.brutoInkomsten??>&euro; ${inkomsten.brutoInkomsten?string("0.00")}<#else>-</#if></td>
                    <td><#if inkomsten.nettoInkomsten??>&euro; ${inkomsten.nettoInkomsten?string("0.00")}<#else>-</#if></td>
                    <td><#if inkomsten.vakantiegeldBrutoInkomsten??>&euro; ${inkomsten.vakantiegeldBrutoInkomsten?string("0.00")}<#else>-</#if></td>
                    <td><#if inkomsten.vakantiegeldNettoInkomsten??>&euro; ${inkomsten.vakantiegeldNettoInkomsten?string("0.00")}<#else>-</#if></td>
                </tr>
                <#if inkomsten?has_next>
                    <tr class="divider"><td colspan="6">&nbsp;</td></tr>
                </#if>
            </#list>
        </table>
        <#if (inkomstenAanvrager.uitkeringen.totaalNettomaandelijkseInkomstenVanEenStap)?has_content>
            <table class="table">
                <tr class="divider">
                    <td>Totaal netto inkomsten uit uitkeringen, pensioenen en studiefinanciering</td>
                </tr>
                <tr><td>&euro; ${inkomstenAanvrager.uitkeringen.totaalNettomaandelijkseInkomstenVanEenStap?string("0.00")}</td></tr>
            </table>
        </#if>
        <table class="table">
            <tr class="divider">
                <td>Toelichting</td>
            </tr>
            <tr><td>${inkomstenAanvrager.uitkeringen.toelichting!""}</td></tr>
        </table>
    </#if>

    <#if inkomstenAanvrager?has_content && inkomstenAanvrager.onderneming?has_content && inkomstenAanvrager.onderneming.inkomstenComponenten?has_content>
        <table  class="table">
            <tr class="divider">
                <td  colspan="2">Onderneming</td>
                <td  colspan="2">
                    <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.bs224InkomstenAanvrager??>
                        <#if additioneleVeldenAanvrager.bs224InkomstenAanvrager.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                        (${showDate(additioneleVeldenAanvrager.bs224InkomstenAanvrager.datumVastgesteld)})
                    <#else>Nog vast te stellen
                    </#if>
                </td>
            </tr>
            <#list inkomstenAanvrager.onderneming.inkomstenComponenten as inkomsten>
                <tr>
                    <td colspan="2">Naam bedrijf</td>
                    <td colspan="2">${inkomsten.naam!""}</td>
                </tr>
                <tr>
                    <td colspan="2">Soort bedrijf</td>
                    <td colspan="2">${inkomsten.soortBedrijf!""}</td>
                </tr>
                <tr>
                    <td colspan="2">Soort inkomen</td>
                    <td colspan="2">${inkomsten.primairInkomstencomponentType.name!""}</td>
                </tr>
                <tr>
                    <td colspan="2">Periodiciteit uitbetaling</td>
                    <td colspan="2">${inkomsten.periodiciteitUitbetaling.name!""}</td>
                </tr>
                <tr>
                    <td colspan="2">Aantal uren per week</td>
                    <td colspan="2">${inkomsten.urenPerWeek!""}</td>
                </tr>
                <tr>
                    <td colspan="2">
                        Ingeschreven KvK
                        <span class="float-right">
                        <#if inkomsten.indicatieIngeschrevenKvK!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                    <td colspan="2">
                        Recht op zelfstandige aftrek
                        <span class="float-right">
                        <#if inkomsten.indicatieRechtOpZelfstandigenaftrek!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        Toestemming gemeente parttime ondernemen
                        <span class="float-right">
                        <#if inkomsten.indicatieToestemmingGemeenteParttime!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                    <td colspan="2">
                    </td>
                </tr>
                <tr><td colspan="4">&nbsp;</td></tr>
                <tr>
                    <td rowspan="2">Begindatum periode</td>
                    <td rowspan="2">Einddatum periode</td>
                    <td colspan="2">Inkomen</td>
                </tr>
                <tr>
                    <td>Bruto bedrag</td>
                    <td>Netto bedrag</td>
                </tr>
                <tr>
                    <td>${inkomsten.periodeStartdatum?has_content?then(showDate(inkomsten.periodeStartdatum), '-')}</td>
                    <td>${inkomsten.periodeEinddatum?has_content?then(showDate(inkomsten.periodeEinddatum), '-')}</td>
                    <td><#if inkomsten.brutoInkomsten??>&euro; ${inkomsten.brutoInkomsten?string("0.00")}<#else>-</#if></td>
                    <td><#if inkomsten.nettoInkomsten??>&euro; ${inkomsten.nettoInkomsten?string("0.00")}<#else>-</#if></td>
                </tr>
                <#if inkomsten?has_next>
                    <tr class="divider"><td colspan="4">&nbsp;</td></tr>
                </#if>
            </#list>
        </table>
        <#if (inkomstenAanvrager.onderneming.totaalNettomaandelijkseInkomstenVanEenStap)?has_content>
            <table class="table">
                <tr class="divider">
                    <td>Totaal netto inkomsten uit onderneming</td>
                </tr>
                <tr><td>&euro; ${inkomstenAanvrager.onderneming.totaalNettomaandelijkseInkomstenVanEenStap?string("0.00")}</td></tr>
            </table>
        </#if>
        <table class="table">
            <tr class="divider">
                <td>Toelichting</td>
            </tr>
            <tr><td>${inkomstenAanvrager.onderneming.toelichting!""}</td></tr>
        </table>
    </#if>

    <#if inkomstenAanvrager?has_content && inkomstenAanvrager.overigeInkomsten?has_content && inkomstenAanvrager.overigeInkomsten.inkomstenComponenten?has_content>
        <table  class="table">
            <tr class="divider">
                <td  colspan="2">Overige inkomsten</td>
                <td  colspan="2">
                    <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.bs224InkomstenAanvrager??>
                        <#if additioneleVeldenAanvrager.bs224InkomstenAanvrager.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                        (${showDate(additioneleVeldenAanvrager.bs224InkomstenAanvrager.datumVastgesteld)})
                    <#else>Nog vast te stellen
                    </#if>
                </td>
            </tr>
            <#list inkomstenAanvrager.overigeInkomsten.inkomstenComponenten as inkomsten>
                <tr>
                    <td colspan="2">Soort inkomsten</td>
                    <td colspan="2">${inkomsten.primairInkomstencomponentType.name!""}</td>
                </tr>
                <tr>
                    <td colspan="2">Periodiciteit uitbetaling</td>
                    <td colspan="2">${inkomsten.periodiciteitUitbetaling.name!""}</td>
                </tr>
                <#if inkomsten.primairInkomstencomponentType.code == '2'>
                    <tr>
                        <td colspan="2">Categorie</td>
                        <td colspan="2">${inkomsten.categorie.name!""}</td>
                    </tr>
                </#if>
                <#if inkomsten.primairInkomstencomponentType.code == '1'>
                    <tr>
                        <td colspan="2">Soort alimentatie</td>
                        <td colspan="2">
                            ${inkomsten.inkomstensoortAlimentatie.name!""}
                        </td>
                    </tr>
                </#if>
                <#if inkomsten.primairInkomstencomponentType.code == '8'>
                    <tr>
                        <td colspan="2">Beschrijving hobby</td>
                        <td colspan="2">${inkomsten.beschrijvingHobby!""}</td>
                    </tr>
                </#if>
                <tr><td colspan="4">&nbsp;</td></tr>
                <tr>
                    <td rowspan="2">Begindatum periode</td>
                    <td rowspan="2">Einddatum periode</td>
                    <td colspan="2">Inkomen</td>
                </tr>
                <tr>
                    <td>Bruto bedrag</td>
                    <td>Netto bedrag</td>
                </tr>
                <tr>
                    <td>${inkomsten.periodeStartdatum?has_content?then(showDate(inkomsten.periodeStartdatum), '-')}</td>
                    <td>${inkomsten.periodeEinddatum?has_content?then(showDate(inkomsten.periodeEinddatum), '-')}</td>
                    <td><#if inkomsten.brutoInkomsten??>&euro; ${inkomsten.brutoInkomsten?string("0.00")}<#else>-</#if></td>
                    <td><#if inkomsten.nettoInkomsten??>&euro; ${inkomsten.nettoInkomsten?string("0.00")}<#else>-</#if></td>
                </tr>
                <#if inkomsten?has_next>
                    <tr class="divider"><td colspan="4">&nbsp;</td></tr>
                </#if>
            </#list>
        </table>
        <#if (inkomstenAanvrager.overigeInkomsten.totaalNettomaandelijkseInkomstenVanEenStap)?has_content>
            <table class="table">
                <tr class="divider">
                    <td>Totaal netto overige inkomsten</td>
                </tr>
                <tr><td>&euro; ${inkomstenAanvrager.overigeInkomsten.totaalNettomaandelijkseInkomstenVanEenStap?string("0.00")}</td></tr>
            </table>
        </#if>
        <table class="table">
            <tr class="divider">
                <td>Toelichting</td>
            </tr>
            <tr><td>${inkomstenAanvrager.overigeInkomsten.toelichting!""}</td></tr>
        </table>
    </#if>

    <#if inkomstenAanvrager.totaalNettoMaandInkomsten?has_content>
        <table class="table">
            <tr class="divider">
                <td>Totaal netto inkomsten ${aanvrager.voornamen!""} ${aanvrager.voorvoegsel!""} ${aanvrager.achternaam!""}</td>
            </tr>
            <tr><td>&euro; ${inkomstenAanvrager.totaalNettoMaandInkomsten?string("0.00")}</td></tr>
        </table>
    </#if>

    <#if (inkomstenAanvrager.toelichtingTotaalVastgesteldInkomen)?has_content>
        <table class="table">
            <tr class="divider">
                <td>Toelichting totaal vastgesteld inkomen ${aanvrager.voornamen!""} ${aanvrager.voorvoegsel!""} ${aanvrager.achternaam!""}</td>
            </tr>
            <tr><td>${inkomstenAanvrager.toelichtingTotaalVastgesteldInkomen}</td></tr>
        </table>
    </#if>

    <#if vrijlatingAanvrager?has_content>
        <table  class="table">
            <tr class="divider">
                <td  colspan="2">Vrijlating op inkomen</td>
                <td  colspan="1">
                    <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.bs224InkomstenAanvrager??>
                        <#if additioneleVeldenAanvrager.bs224InkomstenAanvrager.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                        (${showDate(additioneleVeldenAanvrager.bs224InkomstenAanvrager.datumVastgesteld)})
                    <#else>Nog vast te stellen
                    </#if>
                </td>
            </tr>
            <tr>
                <td>Inkomstenvrijlating</td>
                <td>Percentage</td>
                <td>Begindatum</td>
            </tr>
            <tr>
                <td>${(vrijlatingLabels[vrijlatingAanvrager.vrijlatingType!""])!""}</td>
                <td>${vrijlatingAanvrager.percentage!""}</td>
                <td>${vrijlatingAanvrager.ingangsdatum?has_content?then(showDate(vrijlatingAanvrager.ingangsdatum), '-')}</td>
            </tr>
        </table>
    </#if>
</#if>

<!-- ============================================================ -->
<!-- VASTGESTELDE GEGEVENS PARTNER                                -->
<!-- ============================================================ -->
<#if partnerExist>
    <div class="page-break"></div>
    <h2>Vastgestelde gegevens partner (${partner.voornamen!""} ${partner.voorvoegsel!""} ${partner.achternaam!""})</h2>

    <table class="table">
        <tr class="divider">
            <td>Persoon</td>
            <td>
                <#if additioneleVeldenPartner?? && additioneleVeldenPartner.vastleggingPersoon??>
                    <#if additioneleVeldenPartner.vastleggingPersoon.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                    (${showDate(additioneleVeldenPartner.vastleggingPersoon.datumVastgesteld)})
                <#else>Nog vast te stellen
                </#if>
            </td>
        </tr>
        <tr class="odd">
            <td class="label">BSN</td>
            <td>${partner.bsn!""}</td>
        </tr>
        <tr class="even">
            <td class="label">Naam</td>
            <td>${partner.voorletters!""} ${partner.voorvoegsel!""} ${partner.achternaam!""}</td>
        </tr>
        <tr class="odd">
            <td class="label">Geboortedatum</td>
            <td>${partner.geboortedatum?has_content?then(partner.geboortedatum?split("-")?reverse?join("-"), '-')}</td>
        </tr>
        <tr class="even">
            <td class="label">Beschikbaarheid (u/week)</td>
            <td>
                <#if beschikbaarheidPartner??>
                    ${beschikbaarheidPartner.uurPerWeek!""}
                </#if>
            </td>
        </tr>
        <#if additioneleVeldenPartner?? && additioneleVeldenPartner.vastleggingPersoon?? && additioneleVeldenPartner.vastleggingPersoon.toelichtingConsulent?has_content>
            <tr class="odd">
                <td class="label">Toelichting</td>
                <td>${additioneleVeldenPartner.vastleggingPersoon.toelichtingConsulent}</td>
            </tr>
        </#if>
    </table>

    <table  class="table">
        <tr class="divider">
            <td>Adres</td>
            <td>
                <#if additioneleVeldenPartner?? && additioneleVeldenPartner.verblijfadres??>
                    <#if additioneleVeldenPartner.verblijfadres.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                    (${showDate(additioneleVeldenPartner.verblijfadres.datumVastgesteld)})
                <#else>Nog vast te stellen
                </#if>
            </td>
        </tr>
        <tr class="odd">
            <td class="label">Verblijfadres</td>
            <td>
                <#if partner.verblijfadres??>
                    ${partner.verblijfadres.straatnaam!""} ${partner.verblijfadres.huisnummer!""}${partner.verblijfadres.huisletter!""},
                    ${partner.verblijfadres.postcode!""} ${partner.verblijfadres.woonplaatsnaam!""}
                </#if>
            </td>
        </tr>
        <tr class="even">
            <td class="label">Correspondentieadres</td>
            <td></td>
        </tr>
        <#if additioneleVeldenPartner?? && additioneleVeldenPartner.verblijfadres?? && additioneleVeldenPartner.verblijfadres.toelichtingConsulent?has_content>
            <tr class="odd">
                <td class="label">Toelichting</td>
                <td>${additioneleVeldenPartner.verblijfadres.toelichtingConsulent}</td>
            </tr>
        </#if>
    </table>

    <table  class="table">
        <tr class="divider">
            <td>Nationaliteit / Verblijfstatus</td>
            <td>
                <#if additioneleVeldenPartner?? && additioneleVeldenPartner.bs213verblijfstitel??>
                    <#if additioneleVeldenPartner.bs213verblijfstitel.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                    (${showDate(additioneleVeldenPartner.bs213verblijfstitel.datumVastgesteld)})
                <#else>Nog vast te stellen
                </#if>
            </td>
        </tr>
        <tr class="odd">
            <td class="label">Nationaliteit</td>
            <td>
                <#if partner.nationaliteiten?? && partner.nationaliteiten?has_content>
                    <#list partner.nationaliteiten as nat>
                        ${nat.name!""}<#if nat?has_next>, </#if>
                    </#list>
                </#if>
            </td>
        </tr>
        <tr class="even">
            <td class="label">Verblijfstitel / -code</td>
            <td>
                <#if partner.verblijfstitel?? && partner.verblijfstitel.codeVerblijfstitel?? && partner.verblijfstitel.codeVerblijfstitel.name?has_content && partner.verblijfstitel.codeVerblijfstitel.code?has_content>
                    ${partner.verblijfstitel.codeVerblijfstitel.name} (${partner.verblijfstitel.codeVerblijfstitel.code})
                <#else>—</#if>
            </td>
        </tr>
        <tr class="odd">
            <td class="label">Gemeenschapsrecht</td>
            <td>
                <#if additioneleVeldenPartner?? && additioneleVeldenPartner.bs213verblijfstitel??>
                    <#if additioneleVeldenPartner.bs213verblijfstitel.isSprakeVanGemeenschapsrecht??>
                        ${additioneleVeldenPartner.bs213verblijfstitel.isSprakeVanGemeenschapsrecht?string("Ja","Nee")}
                    <#else>—</#if>
                </#if>
            </td>
        </tr>
        <#if additioneleVeldenPartner?? && additioneleVeldenPartner.bs213verblijfstitel?? && additioneleVeldenPartner.bs213verblijfstitel.toelichtingConsulent?has_content>
            <tr class="even">
                <td class="label">Toelichting</td>
                <td colspan="2">${additioneleVeldenPartner.bs213verblijfstitel.toelichtingConsulent}</td>
            </tr>
        </#if>
    </table>

    <!-- Voorliggende voorzieningen partner -->
    <table class="table">
        <tr class="divider">
            <td colspan="2">Voorliggende voorzieningen</td>
            <td>
                <#if additioneleVeldenPartner?? && additioneleVeldenPartner.bs444VoorliggendeVoorzieningen??>
                    <#if additioneleVeldenPartner.bs444VoorliggendeVoorzieningen.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                    (${showDate(additioneleVeldenPartner.bs444VoorliggendeVoorzieningen.datumVastgesteld)})
                <#else>Nog vast te stellen
                </#if>
            </td>
        </tr>
        <tr>
            <th>Signaal</th>
            <th>Voorziening</th>
            <th>Omschrijving</th>
        </tr>
        <#assign partnerVoorliggendeVoorzieningenZichtbaar = partnerVoorliggendeVoorzieningen?filter(v -> (v.mogelijkRecht!"") != "Niet beoordeeld") />
        <#if partnerVoorliggendeVoorzieningenZichtbaar?has_content>
            <#list partnerVoorliggendeVoorzieningenZichtbaar as voorziening>
                <tr class="${voorziening?is_odd_item?string("odd","even")}">
                    <td><#if voorziening.signalering??>${voorziening.signalering?string("Ja","Nee")}<#else>-</#if></td>
                    <td>${voorziening.voorziening!"-"}</td>
                    <td>${voorziening.mogelijkRecht!"-"}</td>
                </tr>
                <#if voorziening.toelichting?has_content>
                    <tr class="${voorziening?is_odd_item?string("odd","even")}">
                        <td class="label">Toelichting</td>
                        <td colspan="2" class="style-multiline">${voorziening.toelichting}</td>
                    </tr>
                </#if>
            </#list>
        <#else>
            <tr><td colspan="3">Geen voorliggende voorzieningen</td></tr>
        </#if>
    </table>

    <!-- Uitsluitingsgronden partner -->
    <table class="table">
        <tr class="divider">
            <td colspan="2">Uitsluitingsgronden</td>
            <td>
                <#if additioneleVeldenPartner?? && additioneleVeldenPartner.bs218Uitsluitingsgronden??>
                    <#if additioneleVeldenPartner.bs218Uitsluitingsgronden.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                    (${showDate(additioneleVeldenPartner.bs218Uitsluitingsgronden.datumVastgesteld)})
                <#else>Nog vast te stellen
                </#if>
            </td>
        </tr>
        <tr>
            <th>Signaal</th>
            <th>Uitsluitingsgrond</th>
            <th>Omschrijving</th>
        </tr>
        <#assign partnerUitsluitingsgrondenZichtbaar = partnerUitsluitingsgronden?filter(u -> (u.uitsluiting!"") != "Niet Bepaald") />
        <#if partnerUitsluitingsgrondenZichtbaar?has_content>
            <#list partnerUitsluitingsgrondenZichtbaar as uitsluitingsgrond>
                <tr class="${uitsluitingsgrond?is_odd_item?string("odd","even")}">
                    <td><#if uitsluitingsgrond.signalering??>${uitsluitingsgrond.signalering?string("Ja","Nee")}<#else>-</#if></td>
                    <td>${uitsluitingsgrond.uitsluitingsgrond!"-"}</td>
                    <td>${uitsluitingsgrond.uitsluiting!"-"}</td>
                </tr>
                <#if uitsluitingsgrond.toelichting?has_content>
                    <tr class="${uitsluitingsgrond?is_odd_item?string("odd","even")}">
                        <td class="label">Toelichting</td>
                        <td colspan="2" class="style-multiline">${uitsluitingsgrond.toelichting}</td>
                    </tr>
                </#if>
            </#list>
        <#else>
            <tr><td colspan="3">Geen uitsluitingsgronden</td></tr>
        </#if>
    </table>

    <h2>Relaties partner</h2>
    <table  class="table">
        <tr class="divider">
            <td>Relaties</td>
            <td colspan="2">
                <#if additioneleVeldenPartner?? && additioneleVeldenPartner.bs216relaties??>
                    <#if additioneleVeldenPartner.bs216relaties.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                    (${showDate(additioneleVeldenPartner.bs216relaties.datumVastgesteld)})
                <#else>Nog vast te stellen
                </#if>
            </td>
        </tr>
        <#if partner.relaties?? && partner.relaties?has_content>
            <#list partner.relaties as relatie>
                <tr class="${relatie?is_odd_item?string("odd","even")}">
                    <td>Naam</td>
                    <td colspan="2">${relatie.voornamen!""} ${relatie.tussenvoegsel!""} ${relatie.achternaam!""}</td>
                    <td rowspan="7">
                        <ul>
                            <#if (relatie.woontOpHetzelfdeAdresAlsAanvrager)!false>
                                <li>Woont op hetzelfde adres</li>
                            </#if>
                            <#if (relatie.indicatieGezamelijkeHuishouding)!false>
                                <li>Is onderdeel van gezamelijke huishouding</li>
                            </#if>
                            <#if (relatie.indicatieOnderhoudsplichtPartner)!false>
                                <li>Is onderhoudsplichtig</li>
                            </#if>
                            <#if (relatie.indicatieCoOuder)!false>
                                <li>Is co-ouder</li>
                            </#if>
                            <#if (relatie.indicatieOnderhoudsplichtOuderKind)!false>
                                <li>Is onderhoudsplichtig</li>
                            </#if>
                            <#if (relatie.isKindEenMedebewoner)!false>
                                <li>Is ook medebewoner</li>
                            </#if>
                            <#if (relatie.afgelopen2JaarPartnerGeweest)!false>
                                <li>Is afgelopen 2 jaar partner geweest</li>
                            </#if>
                            <#if (relatie.samenEenErkendKindHebben)!false>
                                <li>Heeft samen een kind</li>
                            </#if>
                            <#if (relatie.samenlevingscontract)!false>
                                <li>Heeft een samenlevingscontract</li>
                            </#if>
                            <#if (relatie.afgelopenTweeJaarEeeHuishouden)!false>
                                <li>Was afgelopen 2 jaar één huishouden</li>
                            </#if>
                            <#if (relatie.delenKostenHuur)!false>
                                <li>Deelt kosten huur</li>
                            </#if>
                            <#if (relatie.delenVasteLasten)!false>
                                <li>Deelt vaste lasten</li>
                            </#if>
                            <#if (relatie.delenkostenBoodschappen)!false>
                                <li>Deelt kosten boodschappen</li>
                            </#if>
                            <#if (relatie.samenEten)!false>
                                <li>Eet samen</li>
                            </#if>
                            <#if (relatie.samenOpVakantie)!false>
                                <li>Gaat samen op vakantie</li>
                            </#if>
                            <#if (relatie.samenTakenHuishouden)!false>
                                <li>Deelt taken huishouden</li>
                            </#if>
                            <#if (relatie.tijdelijkVerblijf)!false>
                                <li>Betreft een tijdelijke verblijf</li>
                            </#if>
                            <#if (relatie.indicatieZorgbehoefte)!false>
                                <li>Heeft een zorgbehoefte</li>
                            </#if>
                            <#if (relatie.kamerHuur)!false>
                                <li>Heeft een commerciële relatie</li>
                            </#if>
                            <#if (relatie.leeftijdOnder27)!false>
                                <li>Leeftijd onder 27</li>
                            </#if>
                            <#if (relatie.studiefinanciering)!false>
                                <li>Studiefinanciering</li>
                            </#if>
                        </ul>
                    </td>
                </tr>
                <tr class="${relatie?is_odd_item?string("odd","even")}">
                    <td>Begindatum</td>
                    <td>${relatie.datumAanvangRelatie?has_content?then(showDate(relatie.datumAanvangRelatie), '-')}</td>
                </tr>
                <tr class="${relatie?is_odd_item?string("odd","even")}">
                    <td>Einddatum</td>
                    <td>${relatie.datumEindeRelatie?has_content?then(showDate(relatie.datumEindeRelatie), '-')}</td>
                </tr>
                <tr class="${relatie?is_odd_item?string("odd","even")}">
                    <td>Geboortedatum</td>
                    <td>${relatie.geboortedatum?has_content?then(showDate(relatie.geboortedatum), '-')}</td>
                </tr>
                <tr class="${relatie?is_odd_item?string("odd","even")}">
                    <td>Relatietype</td>
                    <td>${relatieTypes[(relatie.relatietype)!'']!'-'}</td>
                </tr>
                <#if relatie.relatietype == 'ouderKind'>
                    <tr class="${relatie?is_odd_item?string("odd","even")}">
                        <td>Kinderbijslag</td>
                        <td><#if relatie.indicatieKinderbijslag?? && relatie.indicatieKinderbijslag><span class="value-yes">Ja</span><#else><span class="value-no">Nee</span></#if></td>
                    </tr>
                </#if>
                <tr class="${relatie?is_odd_item?string("odd","even")}">
                    <td>Kostendeler</td>
                    <td><#if relatie.indicatieKostendeler?? && relatie.indicatieKostendeler><span class="value-yes">Ja</span><#else><span class="value-no">Nee</span></#if></td>
                </tr>
                <#if relatie.toelichtingRelatieTypeAnders??>
                    <tr class="${relatie?is_odd_item?string("odd","even")}">
                        <td>Toelichting relatie medebewoner</td>
                        <td>${relatie.toelichtingRelatieTypeAnders!""}</td>
                    </tr>
                </#if>
                <#if relatie?has_next>
                    <tr class="divider"><td colspan="3"></td></tr>
                </#if>
            </#list>
        <#else>
            <tr><td colspan="3">Geen relaties geregistreerd</td></tr>
        </#if>
    </table>
</#if>

<!-- ============================================================ -->
<!-- INKOMEN Partner                                              -->
<!-- ============================================================ -->
<#if dossierType == 'aanvraag-algemene-bijstand-dcm'>
    <#if inkomstenPartner?has_content>
        <h2>Inkomsten partner</h2>
    </#if>
    <#if inkomstenPartner?has_content && inkomstenPartner.arbeid?has_content && inkomstenPartner.arbeid.inkomstenComponenten?has_content>
        <table  class="table">
            <tr class="divider">
                <td  colspan="2">Inkomsten uit arbeid</td>
                <td  colspan="2">
                    <#if additioneleVeldenPartner?? && additioneleVeldenPartner.bs224InkomstenAanvrager??>
                        <#if additioneleVeldenPartner.bs224InkomstenAanvrager.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                        (${showDate(additioneleVeldenPartner.bs224InkomstenAanvrager.datumVastgesteld)})
                    <#else>Nog vast te stellen
                    </#if>
                </td>
            </tr>
            <#list inkomstenPartner.arbeid.inkomstenComponenten as inkomsten>
                <tr>
                    <td colspan="2">Naam bedrijf</td>
                    <td colspan="2">${inkomsten.naam!""}</td>
                </tr>
                <tr>
                    <td colspan="2">Loonheffingsnummer</td>
                    <td colspan="2">${inkomsten.loonheffingsnummer!""}</td>
                </tr>
                <tr>
                    <td colspan="2">Soort inkomen</td>
                    <td colspan="2">${inkomsten.primairInkomstencomponentType.name!""}</td>
                </tr>
                <tr>
                    <td colspan="2">Periodiciteit uitbetaling</td>
                    <td colspan="2">${inkomsten.periodiciteitUitbetaling.name!""}</td>
                </tr>
                <tr>
                    <td colspan="2">Soort contract</td>
                    <td colspan="2">${inkomsten.soortContract.name!""}</td>
                </tr>
                <tr>
                    <td colspan="2">
                        Reservering vakantiegeld
                        <span class="float-right">
                         <#if inkomsten.indicatieVakantiegeld!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                    <td colspan="2">
                        Loonbeslag
                        <span class="float-right">
                         <#if inkomsten.indicatieLoonbeslag!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        LHK toegepast
                        <span class="float-right">
                         <#if inkomsten.indicatieHeffingskorting!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                    <td colspan="2">
                        Uren vermindering
                        <span class="float-right">
                         <#if inkomsten.indicatieUrenVermindering!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                </tr>
                <tr><td colspan="4">&nbsp;</td></tr>
                <tr>
                    <td>Begindatum periode</td>
                    <td>Einddatum periode</td>
                    <td>Bruto inkomen</td>
                    <td>Netto inkomen</td>
                </tr>
                <tr>
                    <td>${inkomsten.periodeStartdatum?has_content?then(showDate(inkomsten.periodeStartdatum), '-')}</td>
                    <td>${inkomsten.periodeEinddatum?has_content?then(showDate(inkomsten.periodeEinddatum), '-')}</td>
                    <td><#if inkomsten.brutoInkomsten??>&euro; ${inkomsten.brutoInkomsten?string("0.00")}<#else>-</#if></td>
                    <td><#if inkomsten.nettoInkomsten??>&euro; ${inkomsten.nettoInkomsten?string("0.00")}<#else>-</#if></td>
                </tr>
                <tr><td colspan="4">&nbsp;</td></tr>
                <tr>
                    <td colspan="2">Componenten</td>
                    <td>Bruto bedrag</td>
                    <td>Netto bedrag</td>
                </tr>
                <#if inkomsten.indicatieAVWBBudget>
                    <tr>
                        <td colspan="2">${inkomsten.avwbCategorie.name!""}</td>
                        <td><#if inkomsten.avwbBrutoInkomsten??>&euro; ${inkomsten.avwbBrutoInkomsten?string("0.00")}<#else>-</#if></td>
                        <td><#if inkomsten.avwbNettoInkomsten??>&euro; ${inkomsten.avwbNettoInkomsten?string("0.00")}<#else>-</#if></td>
                    </tr>
                </#if>
                <#if inkomsten.indicatieVakantiegeld>
                    <tr>
                        <td colspan="2">Vakantiegeld</td>
                        <td><#if inkomsten.vakantiegeldBrutoInkomsten??>&euro; ${inkomsten.vakantiegeldBrutoInkomsten?string("0.00")}<#else>-</#if></td>
                        <td><#if inkomsten.vakantiegeldNettoInkomsten??>&euro; ${inkomsten.vakantiegeldNettoInkomsten?string("0.00")}<#else>-</#if></td>
                    </tr>
                </#if>
                <#if inkomsten.indicatieMaaltijdvergoeding>
                    <tr>
                        <td colspan="2">Maaltijdvergoeding</td>
                        <td><#if inkomsten.maaltijdvergoedingBrutoInkomsten??>&euro; ${inkomsten.maaltijdvergoedingBrutoInkomsten?string("0.00")}<#else>-</#if></td>
                        <td><#if inkomsten.maaltijdvergoedingNettoInkomsten??>&euro; ${inkomsten.maaltijdvergoedingNettoInkomsten?string("0.00")}<#else>-</#if></td>
                    </tr>
                </#if>
                <#if inkomsten.indicatieOnkostenvergoeding>
                    <tr>
                        <td colspan="2">Onkostenvergoeding</td>
                        <td><#if inkomsten.onkostenvergoedingBrutoInkomsten??>&euro; ${inkomsten.onkostenvergoedingBrutoInkomsten?string("0.00")}<#else>-</#if></td>
                        <td><#if inkomsten.onkostenvergoedingNettoInkomsten??>&euro; ${inkomsten.onkostenvergoedingNettoInkomsten?string("0.00")}<#else>-</#if></td>
                    </tr>
                </#if>
                <#if inkomsten.indicatieReiskostenvergoeding>
                    <tr>
                        <td colspan="2">Reiskostenvergoeding</td>
                        <td><#if inkomsten.reiskostenvergoedingBrutoInkomsten??>&euro; ${inkomsten.reiskostenvergoedingBrutoInkomsten?string("0.00")}<#else>-</#if></td>
                        <td><#if inkomsten.reiskostenvergoedingNettoInkomsten??>&euro; ${inkomsten.reiskostenvergoedingNettoInkomsten?string("0.00")}<#else>-</#if></td>
                    </tr>
                </#if>
                <#if inkomsten.indicatieVergoedingInNatura>
                    <tr>
                        <td colspan="2">Vergoeding in natura</td>
                        <td><#if inkomsten.naturaBrutoInkomsten??>&euro; ${inkomsten.naturaBrutoInkomsten?string("0.00")}<#else>-</#if></td>
                        <td><#if inkomsten.naturaNettoInkomsten??>&euro; ${inkomsten.naturaNettoInkomsten?string("0.00")}<#else>-</#if></td>
                    </tr>
                </#if>
                <#if inkomsten?has_next>
                    <tr class="divider"><td colspan="4">&nbsp;</td></tr>
                </#if>
            </#list>
        </table>
        <table class="table">
            <tr class="divider">
                <td>Toelichting</td>
            </tr>
            <tr><td>${inkomstenPartner.arbeid.toelichting!""}</td></tr>
        </table>
    </#if>

    <#if inkomstenPartner?has_content && inkomstenPartner.uitkeringen?has_content && inkomstenPartner.uitkeringen.inkomstenComponenten?has_content>
        <table  class="table">
            <tr class="divider">
                <td  colspan="3">Inkomsten uit uitkering, pensioen en of studiefinanciering </td>
                <td  colspan="3">
                    <#if additioneleVeldenPartner?? && additioneleVeldenPartner.bs224InkomstenAanvrager??>
                        <#if additioneleVeldenPartner.bs224InkomstenAanvrager.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                        (${showDate(additioneleVeldenPartner.bs224InkomstenAanvrager.datumVastgesteld)})
                    <#else>Nog vast te stellen
                    </#if>
                </td>
            </tr>
            <#list inkomstenPartner.uitkeringen.inkomstenComponenten as inkomsten>
                <tr>
                    <td colspan="3">Naam bedrijf</td>
                    <td colspan="3">${inkomsten.naam!""}</td>
                </tr>
                <tr>
                    <td colspan="3">Loonheffingsnummer</td>
                    <td colspan="3">${inkomsten.loonheffingsnummer!""}</td>
                </tr>
                <tr>
                    <td colspan="3">Soort inkomen</td>
                    <td colspan="3">${inkomsten.primairInkomstencomponentType.name!""}</td>
                </tr>
                <tr>
                    <td colspan="3">Periodiciteit uitbetaling</td>
                    <td colspan="3">${inkomsten.periodiciteitUitbetaling.name!""}</td>
                </tr>
                <tr>
                    <td colspan="3">
                        Reservering vakantiegeld
                        <span class="float-right">
                         <#if inkomsten.indicatieVakantiegeld!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                    <td colspan="3">
                        Beslag
                        <span class="float-right">
                         <#if inkomsten.indicatieBeslagUitkering!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                </tr>
                <tr>
                    <td colspan="3">
                        LHK toegepast
                        <span class="float-right">
                         <#if inkomsten.indicatieHeffingskorting!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                    <td colspan="3">
                        Uren vermindering
                        <span class="float-right">
                         <#if inkomsten.indicatieUrenVermindering!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                </tr>
                <tr>
                    <td colspan="3">
                        Toeslag
                        <span class="float-right">
                         <#if inkomsten.indicatieToeslag!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                    <td colspan="3">
                        Verlaging maatregel
                        <span class="float-right">
                         <#if inkomsten.indicatieVerlagingDoorMaatregel!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                </tr>
                <tr>
                    <td colspan="6">
                        Verlaging boete
                        <span class="float-right">
                         <#if inkomsten.indicatieVerlagingDoorBoete!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                </tr>
                <tr><td colspan="6">&nbsp;</td></tr>
                <tr>
                    <td rowspan="2">Begindatum periode</td>
                    <td rowspan="2">Einddatum periode</td>
                    <td colspan="2">Inkomen</td>
                    <td colspan="2">Vakantiegeld</td>
                </tr>
                <tr>
                    <td>Bruto bedrag</td>
                    <td>Netto bedrag</td>
                    <td>Bruto bedrag</td>
                    <td>Netto bedrag</td>
                </tr>
                <tr>
                    <td>${inkomsten.periodeStartdatum?has_content?then(showDate(inkomsten.periodeStartdatum), '-')}</td>
                    <td>${inkomsten.periodeEinddatum?has_content?then(showDate(inkomsten.periodeEinddatum), '-')}</td>
                    <td><#if inkomsten.brutoInkomsten??>&euro; ${inkomsten.brutoInkomsten?string("0.00")}<#else>-</#if></td>
                    <td><#if inkomsten.nettoInkomsten??>&euro; ${inkomsten.nettoInkomsten?string("0.00")}<#else>-</#if></td>
                    <td><#if inkomsten.vakantiegeldBrutoInkomsten??>&euro; ${inkomsten.vakantiegeldBrutoInkomsten?string("0.00")}<#else>-</#if></td>
                    <td><#if inkomsten.vakantiegeldNettoInkomsten??>&euro; ${inkomsten.vakantiegeldNettoInkomsten?string("0.00")}<#else>-</#if></td>
                </tr>
                <#if inkomsten?has_next>
                    <tr class="divider"><td colspan="6">&nbsp;</td></tr>
                </#if>
            </#list>
        </table>
        <table class="table">
            <tr class="divider">
                <td>Toelichting</td>
            </tr>
            <tr><td>${inkomstenPartner.uitkeringen.toelichting!""}</td></tr>
        </table>
    </#if>

    <#if inkomstenPartner?has_content && inkomstenPartner.onderneming?has_content && inkomstenPartner.onderneming.inkomstenComponenten?has_content>
        <table  class="table">
            <tr class="divider">
                <td  colspan="2">Onderneming</td>
                <td  colspan="2">
                    <#if additioneleVeldenPartner?? && additioneleVeldenPartner.bs224InkomstenAanvrager??>
                        <#if additioneleVeldenPartner.bs224InkomstenAanvrager.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                        (${showDate(additioneleVeldenPartner.bs224InkomstenAanvrager.datumVastgesteld)})
                    <#else>Nog vast te stellen
                    </#if>
                </td>
            </tr>
            <#list inkomstenPartner.onderneming.inkomstenComponenten as inkomsten>
                <tr>
                    <td colspan="2">Naam bedrijf</td>
                    <td colspan="2">${inkomsten.naam!""}</td>
                </tr>
                <tr>
                    <td colspan="2">Soort bedrijf</td>
                    <td colspan="2">${inkomsten.soortBedrijf!""}</td>
                </tr>
                <tr>
                    <td colspan="2">Soort inkomen</td>
                    <td colspan="2">${inkomsten.primairInkomstencomponentType.name!""}</td>
                </tr>
                <tr>
                    <td colspan="2">Periodiciteit uitbetaling</td>
                    <td colspan="2">${inkomsten.periodiciteitUitbetaling.name!""}</td>
                </tr>
                <tr>
                    <td colspan="2">Aantal uren per week</td>
                    <td colspan="2">${inkomsten.urenPerWeek!""}</td>
                </tr>
                <tr>
                    <td colspan="2">
                        Ingeschreven KvK
                        <span class="float-right">
                         <#if inkomsten.indicatieIngeschrevenKvK!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                    <td colspan="2">
                        Recht op zelfstandige aftrek
                        <span class="float-right">
                         <#if inkomsten.indicatieRechtOpZelfstandigenaftrek!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        Toestemming gemeente parttime ondernemen
                        <span class="float-right">
                         <#if inkomsten.indicatieToestemmingGemeenteParttime!false>Ja<#else>Nee</#if>
                    </span>
                    </td>
                    <td colspan="2">
                    </td>
                </tr>
                <tr><td colspan="4">&nbsp;</td></tr>
                <tr>
                    <td rowspan="2">Begindatum periode</td>
                    <td rowspan="2">Einddatum periode</td>
                    <td colspan="2">Inkomen</td>
                </tr>
                <tr>
                    <td>Bruto bedrag</td>
                    <td>Netto bedrag</td>
                </tr>
                <tr>
                    <td>${inkomsten.periodeStartdatum?has_content?then(showDate(inkomsten.periodeStartdatum), '-')}</td>
                    <td>${inkomsten.periodeEinddatum?has_content?then(showDate(inkomsten.periodeEinddatum), '-')}</td>
                    <td><#if inkomsten.brutoInkomsten??>&euro; ${inkomsten.brutoInkomsten?string("0.00")}<#else>-</#if></td>
                    <td><#if inkomsten.nettoInkomsten??>&euro; ${inkomsten.nettoInkomsten?string("0.00")}<#else>-</#if></td>
                </tr>
                <#if inkomsten?has_next>
                    <tr class="divider"><td colspan="4">&nbsp;</td></tr>
                </#if>
            </#list>
        </table>
        <table class="table">
            <tr class="divider">
                <td>Toelichting</td>
            </tr>
            <tr><td>${inkomstenPartner.onderneming.toelichting!""}</td></tr>
        </table>
    </#if>

    <#if inkomstenPartner?has_content && inkomstenPartner.overigeInkomsten?has_content && inkomstenPartner.overigeInkomsten.inkomstenComponenten?has_content>
        <table  class="table">
            <tr class="divider">
                <td  colspan="2">Overige inkomsten</td>
                <td  colspan="2">
                    <#if additioneleVeldenPartner?? && additioneleVeldenPartner.bs224InkomstenAanvrager??>
                        <#if additioneleVeldenPartner.bs224InkomstenAanvrager.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                        (${showDate(additioneleVeldenPartner.bs224InkomstenAanvrager.datumVastgesteld)})
                    <#else>Nog vast te stellen
                    </#if>
                </td>
            </tr>
            <#list inkomstenPartner.overigeInkomsten.inkomstenComponenten as inkomsten>
                <tr>
                    <td colspan="2">Soort inkomsten</td>
                    <td colspan="2">${inkomsten.primairInkomstencomponentType.name!""}</td>
                </tr>
                <tr>
                    <td colspan="2">Periodiciteit uitbetaling</td>
                    <td colspan="2">${inkomsten.periodiciteitUitbetaling.name!""}</td>
                </tr>
                <#if inkomsten.primairInkomstencomponentType.code == '2'>
                    <tr>
                        <td colspan="2">Categorie</td>
                        <td colspan="2">${inkomsten.categorie.name!""}</td>
                    </tr>
                </#if>
                <#if inkomsten.primairInkomstencomponentType.code == '1'>
                    <tr>
                        <td colspan="2">Soort alimentatie</td>
                        <td colspan="2">
                            ${inkomsten.inkomstensoortAlimentatie.name!""}
                        </td>
                    </tr>
                </#if>
                <#if inkomsten.primairInkomstencomponentType.code == '8'>
                    <tr>
                        <td colspan="2">Beschrijving hobby</td>
                        <td colspan="2">${inkomsten.beschrijvingHobby!""}</td>
                    </tr>
                </#if>
                <tr><td colspan="4">&nbsp;</td></tr>
                <tr>
                    <td rowspan="2">Begindatum periode</td>
                    <td rowspan="2">Einddatum periode</td>
                    <td colspan="2">Inkomen</td>
                </tr>
                <tr>
                    <td>Bruto bedrag</td>
                    <td>Netto bedrag</td>
                </tr>
                <tr>
                    <td>${inkomsten.periodeStartdatum?has_content?then(showDate(inkomsten.periodeStartdatum), '-')}</td>
                    <td>${inkomsten.periodeEinddatum?has_content?then(showDate(inkomsten.periodeEinddatum), '-')}</td>
                    <td><#if inkomsten.brutoInkomsten??>&euro; ${inkomsten.brutoInkomsten?string("0.00")}<#else>-</#if></td>
                    <td><#if inkomsten.nettoInkomsten??>&euro; ${inkomsten.nettoInkomsten?string("0.00")}<#else>-</#if></td>
                </tr>
                <#if inkomsten?has_next>
                    <tr class="divider"><td colspan="4">&nbsp;</td></tr>
                </#if>
            </#list>
        </table>
        <table class="table">
            <tr class="divider">
                <td>Toelichting</td>
            </tr>
            <tr><td>${inkomstenPartner.overigeInkomsten.toelichting!""}</td></tr>
        </table>
    </#if>
    <#if inkomstenPartner.totaalNettoMaandInkomsten?has_content>
        <table class="table">
            <tr class="divider">
                <td>Totaal netto inkomsten ${partner.voornamen!""} ${partner.voorvoegsel!""} ${partner.achternaam!""}</td>
            </tr>
            <tr><td>&euro; ${inkomstenPartner.totaalNettoMaandInkomsten?string("0.00")}</td></tr>
        </table>
    </#if>
    <#if inkomstenPartner?has_content && inkomstenPartner.welkeInkomsten?has_content && inkomstenPartner.welkeInkomsten.geenInkomstenVaststellen?has_content>
        <table class="table">
            <tr class="divider">
                <td>Toelichting totaal vastgesteld inkomen ${partner.voornamen!""} ${partner.voorvoegsel!""} ${partner.achternaam!""}</td>
            </tr>
            <tr><td>${inkomstenPartner.toelichtingTotaalVastgesteldInkomen!""}</td></tr>
        </table>
    </#if>

    <#if additioneleVeldenAanvrager?has_content && additioneleVeldenAanvrager.bs224bGezamelijkInkomen?has_content && additioneleVeldenAanvrager.bs224bGezamelijkInkomen.gezamelijkInkomen?has_content>
        <table class="table">
            <tr class="divider">
                <td>Totaal gezamenlijke netto inkomsten</td>
            </tr>
            <tr><td>&euro; ${additioneleVeldenAanvrager.bs224bGezamelijkInkomen.gezamelijkInkomen?string("0.00")}</td></tr>
        </table>
    </#if>

    <#if vrijlatingPartner?has_content>
        <table  class="table">
            <tr class="divider">
                <td  colspan="2">Vrijlating op inkomen</td>
                <td  colspan="1">
                    <#if additioneleVeldenPartner?? && additioneleVeldenPartner.bs224InkomstenPartner??>
                        <#if additioneleVeldenPartner.bs224InkomstenPartner.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                        (${showDate(additioneleVeldenPartner.bs224InkomstenPartner.datumVastgesteld)})
                    <#else>Nog vast te stellen
                    </#if>
                </td>
            </tr>
            <tr>
                <td>Inkomstenvrijlating</td>
                <td>Percentage</td>
                <td>Begindatum</td>
            </tr>
            <tr>
                <td>${(vrijlatingLabels[vrijlatingPartner.vrijlatingType!""])!""}</td>
                <td>${vrijlatingPartner.percentage!""}</td>
                <td>${vrijlatingPartner.ingangsdatum?has_content?then(showDate(vrijlatingPartner.ingangsdatum), '-')}</td>
            </tr>
        </table>
    </#if>
</#if>


<!-- ============================================================ -->
<!-- Reden aanvraag                                               -->
<!-- ============================================================ -->
<h2>Afwijkende ingangsdatum</h2>

<table  class="table">
    <tr class="divider">
        <td colspan="2">Reden niet in staat eerder een aanvraag te doen</td>
        <td>
            <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.bs221ingangsdatum??>
                <#if additioneleVeldenAanvrager.bs221ingangsdatum.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                (${showDate(additioneleVeldenAanvrager.bs221ingangsdatum.datumVastgesteld)})
            <#else>Nog vast te stellen
            </#if>
        </td>
    </tr>
    <tr>
        <td>Gewenste ingangsdatum</td>
        <td colspan="2">
            <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.bs221ingangsdatum??>
                ${additioneleVeldenAanvrager.bs221ingangsdatum.ingangsdatum?has_content?then(showDate(additioneleVeldenAanvrager.bs221ingangsdatum.ingangsdatum), '-')}
            </#if>
        </td>
    </tr>
    <#if redenAanvraag?has_content && redenAanvraag.redenGewensteIngangsdatum?has_content && redenAanvraag.redenGewensteIngangsdatum == 'WACHTEN_BESLISSING_INSTANTIE'>
        <tr>
            <td>Reden</td>
            <td colspan="2">${(redenGewensteIngangsdatum[redenAanvraag.redenGewensteIngangsdatum!""])!""}</td>
        </tr>
        <tr>
            <td>Welke beslissing</td>
            <td colspan="2">${redenAanvraag.ingangBijstandsuitkering.bijzondereRedenNietEerderAanvragen!""}</td>
        </tr>
        <tr>
            <td>Ontvangstdatum beslissing</td>
            <td colspan="2">${showDate(redenAanvraag.ingangBijstandsuitkering.beslissingInstantieOntvangstdatum)}</td>
        </tr>
        <tr>
            <td>Toelichting vaststelling</td>
            <td colspan="2">
                <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.bs221ingangsdatum??> ${additioneleVeldenAanvrager.bs221ingangsdatum.toelichting!"-"}</#if>
            </td>
        </tr>
    </#if>
    <#if redenAanvraag?has_content && redenAanvraag.redenGewensteIngangsdatum?has_content && redenAanvraag.redenGewensteIngangsdatum == 'OPNAME_INSTELLING'>
        <tr>
            <td>Reden</td>
            <td colspan="2">${(redenGewensteIngangsdatum[redenAanvraag.redenGewensteIngangsdatum!""])!""}</td>
        </tr>
        <tr>
            <td>Naam instelling</td>
            <td colspan="2">${redenAanvraag.ingangBijstandsuitkering.bijzondereRedenNietEerderAanvragen!""}</td>
        </tr>
        <tr>
            <td>Ingangsdatum opname</td>
            <td colspan="2">${showDate(redenAanvraag.ingangBijstandsuitkering.opnameStartdatum)}</td>
        </tr>
        <tr>
            <td> Einddatum opname</td>
            <td colspan="2">${showDate(redenAanvraag.ingangBijstandsuitkering.opnameEinddatum)}</td>
        </tr>
        <tr>
            <td>Toelichting vaststelling</td>
            <td colspan="2">
                <#if additioneleVeldenAanvrager?? &&  additioneleVeldenAanvrager.bs221ingangsdatum??> ${additioneleVeldenAanvrager.bs221ingangsdatum.toelichting!"-"}</#if>
            </td>
        </tr>
    </#if>
    <#if redenAanvraag?has_content && redenAanvraag.redenGewensteIngangsdatum?has_content && redenAanvraag.redenGewensteIngangsdatum == 'WACHTEN_DIGID'>
        <tr>
            <td>Reden</td>
            <td colspan="2">${(redenGewensteIngangsdatum[redenAanvraag.redenGewensteIngangsdatum!""])!""}</td>
        </tr>
        <tr>
            <td>Aanvraagdatum DigiD</td>
            <td colspan="2">${showDate(redenAanvraag.ingangBijstandsuitkering.digiDaanvraagdatum)}</td>
        </tr>
        <tr>
            <td>Toelichting aanvrager</td>
            <td colspan="2">${redenAanvraag.ingangBijstandsuitkering.bijzondereRedenNietEerderAanvragen!""}</td>
        </tr>
        <tr>
            <td>Toelichting vaststelling</td>
            <td colspan="2">
                <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.bs221ingangsdatum??> ${additioneleVeldenAanvrager.bs221ingangsdatum.toelichting!"-"}</#if>
            </td>
        </tr>
    </#if>
    <#if redenAanvraag?has_content && redenAanvraag.redenGewensteIngangsdatum?has_content && redenAanvraag.redenGewensteIngangsdatum == 'ANDERE_REDEN'>
        <tr>
            <td>Reden</td>
            <td colspan="2">${(redenGewensteIngangsdatum[redenAanvraag.redenGewensteIngangsdatum!""])!""}</td>
        </tr>
        <tr>
            <td>Toelichting aanvrager</td>
            <td colspan="2">${redenAanvraag.ingangBijstandsuitkering.bijzondereRedenAanvraagdatumLaterIngaan!""}</td>
        </tr>
        <tr>
            <td>Toelichting vaststelling</td>
            <td colspan="2">
                <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.bs221ingangsdatum??> ${additioneleVeldenAanvrager.bs221ingangsdatum.toelichting!"-"}</#if>
            </td>
        </tr>
    </#if>

</table>

<!-- ============================================================ -->
<!-- VERMOGEN                                                      -->
<!-- ============================================================ -->
<#if dossierType == 'aanvraag-algemene-bijstand-dcm'>
    <h2>Vermogen (gezamenlijk)</h2>

    <!-- Bankrekeningen -->
    <table  class="table">
        <tr class="divider">
            <td colspan="2">Bank- of spaartegoeden</td>
            <td>
                <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.bs214vermogen??>
                    <#if additioneleVeldenAanvrager.bs214vermogen.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                    (${showDate(additioneleVeldenAanvrager.bs214vermogen.datumVastgesteld)})
                <#else>Nog vast te stellen
                </#if>
            </td>
        </tr>
        <tr>
            <th>Bankrekening</th>
            <th>Tenaamstelling</th>
            <th>Saldo</th>
        </tr>
        <#if vermogensComponenten?has_content && vermogensComponenten.bankrekeningen?has_content>
            <#assign totaal = 0 />
            <#list vermogensComponenten.bankrekeningen as bankrekeningen>
                <#assign totaal = totaal + (bankrekeningen.rekenwaarde)!0 />
                <#if bankrekeningen.componenten?has_content>
                    <#list bankrekeningen.componenten as rekening>
                        <tr class="">
                            <td>${rekening.iban!""}</td>
                            <td>${rekening.tenaamstelling!""}</td>
                            <td><#if rekening.waarde??>&euro; ${rekening.waarde?string("0.00")}<#else>-</#if></td>
                        </tr>
                    </#list>
                </#if>
            </#list>

            <tr>
                <td colspan="2"><strong>Totaal</strong></td>
                <td><#if totaal??>&euro; ${totaal?string("0.00")}<#else>-</#if></td>
            </tr>
        <#else>
            <tr><td colspan="3">Geen bankrekeningen</td></tr>
        </#if>
    </table>

    <!-- Bezittingen -->
    <table  class="table">
        <tr class="divider">
            <td colspan="2">Bezittingen</td>
            <td>
                <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.bs214vermogen??>
                    <#if additioneleVeldenAanvrager.bs214vermogen.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                    (${showDate(additioneleVeldenAanvrager.bs214vermogen.datumVastgesteld)})
                <#else>Nog vast te stellen
                </#if>
            </td>
        </tr>
        <tr>
            <th>Bezitting met vrijlating</th>
            <th>Omschrijving</th>
            <th>Waarde</th>
        </tr>
        <#if vermogensComponenten?has_content && vermogensComponenten.bezittingenMetVrijlating?has_content>
        <#assign totaal = 0 />
        <#list vermogensComponenten.bezittingenMetVrijlating as bezittingen>
            <#assign totaal = totaal + (bezittingen.rekenwaarde)!0 />
            <#if bezittingen.componenten?has_content>
                <#list bezittingen.componenten as bezitting>
                    <tr class="">
                        <td>${bezitting.soortVermogenscomponent.name!""}</td>
                        <td>${bezitting.omschrijving!""}</td>
                        <td><#if bezitting.waarde??>&euro; ${bezitting.waarde?string("0.00")}<#else>-</#if></td>
                    </tr>
                </#list>
            </#if>
        </#list>
        <tr>
            <td colspan="2"><strong>Subtotaal</strong></td>
            <td><#if vermogensComponenten.subTotaalBezittingenMetVrijlating??>&euro; ${vermogensComponenten.subTotaalBezittingenMetVrijlating?string("0.00")}<#else>-</#if></td>
        </tr>
        <tr>
            <td colspan="2"><strong>Vrijlating</strong></td>
            <td><#if vermogensComponenten.vrijlatingBezittingenMetVrijlating??>&euro; ${vermogensComponenten.vrijlatingBezittingenMetVrijlating?string("0.00")}<#else>-</#if></td>
        </tr>
        <tr>
            <td colspan="2"><strong>Totaal</strong></td>
            <td><#if totaal??>&euro; ${totaal?string("0.00")}<#else>-</#if></td>
        </tr>
        <tr class="divider"><td colspan="3"></td></tr>
        <tr>
            <td><strong>Toelichting</strong></td>
            <td colspan="2"></td>
        </tr>
    </#if>
        <tr class="divider"><td colspan="3"></td></tr>
        <tr>
            <th>Bezitting zonder vrijlating</th>
            <th>Omschrijving</th>
            <th>Waarde</th>
        </tr>
        <#if vermogensComponenten?has_content && vermogensComponenten.bezittingenZonderVrijlating?has_content>
            <#assign totaal = 0 />
            <#list vermogensComponenten.bezittingenZonderVrijlating as bezittingen>
                <#assign totaal = totaal + (bezittingen.rekenwaarde)!0 />
                <#if bezittingen.componenten?has_content>
                    <#list bezittingen.componenten as bezitting>
                        <tr class="">
                            <td>${bezitting.soortVermogenscomponent.name!""}</td>
                            <td>${bezitting.omschrijving!""}</td>
                            <td><#if bezitting.waarde??>&euro; ${bezitting.waarde?string("0.00")}<#else>-</#if></td>
                        </tr>
                    </#list>
                </#if>
            </#list>
            <tr>
                <td colspan="2"><strong>Subtotaal</strong></td>
                <td><#if vermogensComponenten.subTotaalBezittingenMZonderVrijlating??>&euro; ${vermogensComponenten.subTotaalBezittingenMZonderVrijlating?string("0.00")}<#else>-</#if></td>
            </tr>
            <tr>
                <td colspan="2"><strong>Vrijlating</strong></td>
                <td><#if vermogensComponenten.vrijlatingBezittingenMZonderVrijlating??>&euro; ${vermogensComponenten.vrijlatingBezittingenMZonderVrijlating?string("0.00")}<#else>-</#if></td>
            </tr>
            <tr>
                <td colspan="2"><strong>Totaal</strong></td>
                <td><#if totaal??>&euro; ${totaal?string("0.00")}<#else>-</#if></td>
            </tr>
            <tr class="divider"><td colspan="3"></td></tr>
            <tr>
                <td><strong>Toelichting</strong></td>
                <td colspan="2"></td>
            </tr>
        </#if>

    </table>

    <!-- Schulden -->
    <table  class="table">
        <tr class="divider">
            <td colspan="2">Schulden</td>
            <td>
                <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.bs214vermogen??>
                    <#if additioneleVeldenAanvrager.bs214vermogen.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                    (${showDate(additioneleVeldenAanvrager.bs214vermogen.datumVastgesteld)})
                <#else>Nog vast te stellen
                </#if>
            </td>
        </tr>
        <tr>
            <th>Soort</th>
            <th></th>
            <th>Saldo</th>
        </tr>
        <#if vermogensComponenten?has_content && vermogensComponenten.vastgesteldeSchuld?has_content>
            <#assign totaal = 0 />
            <#list vermogensComponenten.vastgesteldeSchuld as vastgesteldeSchuld>
                <#assign totaal = totaal + (vastgesteldeSchuld.waarde)!0 />
                <#if vastgesteldeSchuld.componenten?has_content>
                    <#list vastgesteldeSchuld.componenten as schuld>
                        <tr class="">
                            <td>${schuld.soortVermogenscomponent.name!""}</td>
                            <td>${schuld.omschrijving!""}</td>
                            <td><#if schuld.waarde??>&euro; ${schuld.waarde?string("0.00")}<#else>-</#if></td>
                        </tr>
                    </#list>
                </#if>
            </#list>

            <tr>
                <td colspan="2"><strong>Totaal</strong></td>
                <td><#if totaal??>&euro; ${totaal?string("0.00")}<#else>-</#if></td>
            </tr>
        <#else>
            <tr><td colspan="3">Geen schulden</td></tr>
        </#if>
    </table>
    <table class="table">
        <tr class="divider">
            <td colspan="6">&nbsp;</td>
        </tr>
        <tr>
            <td><strong>Totaal in aanmerking te nemen vermogen</strong></td>
            <td colspan="5"><#if vermogen?? && vermogen.berekendVermogen??>&euro; ${vermogen.berekendVermogen?string("0.00")}<#else>-</#if></td>
        </tr>
    </table>

    <!-- Eigen woning -->
    <table  class="table">
        <tr class="divider">
            <td colspan="4">Eigen woning</td>
            <td colspan="2">
                <#if additioneleVeldenAanvrager?? && additioneleVeldenAanvrager.bs214vermogen??>
                    <#if additioneleVeldenAanvrager.bs214vermogen.isHandmatigBeoordeeld>Handmatig<#else>Automatisch</#if>
                    (${showDate(additioneleVeldenAanvrager.bs214vermogen.datumVastgesteld)})
                <#else>Nog vast te stellen
                </#if>
            </td>
        </tr>
        <tr>
            <td></td>
            <td>WOZ</td>
            <td>Hypotheek</td>
            <td>Levensverzekering/ Belegging</td>
            <td>Buiten beschouwing te laten waarde</td>
            <td>Beschikbare waarde eigen woning</td>
        </tr>
        <#if vermogensComponenten?has_content && vermogensComponenten.vastgesteldeEigenWoning?has_content>
            <#assign totaal = 0 />
            <#list vermogensComponenten.vastgesteldeEigenWoning as eigenWoning>
                <#assign totaal = totaal + (eigenWoning.rekenwaarde)!0 />
                <#if eigenWoning.componenten?has_content>
                    <td>Waarde</td>
                    <td>
                        <#assign eigenWoningComponent = eigenWoning.componenten?filter(c -> c.soortVermogenscomponent.code == "1") />
                        <#assign woning = (eigenWoningComponent?has_content)?then(eigenWoningComponent?first, {}) />

                        <#if woning?has_content && woning.waarde??>
                            &euro; ${woning.waarde?string("0.00")}
                        <#else>
                            -
                        </#if>
                    </td>
                    <td>
                        <#assign eigenWoningComponent = eigenWoning.componenten?filter(c -> c.soortVermogenscomponent.code == "17") />
                        <#assign woning = (eigenWoningComponent?has_content)?then(eigenWoningComponent?first, {}) />

                        <#if woning?has_content && woning.waarde??>
                            &euro; ${woning.waarde?string("0.00")}
                        <#else>
                            -
                        </#if>
                    </td>
                    <td></td>
                    <td><#if eigenWoning.vrijlating??>&euro; ${eigenWoning.vrijlating?string("0.00")}<#else>-</#if></td>
                    <td><#if eigenWoning.nettoSubtotaal??>&euro; ${eigenWoning.nettoSubtotaal?string("0.00")}<#else>-</#if></td>
                </#if>
            </#list>

            <tr>
                <td><strong>Totaal</strong></td>
                <td colspan="5"><#if totaal??>&euro; ${totaal?string("0.00")}<#else>-</#if></td>
            </tr>
        <#else>
            <tr><td colspan="6">Geen eigen woning</td></tr>
        </#if>
    </table>
</#if>

<!-- ============================================================ -->
<!-- AANVULLENDE TOELICHTING                                      -->
<!-- ============================================================ -->
<table class="table">
    <tr class="divider">
        <td>Uitgebreide motivering</td>
    </tr>
    <tr>
        <td class="style-multiline" colspan="2" style="min-height: 60px; height: 60px;">${uitgebreideMotivering!""}</td>
    </tr>
</table>
</body>
</html>