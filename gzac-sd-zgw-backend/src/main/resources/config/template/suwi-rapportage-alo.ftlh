<#ftl output_format="HTML">
<!DOCTYPE html>
<html lang="nl">
<head>
    <meta charset="UTF-8"/>
    <title>SUWI Rapportage</title>

    <style>
        @page {
            size: A4;
            margin: 3cm 1cm 3cm 1cm;

            @top-center { content: element(page-header); }
            @bottom-center { content: element(page-footer); }
        }

        body {
            font-size: 12px;
        }

        #page-header {
            position: running(page-header);
            font-size: 12px;
            border-bottom: 1px solid #999;
            padding-bottom: 5px;
            line-height: 50px;
            height: 50px;
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

        .subonderwerp {
            width: 100%;
            background-color: #f4f7fc;
            color: #154273;
            border-left: 2px solid #154273;
            padding: 6px 12px;
            margin-bottom: 12px;
            font-weight: bold;
            font-size: 9pt;
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

        .suwinet {
            color: #005090;
            font-weight: bold;
        }

        .suwibron {
            color: #F54927;
            font-weight: bold;
        }

        .timestamp {
            float: right;
        }

        table.table {
            page-break-inside: avoid;
        }
    </style>
</head>

<body>
<#assign brp = (pv.brpPersoonsgegevensInfo)!{} />
<#assign uwv = (pv.uwvInfo)!{} />
<#assign inkomsten = (uwv.inkomsten)![] />
<#assign svb = (pv.svbUitkeringen)!{} />
<#assign uitkeringen = (svb.svbUitkeringen)![] />
<#assign duoStudiefinancieringInfo = (pv.duoStudiefinancieringInfo)!{} />
<#assign duoPersoonsInformatie = (pv.duoPersoonsInformatie)!{} />
<#assign bijstandsregelingenInfo = (pv.bijstandsregelingenInfo)!{} />
<#assign rdw = (pv.rdwVoertuigen)!{} />
<#assign voertuigen = (rdw.motorVoertuigen)![] />
<#assign kadaster = (pv.kadastraleObjecten)!{} />
<#assign onroerendeGoederen = (kadaster.onroerendeGoederen)![] />

<#assign fullName =
((brp.voornamen)!'')
+ ((brp.voorvoegsel)?has_content?then(' ' + brp.voorvoegsel,''))
+ ((brp.achternaam)?has_content?then(' ' + brp.achternaam,''))
/>
<#function showDate d>
    <#if !d?has_content>
        <#return "-" />
    </#if>

    <#assign s = d?string />

    <#if s?matches("^\\d{4}-\\d{2}-\\d{2}$")>
        <#assign y = s?substring(0, 4) />
        <#assign m = s?substring(5, 7) />
        <#assign day = s?substring(8, 10) />

        <#if m == "00" || day == "00">
            <#return day + "-" + m + "-" + y />
        <#else>
            <#return s?date("yyyy-MM-dd")?string("dd-MM-yyyy") />
        </#if>

    <#elseif s?matches("^\\d{8}$")>
        <#assign y = s?substring(0, 4) />
        <#assign m = s?substring(4, 6) />
        <#assign day = s?substring(6, 8) />

        <#if m == "00" || day == "00">
            <#return day + "-" + m + "-" + y />
        <#else>
            <#return s?date("yyyyMMdd")?string("dd-MM-yyyy") />
        </#if>
    </#if>

    <#return s />
</#function>


<#assign basisbeursCodes = {
"0": "Studiefinanciering afgewezen",
"1": "Recht op studiefinanciering",
"3": "Geen recht op studiefinanciering"
} />

<#assign partnertoeslagCodes = {
"1": "Geen recht op partnertoeslag",
"2": "Aanvraag op partnertoeslag is in behandeling",
"3": "Recht op partnertoeslag"
} />

<#assign eenOudertoeslagCodes = {
"1": "Geen recht op een-oudertoeslag",
"3": "Recht op een-oudertoeslag"
} />

<#assign jaNeeCodes = {
"1": "Ja",
"2": "Nee"
} />

