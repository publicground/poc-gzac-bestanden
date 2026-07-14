<#ftl output_format="HTML">
<!DOCTYPE html>
<html lang="nl">
<head>
    <meta charset="UTF-8"/>
    <title>BRP Persoonsgegevens</title>

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

        .suwinet {
            color: #005090;
            font-weight: bold;
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
    </style>
</head>

<body>
<div id="page-header">
    <span class="suwinet">BRP Persoonsgegevens</span>&nbsp;<span class="timestamp">${.now?string("dd-MM-yyyy HH:mm:ss")}</span>
</div>
<div id="page-footer"><span class="page-count"></span></div>

<#assign persoon = (pv.brpPersoonsgegevensInfo)!{} />
<#assign partner = (pv.brpPersoonsgegevensPartnerInfo)!{} />
<#assign kinderen = (pv.brpPersoonsgegevensKinderenInfo)![] />

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
                <tr><td class="label">Geboortedatum:</td><td class="value">${(pv.geboortedatumBrp?has_content)?then(showDate(pv.geboortedatumBrp), "-")}</td></tr>
                <tr><td class="label">BSN:</td><td class="value">${(p.bsn)!'-'}</td></tr>
                <tr><td class="label">A-nummer:</td><td class="value">${(p.anummer)!'-'}</td></tr>
            </table>
        </div>

        <div class="sectie">
            <h2>Adres (BRP)</h2>
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
                <tr><td class="label">Datum aanvang:</td><td class="value">${(v.datumAanvangVerblijfstitel)!''}</td></tr>
                <tr><td class="label">Datum einde:</td><td class="value">${(v.datumEindeVerblijfstitel)!''}</td></tr>
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
                            <td>${(vh.datumBeginAdreshouding)!''}</td>
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

<@persoonBlok titel="Aanvrager" p=persoon />

<#if partner?has_content && (partner.bsn)?has_content>
    <div style="page-break-before: always;"></div>
    <@persoonBlok titel="Partner" p=partner />
</#if>

<#if kinderen?has_content>
    <div style="page-break-before: always;"></div>

    <div class="onderwerp">Kinderen</div>

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

</body>
</html>