<#assign szWetCodes = {
"AAW": "(voormalige) Algemene Arbeidsongeschiktheidswet",
"AB": "Algemene bijstand (regeling)",
"ABW": "Algemene Bijstandswet",
"AKW": "Algemene Kinderbijslagwet",
"ANW": "Algemene Nabestaandenwet",
"AOW": "Algemene Ouderdomswet",
"AOW-OBR": "AOW-Overbruggingsregeling",
"AOWOR": "AOW-Overbruggingsregeling",
"AWBZ": "Algemene Wet Bijzondere Ziektekosten",
"AWW": "(voormalige) Algemene Weduwen en Wezenwet",
"BB": "Bijzondere Bijstand",
"BBZ": "Besluit Bijstandverlening Zelfstandigen",
"BBZB": "Besluit Bijstandverlening Zelfstandigen – Bedrijfskapitaal",
"BBZBB": "Besluit Bijstandverlening Zelfstandigen – Bijzondere Bijstand",
"BBZL": "Besluit Bijstandverlening Zelfstandigen – Levensonderhoud",
"CSV": "Coördinatiewet Sociale Verzekering",
"INTW": "Internationale wetten",
"IOAW": "Wet Inkomensvoorziening Oudere en gedeeltelijk Arbeidsongeschikte Werkloze Werknemers",
"IOAZ": "Wet Inkomensvoorziening Oudere en gedeeltelijk Arbeidsongeschikte gewezen Zelfstandigen",
"IOW": "Wet Inkomensvoorziening Oudere Werklozen",
"IVA": "Regeling Inkomensvoorziening Volledig Arbeidsongeschikten",
"KB": "Koninklijk besluit en aanverwante regelingen",
"KOP": "Kopjesregeling",
"NWJ": "Wet werk en arbeidsondersteuning jonggehandicapten (Wet Wajong)",
"OSV": "Organisatiewet Sociale Verzekering",
"PVW": "pensioenwetten",
"PWJ": "Participatiewet Wajong",
"REA": "Wet op de (RE)integratie Arbeidsgehandicapten",
"RW": "Remigratiewet",
"RWW": "(voormalige) Rijksgroepregeling Werkloze Werknemers",
"TRI": "Tijdelijke Regeling Inkomensgevolgen herbeoordeelde arbeidsongeschikten",
"TW": "Toeslagenwet",
"VUT": "VUT-regeling",
"WAJ": "Wet Arbeidsongeschiktheidsvoorziening Jonggehandicapten (WAJONG)",
"WAMIL": "Wet Arbeidsongeschiktheidsvoorziening Militairen",
"WAO": "Wet op de Arbeidsongeschiktheidsverzekering",
"WAZ": "Wet Arbeidsongeschiktheidsverzekering Zelfstandigen",
"WAZO": "Wet Arbeid en Zorg",
"WBIA": "(Tijdelijke Wet) Beperking Inkomensgevolgen Arbeidsongeschiktheidscriteria",
"WGA": "Regeling Werkhervatting Gedeeltelijk Arbeidsgeschikten",
"WHK": "Werkhervattingskas",
"WIA": "Wet Werk en Inkomen naar Arbeidsvermogen",
"WIJ": "Wet Investeren in Jongeren",
"WMO": "Wet Maatschappelijke Ondersteuning",
"WSW": "Wet Sociale Werkvooriening",
"WVG": "Wet Voorzieningen Gehandicapten",
"WW": "Werkloosheidswet",
"WWAJ": "Wet Werk en Arbeidsondersteuning Jonggehandicapten (Wet Wajong)",
"WWB": "Wet Werk en Bijstand",
"WWF": "Werkloosheidswet Faillissementen",
"WWIK": "Wet Werk en Inkomen Kunstenaars",
"ZFW": "Ziekenfondswet",
"ZVW": "Zorgverzekeringswet",
"ZW": "Ziektewet"
} />

<#assign beslissingCodes = {
"01": "Toekenning",
"02": "Intrekking",
"03": "Buiten behandeling",
"04": "Afwijzing",
"09": "Niet van toepassing"
} />

<#assign clusterBijzBijstandCodes = {
"b": "Voorzieningen voor het huishouden",
"c": "Voorzieningen voor het wonen",
"d": "Voorzieningen voor het opvang",
"e": "Kosten uit maatschappelijke zorg",
"f": "Financiële transacties",
"g": "Uitstroombevordering",
"h": "Medische dienstverlening",
"i": "Overige kostensoorten",
"j": "Kosten wel in statistiek maar geen bijzondere bijstand",
"k": "Individuele inkomenstoeslag",
"l": "Individuele studietoeslag",
"m": "Collectieve aanvullende zorgverzekering"
} />

<#assign redenVorderingCodes = {
"1": "Verlaging/boete/maatregel als terugvordering geboekt",
"2": "Verlaging/boete/maatregel niet als terugvordering geboekt",
"11": "Vanwege onverschuldigde betaling; niet verwijtbaar aan persoon",
"12": "Vanwege verleend voorschot",
"13": "Vanwege onverschuldigde betaling; wel verwijtbaar aan persoon",
"14": "Vanwege later ter beschikking gekomen middelen",
"15": "Vanwege niet nakomen verplichtingen bij lening",
"16": "Vanwege niet nakomen re-integratieverplichting",
"21": "Verhaal op onderhoudsplichtige",
"22": "Verhaal overig",
"31": "Vanwege lening voor duurzame gebruiksgoederen",
"32": "Vanwege lening voor bedrijfskapitaal",
"33": "Vanwege lening voor levensonderhoud bij zelfstandige",
"34": "Vanwege verkrijgen van krediethypotheek",
"35": "Overige leningen (excl vorderingen die als gevolg van een wijziging in de betaalddata als overbrugging aan de cliënt is verstrekt)",
"41": "Gecedeerde alimentatie",
"42": "Vanwege overige cederingen",
"43": "Vanwege rente",
"44": "Vanwege door te belasten invorderingskosten",
"49": "Overige gronden (incl vorderingen die agv een wijziging in de betaaldata als overbrugging aan de cliënt is verstrekt)",
"51": "Fraude: verzwijgen witte inkomsten",
"52": "Fraude: verzwijgen zwarte inkomsten",
"53": "Fraude: verzwijgen vermogen en of inkomsten uit vermogen",
"54": "Fraude: onjuiste opgave woonadres",
"55": "Fraude: onjuiste opgave samenstelling huishouden",
"56": "Andere fraude",
"57": "Onverschuldigde betaling (vordering vanaf 01-01-2013)",
"58": "Boete wegens fraude",
"59": "Boete niet wegens fraude",
"60": "Lening (vordering vanaf 01-01-2013)",
"61": "Krediethypotheek (vordering vanaf 01-01-2013)",
"62": "Verhaal op onderhoudsplichtige voor kind (vordering vanaf 01-01-2013)",
"63": "Verhaal op onderhoudsplichtige voor ex-partner (vordering vanaf 01-01-2013)",
"64": "Rente en incassokosten (vordering vanaf 01-01-2013)",
"65": "Overig (vordering vanaf 01-01-2013)",
"81": "Onverschuldigd betaald verwijtbaar (vordering vóór 01-01-2013)",
"82": "Onverschuldigd betaald niet verwijtbaar (vordering vóór 01-01-2013)",
"83": "Lening (vordering vóór 01-01-2013)",
"84": "Rente en incassokosten (vordering vóór 01-01-2013)",
"85": "Overig (vordering vóór 01-01-2013)"
} />


<div id="page-header">
    <span class="suwinet">SUWI Rapportage</span>&nbsp;<span class="timestamp">${.now?string("dd-MM-yyyy HH:mm:ss")}</span>
</div>
<div id="page-footer"><span class="page-count"></span></div>

<#assign persoon = (pv.brpPersoonsgegevensInfo)!{} />
<#assign partner = (pv.brpPersoonsgegevensPartnerInfo)!{} />
<#assign kinderen = (pv.brpPersoonsgegevensKinderenInfo)![] />

<#macro fullName x>
    <#local nm = ((x.voornamen)!'')
    + ((x.voorvoegsel)?has_content?then(' ' + x.voorvoegsel,''))
    + ((x.achternaam)?has_content?then(' ' + x.achternaam,''))
    />
    ${nm?trim?has_content?then(nm?trim,'-')}
</#macro>

<#macro housePart adr>
    <#local hp =
    ((adr.huisnummer)!'')?string
    + ((adr.huisletter)?has_content?then(adr.huisletter,''))
    + ((adr.huisnummertoevoeging)?has_content?then(' ' + adr.huisnummertoevoeging,''))
    />
    ${hp?trim}
</#macro>

<#macro adresRegel adr>
    ${(adr.straatnaam)!'-'} <@housePart adr=adr />
    <#if (adr.postcode)?has_content || (adr.woonplaatsnaam)?has_content>
        <br/>${(adr.postcode)!''} ${(adr.woonplaatsnaam)!''}
    </#if>
</#macro>

<#macro addr adr>
    <#if adr?has_content>
        ${adr.straatnaam!""} ${adr.huisnummer!""}<#if (adr.huisletter)?has_content>${adr.huisletter}</#if><#if (adr.huisnummertoevoeging)?has_content> ${adr.huisnummertoevoeging}</#if><br/>
        ${adr.postcode!""} ${adr.woonplaatsnaam!""}
    <#else>
        -
    </#if>
</#macro>

<#function euroFromCents cents>
    <#local raw =
    (cents?is_number)
    ? then(cents?c, (cents!'')?string)
    />

    <#local normalized =
    raw?trim
    ?replace(" ", "")
    ?replace(".", "")   <#-- strip thousands separators if they exist -->
    ?replace(",", ".") <#-- allow decimal comma -->
    />

    <#local c = (normalized?has_content)?then(normalized?number, 0) />
    <#local e = c / 100 />
    <#return "€ " + e?string["0.00"]?replace(".", ",") />
</#function>


<#macro persoonBlok titel p>
    <#assign adr = (p.adresBrp)!{} />
    <#assign v = (p.verblijfstitel)!{} />
    <#assign vCode = (v.codeVerblijfstitel)!{} />

    <div class="sectie">
        <div class="onderwerp">${titel} – <@fullName x=p /> (BSN: ${(p.bsn)!'-'})</div>

        <div class="sectie">
            <h2>Persoon</h2>
            <table class="persoon-table">
                <tr><td class="label">Naam:</td><td class="value"><@fullName x=p /></td></tr>
                <tr><td class="label">Geboortedatum:</td><td class="value">${(p.geboortedatum?has_content)?then(showDate(p.geboortedatum), "-")}</td></tr>
                <tr><td class="label">BSN:</td><td class="value">${(p.bsn)!'-'}</td></tr>
                <tr><td class="label">A-nummer:</td><td class="value">${(p.anummer)!'-'}</td></tr>
            </table>
        </div>

        <div class="sectie">
            <h2>BRP adres</h2>
            <table class="persoon-table">
                <tr><td class="label">Adres:</td><td class="value"><@adresRegel adr=adr /></td></tr>
                <#if (adr.aanduidingBijHuisnummer)?has_content>
                    <tr><td class="label">Aanduiding:</td><td class="value">${adr.aanduidingBijHuisnummer}</td></tr>
                </#if>
                <#if (adr.locatieomschrijving)?has_content>
                    <tr><td class="label">Locatieomschrijving:</td><td class="value">${adr.locatieomschrijving}</td></tr>
                </#if>
            </table>
        </div>

        <div class="sectie">
            <h2>Verblijfstitel</h2>
            <table class="persoon-table">
                <tr><td class="label">Code:</td><td class="value">${(vCode.code)!'-'}</td></tr>
                <tr><td class="label">Omschrijving:</td><td class="value">${(vCode.name)!''}</td></tr>
                <tr><td class="label">Datum aanvang:</td><td class="value">${(v.datumAanvangVerblijfstitel?has_content)?then(showDate(v.datumAanvangVerblijfstitel), "-")}</td></tr>
                <tr><td class="label">Datum einde:</td><td class="value">${(v.datumEindeVerblijfstitel?has_content)?then(showDate(v.datumEindeVerblijfstitel), "-")}</td></tr>
            </table>
        </div>

        <div class="sectie">
            <h2>Nationaliteiten</h2>
            <#if (p.nationaliteiten)?has_content>
                <table class="table">
                    <thead><tr><th>Naam</th></tr></thead>
                    <tbody>
                    <#list p.nationaliteiten as n>
                        <tr><td>${(n.name)!''}</td></tr>
                    </#list>
                    </tbody>
                </table>
            <#else>
                <p>-</p>
            </#if>
        </div>

        <div class="sectie">
            <h2>Verblijfplaats historisch</h2>
            <#if (p.verblijfplaatsHistorisch)?has_content>
                <table class="table">
                    <thead>
                    <tr>
                        <th>Type</th>
                        <th>Adres</th>
                        <th>Datum begin adreshouding</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list p.verblijfplaatsHistorisch as vh>
                        <#assign vhAdr = (vh.adres)!{} />
                        <#assign vhLine =
                        ((vhAdr.straatnaam)!'')
                        + (((vhAdr.huisnummer)!'')?string?has_content?then(' ' + ((vhAdr.huisnummer)!'')?string,''))
                        + ((vhAdr.huisletter)?has_content?then(vhAdr.huisletter,''))
                        + ((vhAdr.huisnummertoevoeging)?has_content?then(' ' + vhAdr.huisnummertoevoeging,''))
                        + ((vhAdr.postcode)?has_content?then(', ' + vhAdr.postcode,''))
                        + ((vhAdr.woonplaatsnaam)?has_content?then(' ' + vhAdr.woonplaatsnaam,''))
                        />
                        <tr>
                            <td>${(vh.type)!''}</td>
                            <td>${vhLine?trim?has_content?then(vhLine?trim,'-')}</td>
                            <td>${(vh.datumBeginAdreshouding?has_content)?then(showDate(vh.datumBeginAdreshouding), "-")}</td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            <#else>
                <p>-</p>
            </#if>
        </div>
    </div>
</#macro>

<#macro partnerBlok titel p>
    <#assign adr = (p.adresBrp)!{} />
    <#assign v = (p.verblijfstitel)!{} />
    <#assign vCode = (v.codeVerblijfstitel)!{} />

    <div class="sectie">
        <div class="onderwerp">${titel} – <@fullName x=p /> (BSN: ${(p.bsn)!'-'})</div>

        <div class="sectie">
            <h2>Persoon</h2>
            <table class="persoon-table">
                <tr><td class="label">Naam:</td><td class="value"><@fullName x=p /></td></tr>
                <tr><td class="label">Geboortedatum:</td><td class="value">${(p.geboortedatum?has_content)?then(showDate(p.geboortedatum), "-")}</td></tr>
                <tr><td class="label">BSN:</td><td class="value">${(p.bsn)!'-'}</td></tr>
            </table>
        </div>
    </div>
</#macro>

<@persoonBlok titel="BRP persoon" p=persoon />

<#if partner?has_content && (partner.bsn)?has_content>
    <div style="break-before: always;"></div>
    <@partnerBlok titel="BRP partner" p=partner />
</#if>

<#if kinderen?has_content>
    <div style="break-before: always;"></div>

    <div class="onderwerp">BRP kinderen</div>

    <table class="table">
        <thead>
        <tr>
            <th>Naam</th>
            <th>Geboortedatum</th>
            <th>BSN</th>
        </tr>
        </thead>
        <tbody>
        <#list kinderen as k>
            <tr>
                <td><@fullName x=k /></td>
                <td>${(k.geboortedatum?has_content)?then(showDate(k.geboortedatum), "-")}</td>
                <td>${(k.bsn)!''}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</#if>
<div style="margin-top:25px"></div>
<div style="break-before: always;">
    <div class="onderwerp">
        UWV
    </div>

    <div class="sectie">
        <#if inkomsten?has_content>
            <div class="subonderwerp" style="margin-bottom:10px;">
                Inkomstenverhoudingen
            </div>
            <table class="table" style="margin-bottom:10px;">
                <thead>
                <tr>
                    <th>Inhoudingsplichtige</th>
                    <th>Soort Inkomstenverhouding</th>
                    <th>Van</th>
                    <th>Tot</th>
                    <th>Laatste inkomstenopgave</th>
                </tr>
                </thead>
                <tbody>
                    <#list inkomsten as inkomsten>
                        <#assign eersteOpgave = (inkomsten.opgaven?has_content)?then(inkomsten.opgaven?first, {}) />
                        <#assign laatsteOgave = (inkomsten.opgaven?has_content)?then(inkomsten.opgaven?last, {}) />
                        <tr>
                            <td>${inkomsten.naamRechtspersoon!""}</td>
                            <td>
                                <#if eersteOpgave?has_content && eersteOpgave.codeSoortIkv?has_content && eersteOpgave.codeSoortIkv.name?has_content>
                                    <#if eersteOpgave.codeSoortIkv.name == "not found">
                                        Geen Suwinet-informatie beschikbaar
                                    <#else>
                                        ${eersteOpgave.codeSoortIkv.name}
                                    </#if>
                                <#else>
                                    -
                                </#if>
                            </td>
                            <td>${(inkomsten.datumBeginIkv?has_content)?then(showDate(inkomsten.datumBeginIkv), "-")}</td>
                            <td>${(inkomsten.datumEindIkv?has_content)?then(showDate(inkomsten.datumEindIkv), "-")}</td>
                            <td>
                                <#if laatsteOgave?has_content && laatsteOgave.datumEindOpgave?has_content>
                                    ${showDate(laatsteOgave.datumEindOpgave)}
                                <#else>
                                    -
                                </#if>
                            </td>
                        </tr>
                    </#list>
                </tbody>
            </table>
        </#if>

        <h2>Inkomsten (per werkgever/instantie)</h2>
        <#if inkomsten?has_content>
            <#list inkomsten as ikv>
                <#assign opgaven = (ikv.opgaven)![] />
                <#assign eersteOpgave = (ikv.opgaven?has_content)?then(ikv.opgaven?first, {}) />
                <#assign totaalCents = 0 />

                <#list opgaven as o>
                    <#assign totaalCents = totaalCents + ((o.brutoSocialeVerzekeringsLoon)!0) />
                </#list>

                <div class="subonderwerp" style="margin-bottom:10px;">
                    ${(ikv.naamRechtspersoon)!'Onbekend'} <span class="muted small">(Loonheffingennr: ${(ikv.loonheffingennummer)!'-'})</span>
                </div>

                <table class="persoon-table" style="margin-bottom:10px;">
                    <tr>
                        <td class="label">Inhoudingsplichtige</td>
                        <td class="value">${ikv.naamRechtspersoon!""}</td>
                    </tr>
                    <tr>
                        <td class="label">Loonheffingsnummer</td>
                        <td class="value">${ikv.loonheffingennummer!""}</td>
                    </tr>
                    <tr>
                        <td class="label">Adres</td>
                        <td class="value"><@addr adr=(ikv.straatadres)!{} /></td>
                    </tr>
                    <tr>
                        <td class="label">Sector</td>
                        <td class="value">${ikv.cdSector!""}</td>
                    </tr>
                    <tr>
                        <td class="label">Soort inkomstenverhouding</td>
                        <td class="value">
                            <#if eersteOpgave?has_content && eersteOpgave.codeSoortIkv?has_content && eersteOpgave.codeSoortIkv.name?has_content>
                                <#if eersteOpgave.codeSoortIkv.name == "not found">
                                    Geen Suwinet-informatie beschikbaar
                                <#else>
                                    ${eersteOpgave.codeSoortIkv.name}
                                </#if>
                            <#else>
                                -
                            </#if>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">Arbeidsovereenkomst onbepaalde tijd</td>
                        <td class="value">
                            <#if eersteOpgave?has_content && eersteOpgave.codeSoortArbeidscontract?has_content && eersteOpgave.codeSoortArbeidscontract.name?has_content>
                                <#if eersteOpgave.codeSoortArbeidscontract.name == "Onbepaalde tijd">
                                    Ja
                                <#else>
                                    Nee
                                </#if>
                            <#else>
                                -
                            </#if>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">Oproepovereenkomst</td>
                        <td class="value">
                            <#if eersteOpgave?has_content && eersteOpgave.indRegelmatigArbeidspatroon?has_content && eersteOpgave.indRegelmatigArbeidspatroon.name?has_content>
                                <#if eersteOpgave.indRegelmatigArbeidspatroon.name == "Nee">
                                    Ja
                                <#else>
                                    Nee
                                </#if>
                            <#else>
                                -
                            </#if>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">Loonheffingskorting toegepast</td>
                        <td class="value">
                            <#if eersteOpgave?has_content && eersteOpgave.indLoonheffingskortingToegepast?has_content && eersteOpgave.indLoonheffingskortingToegepast.name?has_content>
                                <#if eersteOpgave.indLoonheffingskortingToegepast.name == "Ja">
                                    Ja
                                <#else>
                                    Nee
                                </#if>
                            <#else>
                                -
                            </#if>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">Loon is (mede) AOW voor alleenstaande</td>
                        <td class="value">
                            <#if eersteOpgave?has_content && eersteOpgave.indLoonIsMedeAowAlleenstaande?has_content && eersteOpgave.indLoonIsMedeAowAlleenstaande.name?has_content>
                                <#if eersteOpgave.indLoonIsMedeAowAlleenstaande.name == "not found">
                                    Geen Suwinet-informatie beschikbaar
                                <#else>
                                    ${eersteOpgave.indLoonIsMedeAowAlleenstaande.name}
                                </#if>
                            <#else>
                                -
                            </#if>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">Loon is inclusief WAJONG</td>
                        <td class="value">
                            <#if eersteOpgave?has_content && eersteOpgave.indLoonInclusiefWajongUitkering?has_content && eersteOpgave.indLoonInclusiefWajongUitkering.name?has_content>
                                <#if eersteOpgave.indLoonInclusiefWajongUitkering.name == "not found">
                                    Geen Suwinet-informatie beschikbaar
                                <#else>
                                    ${eersteOpgave.indLoonInclusiefWajongUitkering.name}
                                </#if>
                            <#else>
                                -
                            </#if>
                        </td>
                    </tr>
                </table>

                <#if opgaven?has_content>
                    <table class="table">
                        <thead>
                        <tr>
                            <th>Van</th>
                            <th>Tot en met</th>
                            <th>Verloonde uren</th>
                            <th>Aantal SV dagen</th>
                            <th>Brutoloon SV</th>
                            <th>Loon LB/PVV</th>
                            <th>Ingehouden LB/PVV</th>
                            <th>Vakantietoeslag</th>
                            <th>Opgebouwde Vakantietoeslag</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list opgaven as o>
                            <tr>
                                <td>${showDate(o.datumAanvangOpgave)}</td>
                                <td>${showDate(o.datumEindOpgave)}</td>
                                <td>${(o.aantalVerloondeUren)!0}</td>
                                <td>${(o.aantalSvDagen)!0}</td>
                                <td>${euroFromCents((o.brutoSocialeVerzekeringsLoon)!0)}</td>
                                <td>${euroFromCents((o.loonLbPremieVolksverzekering)!0)}</td>
                                <td>${euroFromCents((o.ingehoudenLbPremieVolksverzekering)!0)}</td>
                                <td>${euroFromCents((o.vakantietoeslag)!0)}</td>
                                <td>${euroFromCents((o.opgbRechtVakantietoeslag)!0)}</td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                <#else>
                    <p>-</p>
                </#if>

                <div style="page-break-inside: avoid; margin-bottom: 18px;"></div>
            </#list>
        <#else>
            <p>-</p>
        </#if>
    </div>
</div>


<div class="onderwerp">
    SVB
</div>

<div class="sectie">
    <h2>SVB-uitkeringen</h2>

    <#if uitkeringen?has_content>
        <#list uitkeringen as u>
            <#assign periodes = (u.periodes)![] />

            <div class="subonderwerp" style="margin-bottom:10px;">
                Wet: ${(u.codeSzWet)!'-'}
                <span class="muted small">
                    (Verhouding: ${showDate(u.datumBeginUitkeringsverhouding)} <span class="muted">t/m</span>
                    ${((u.datumEindUitkeringsverhouding)?has_content)?then(showDate(u.datumEindUitkeringsverhouding), "-")})
                </span>
            </div>

            <#if periodes?has_content>
                <table class="table">
                    <thead>
                    <tr>
                        <th>Periode</th>
                        <th>Frequentie</th>
                        <th>Bruto</th>
                        <th>Netto</th>
                        <th>Valuta</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list periodes as p>
                        <tr>
                            <td>${showDate(p.datumAanvangUitkeringsperiode)} <span class="muted">t/m</span> ${showDate(p.datumEindUitkeringsperiode)}</td>
                            <td>${(p.codeUitkeringsperiode.name)!'-'}</td>
                            <td>${euroFromCents((p.brutoBedrag)!0)}</td>
                            <td>${euroFromCents((p.nettoBedrag)!0)}</td>
                            <td>${(p.codeMunteenheid)!'EUR'}</td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            <#else>
                <p>-</p>
            </#if>

            <div style="page-break-inside: avoid; margin-bottom: 18px;"></div>
        </#list>
    <#else>
        <p>-</p>
    </#if>
</div>

<div class="onderwerp">
    DUO
</div>

<div class="sectie">

    <h2>DUO persoonsgegevens</h2>
    <div>Startkwalificatie: <#if duoPersoonsInformatie.indicatieStartkwalificatieDuo?has_content>${duoPersoonsInformatie.indicatieStartkwalificatieDuo} <#else></#if></div>

    <h2>Inschrijvingen</h2>
    <#if duoPersoonsInformatie.onderwijsOvereenkomst?has_content>
        <#list duoPersoonsInformatie.onderwijsOvereenkomst as onderwijsovereenkomsten>
            <div>Datum inschrijving: ${showDate(onderwijsovereenkomsten.datumInschrijvingOpleiding)}</div>
            <div>Datum uitschrijving: ${showDate(onderwijsovereenkomsten.datumUitschrijvingOpleiding)}</div>
            <table class="table">
                <thead>
                <tr>
                    <th>Van</th>
                    <th>Tot</th>
                    <th>Niveau</th>
                    <th>Opleiding</th>
                </tr>
                </thead>
                <tbody>
                <#list onderwijsovereenkomsten.deelnameOpleidingGeregistrDuo as inschrijvingen>
                    <tr>
                        <td>${showDate(inschrijvingen.datumBeginDeelnameOpleiding)}</td>
                        <td>${showDate(inschrijvingen.datumEindDeelnameOpleiding)}</td>
                        <td>${(inschrijvingen.inhoudDeelnameOpleiding.codeNiveauOpleidingDuo)!}</td>
                        <td>${(inschrijvingen.inhoudDeelnameOpleiding.naamOpleidingKortDuo)!''}</td>                    </tr>
                </#list>
                </tbody>
            </table>
        </#list>

    <#else>
        <div>Er zijn geen inschrijvingen bekend.</div>
    </#if>


    <#if duoPersoonsInformatie.resultaatOpleiding?has_content>
        <div style="break-before: always;"></div>
        <h2>Resultaten</h2>
        <#list duoPersoonsInformatie.resultaatOpleiding as resultaten>
            <table class="table">
                <thead>
                <tr>
                    <th>Datum</th>
                    <th>Resultaat</th>
                    <th>Niveau</th>
                    <th>Fase</th>
                    <th>Opleiding</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>${showDate(resultaten.resultaatExamen.datumResultaatExamen)}</td>
                    <td>${(resultaten.resultaatExamen.codeResultaatExamen)!}</td>
                    <td>${(resultaten.inhoudResultaatOpleidingDuo.codeNiveauOpleidingDuo)!}</td>
                    <td>${(resultaten.codeFaseOpleidingDuo)!}</td>
                    <td>${(resultaten.inhoudResultaatOpleidingDuo.naamOpleidingKortDuo)!}</td>
                </tr>
                </tbody>
            </table>
        </#list>

    <#else>
        <div>Er zijn geen resultaten bekend.</div>
    </#if>
</div>

<div class="sectie">
    <h2>DUO studiefinancieringen</h2>

    <#if duoStudiefinancieringInfo.studiefinancieringen?has_content>
        <table class="table">
            <thead>
            <tr>
                <th>StartDatum</th>
                <th>Einddatum</th>
                <th>Basisbeurs</th>
                <th>Aanvullende beurs</th>
                <th>WTOS</th>
                <th>Partnertoeslag</th>
                <th>Oudertoeslag</th>
            </tr>
            </thead>
            <tbody>
            <#list duoStudiefinancieringInfo.studiefinancieringen as studiefinancieringen>
                <tr>
                    <td>${showDate(studiefinancieringen.dateBeginToekenningsperiodeStufi)}</td>
                    <td>${showDate(studiefinancieringen.dateEindToekenningsperiodeStufi)}</td>
                    <td>${basisbeursCodes[(studiefinancieringen.codeToekenningBasisbeursStufi)!'']!'-'}</td>
                    <td>${jaNeeCodes[(studiefinancieringen.indicatieAanvullendeBeursStufi)!'']!'-'}</td>
                    <td>${jaNeeCodes[(studiefinancieringen.indicatieToekenningWtosVo18)!'']!'-'}</td>
                    <td>${partnertoeslagCodes[(studiefinancieringen.codeStatusPartnertoeslagStufi)!'']!'-'}</td>
                    <td>${eenOudertoeslagCodes[(studiefinancieringen.codeStatusEenOudertoeslagStufi)!'']!'-'}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    <#else>
        <div>Er zijn geen studiefinancieringsgegevens beschikbaar.</div>
    </#if>
</div>

<div class="onderwerp">
    GSD
</div>

<div class="sectie">
    <#if bijstandsregelingenInfo.aanvraagUitkeringen?has_content>
        <h2>Bijstandsregelingen</h2>
        <table class="table">
            <thead>
            <tr>
                <th>Uitkeringsinstantie</th>
                <th>Soort uitkering</th>
                <th>Datum aanvraag</th>
                <th>Status</th>
                <th>Datum beslissing</th>
                <th>Beslissing</th>
                <th>Partner</th>
                <th>Reden afwijzing vanuit UWV</th>
            </tr>
            </thead>
            <tbody>
            <#list bijstandsregelingenInfo.aanvraagUitkeringen as aanvraagUitkeringen>
                <tr>
                    <#assign bronPartij = (aanvraagUitkeringen.bron.cdPartijSuwi)!''>
                    <#assign bronVestiging = (aanvraagUitkeringen.bron.cdVestigingSuwi)!''>
                    <td>${(bronPartij?has_content || bronVestiging?has_content)?then(bronPartij + ' ' + bronVestiging, '-')}</td>
                    <td>${szWetCodes[(aanvraagUitkeringen.szWet.cdSzWet)!'']!'-'}</td>
                    <#assign datAanvraag = (aanvraagUitkeringen.datAanvraagUitkering)![] />
                    <#assign datAanvraagStr = datAanvraag?has_content?then(datAanvraag?map(x -> x?c)?join("-"), "") />
                    <td>${datAanvraagStr?has_content?then(showDate(datAanvraagStr), '-')}</td>
                    <td>${(aanvraagUitkeringen.status)!'-'}</td>
                    <#assign datumBeslissing = (aanvraagUitkeringen.beslissingOpAanvraagUitkering.datDagtekeningBeslisOpAanvrUitk)![] />
                    <#assign datumBeslissingStr = datumBeslissing?has_content?then(datumBeslissing?map(x -> x?c)?join("-"), "") />
                    <td>${datumBeslissingStr?has_content?then(showDate(datumBeslissingStr), '-')}</td>
                    <td>${beslissingCodes[(aanvraagUitkeringen.beslissingOpAanvraagUitkering.cdBeslissingOpAanvraagUitkering)!'']!'-'}</td>
                    <#assign partnerVoorletters = (aanvraagUitkeringen.partnerAanvraagUitkering.voorletters)!''>
                    <#assign partnerAchternaam = (aanvraagUitkeringen.partnerAanvraagUitkering.significantDeelVanDeAchternaam)!''>
                    <td>${(partnerVoorletters?has_content || partnerAchternaam?has_content)?then(partnerVoorletters + ' ' + partnerAchternaam, '-')}</td>
                    <td>${(aanvraagUitkeringen.afwijzing)!'-'}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    <#else>
        <div>Er zijn geen studiefinancieringsgegevens beschikbaar.</div>
    </#if>
</div>

<div class="sectie">
    <#if bijstandsregelingenInfo.specifiekeGegevensBijzBijstandList?has_content>
        <h2>Bijzondere bijstand</h2>
        <table class="table">
            <thead>
            <tr>
                <th>Uitkeringsinstantie</th>
                <th>Datum betaalbaarstelling</th>
                <th>Cluster van kosten</th>
                <th>Omschrijving kosten</th>
            </tr>
            </thead>
            <tbody>
            <#list bijstandsregelingenInfo.specifiekeGegevensBijzBijstandList as bijzondereBijstand>
                <tr>
                    <#assign bronPartij = (bijzondereBijstand.bron.cdPartijSuwi)!''>
                    <#assign bronVestiging = (bijzondereBijstand.bron.cdVestigingSuwi)!''>
                    <td>${(bronPartij?has_content || bronVestiging?has_content)?then(bronPartij + ' ' + bronVestiging, '-')}</td>
                    <#assign datumBetaalbaarstelling = (bijzondereBijstand.datBetaalbaarBijzBijstand)![] />
                    <#assign datumBetaalbaarstellingStr = datumBetaalbaarstelling?has_content?then(datumBetaalbaarstelling?map(x -> x?c)?join("-"), "") />
                    <td>${datumBetaalbaarstellingStr?has_content?then(showDate(datumBetaalbaarstellingStr), '-')}</td>
                    <td>${clusterBijzBijstandCodes[(bijzondereBijstand.cdClusterBijzBijstand)!'']!'-'}</td>
                    <td>${(bijzondereBijstand.omsSrtKostenBijzBijstand)!'-'}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    <#else>
        <div>Er zijn geen bijzondere bijstand gegevens beschikbaar.</div>
    </#if>
</div>

<div class="sectie">
    <#if bijstandsregelingenInfo.vorderingen?has_content>
        <h2>Vorderingen</h2>
        <table class="table">
            <thead>
            <tr>
                <th>Uitkeringsinstantie</th>
                <th>Soort uitkering</th>
                <th>Reden vordering</th>
                <th>Partner</th>
            </tr>
            </thead>
            <tbody>
            <#list bijstandsregelingenInfo.vorderingen as vorderingen>
                <tr>
                    <#assign bronPartij = (vorderingen.bron.cdPartijSuwi)!''>
                    <#assign bronVestiging = (vorderingen.bron.cdVestigingSuwi)!''>
                    <td>${(bronPartij?has_content || bronVestiging?has_content)?then(bronPartij + ' ' + bronVestiging, '-')}</td>
                    <td>${szWetCodes[(vorderingen.szWet.cdSzWet)!'']!'-'}</td>
                    <td>${redenVorderingCodes[(vorderingen.cdRedenVordering)!'']!'-'}</td>
                    <#assign partnersVordering = (vorderingen.partnersVordering)![] />
                    <td>${partnersVordering?has_content?then(partnersVordering?map(x -> (x.burgerservicenr)!'')?filter(x -> x?has_content)?join(", "), '-')}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    <#else>
        <div>Er zijn geen vorderingen beschikbaar.</div>
    </#if>
</div>

<div class="onderwerp">
    RDW
</div>

<div class="sectie">
    <h2>Voertuigen</h2>

    <#if voertuigen?has_content>
        <table class="table">
            <thead>
            <tr>
                <th>Kenteken</th>
                <th>Soort</th>
                <th>Merk</th>
                <th>Model</th>
                <th>Datum eerste inschrijving</th>
                <th>Datum registratie aansprakelijkheid</th>
            </tr>
            </thead>
            <tbody>
            <#list voertuigen as v>
                <tr>
                    <td>${(v.kenteken)!'-'}</td>
                    <td>
                        ${(v.soortMotorvoertuig.name)!''}
                        <#if (v.soortMotorvoertuig.code)?has_content>
                            <div class="muted small">${v.soortMotorvoertuig.code}</div>
                        </#if>
                    </td>
                    <td>${(v.merk)!'-'}</td>
                    <td>${(v.model)!'-'}</td>
                    <td>${showDate((v.datumEersteInschrijving)!'')}</td>
                    <td>${showDate((v.datumRegistratieAansprakelijkheid)!'')}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    <#else>
        <p>-</p>
    </#if>
</div>

<div class="onderwerp">
    Kadaster
</div>

<div class="sectie">
    <h2>Onroerende goederen</h2>

    <#if onroerendeGoederen?has_content>
        <#list onroerendeGoederen as og>
            <#assign kad = (og.kadastraleAanduiding)!{} />
            <#assign beperkingen = (og.publiekrechtelijkeBeperking)![] />

            <div class="subonderwerp" style="margin-bottom:10px;">
                ${(kad.kadastraleGemeentenaam)!''}
                ${(kad.kadastraleSectie)!''}
                ${(kad.kadastraalPerceelnr)?c!''}
                <#if (kad.volgnrKadastraalAppartementsrecht)?has_content && (kad.volgnrKadastraalAppartementsrecht)?string != '0'> / ${kad.volgnrKadastraalAppartementsrecht}</#if>
                <span class="muted small">(${(kad.codeKadastraleGemeente)!'-'})</span>
            </div>

            <table class="table" style="margin-bottom:10px;">
                <tr>
                    <td class="label">Type</td>
                    <td class="value">${(og.codeTypeOnroerendeZaak)!'-'}</td>
                </tr>
                <tr>
                    <td class="label">Datum ontstaan</td>
                    <td class="value">${showDate((og.datumOntstaan)!'')}</td>
                </tr>
                <tr>
                    <td class="label">Omschrijving</td>
                    <td class="value">${(og.omschrijving)!'-'}</td>
                </tr>
                <tr>
                    <td class="label">Omschrijving zakelijkrecht</td>
                    <td class="value">
                        ${(og.zakelijkRecht.omschrijvingZakelijkRecht)!'-'}
                        <#if (og.zakelijkRecht.datumEZakelijkRecht)?has_content>
                            <div class="muted small">Einddatum: ${showDate(og.zakelijkRecht.datumEZakelijkRecht)}</div>
                        </#if>
                    </td>
                </tr>
                <tr>
                    <td class="label">Beperkingen</td>
                    <td class="value">
                        <#if beperkingen?has_content>
                            <#list beperkingen as b>
                                ${showDate((b.aantekeningKadastraal.datumEAantekeningKadastraal)!'')}
                                ${(b.aantekeningKadastraal.omschrijvingAantekeningKadastraal)!'-'}
                            </#list>
                        </#if>
                    </td>
                </tr>
                <tr>
                    <td class="label">Indicatie meer gerechtigden</td>
                    <td class="value">${((((og.indicatieMeerGerechtigden)!'0') == '1'))?then('Ja', 'Nee')}</td>
                </tr>
                <tr>
                    <td class="label">Adres</td>
                    <td class="value"><@addr adr=(og.locatieOz)!{} /></td>
                </tr>
            </table>
            <div style="page-break-inside: avoid; margin-bottom: 18px;"></div>
        </#list>
    <#else>
        <p>-</p>
    </#if>
</div>

</body>
</html>