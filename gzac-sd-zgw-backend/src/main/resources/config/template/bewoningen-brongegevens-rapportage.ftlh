<#ftl output_format="HTML">
<!DOCTYPE html>
<html lang="nl">
<head>
    <meta charset="UTF-8"/>
    <title>BRP Persoonsgegevens</title>

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
<#assign brp = (pv.brpPersoonsgegevensInfo)!{} />
<#assign bewoningen = (pv.bewoningen)!{} />
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

<div id="page-header">Bewoningen</div>
<div id="page-footer"><span class="page-count"></span></div>

<div class="onderwerp">
    Bewoningen – <span class="muted">${fullName?trim?has_content?then(fullName?trim,'Onbekend')}</span>
    (BSN: ${(brp.bsn)!'-'})
</div>

<div class="sectie">
    <h2>Persoon</h2>
    <table class="persoon-table">
        <tr><td class="label">Naam</td><td class="value">${fullName?trim?has_content?then(fullName?trim,'-')}</td></tr>
        <tr><td class="label">BSN</td><td class="value">${(brp.bsn)!'-'}</td></tr>
    </table>
</div>

<div class="sectie">
    <#if bewoningen?has_content>
        <h2>Bewoningen</h2>
        <#list bewoningen as bewoningen>
            <div>Adresseerbaar object identificatie: ${(bewoningen.adresseerbaarObjectIdentificatie)!'-'}</div>
             <#if bewoningen.bewoners?has_content>
                 <h2>Bewoners</h2>
                 <table class="table">
                     <thead>
                     <tr>
                         <th>Naam</th>
                         <th>Geboortedatum</th>
                         <th>Geheimhouding</th>
                     </tr>
                     </thead>
                     <tbody>
                     <#list bewoningen.bewoners as bewoners>
                         <tr>
                             <td>${(bewoners.naam.volledigeNaam)!'-'}</td>
                             <td>${(showDate(bewoners.geboorte.datum.datum))!'-'}</td>
                             <td>${(bewoners.geheimhoudingPersoonsgegevens)!'-'}</td>
                         </tr>
                     </#list>
                     </tbody>
                 </table>
             </#if>
            <#if bewoningen.mogelijkeBewoners?has_content>
                <div>Adresseerbaar object identificatie: ${(bewoningen.adresseerbaarObjectIdentificatie)!'-'}</div>
                <h2>Mogelijke bewoners</h2>
                <table class="table">
                    <thead>
                    <tr>
                        <th>Naam</th>
                        <th>Geboortedatum</th>
                        <th>Geheimhouding</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list bewoningen.mogelijkeBewoners as mogelijkeBewoners>
                        <tr>
                            <td>${(bewoners.naam.volledigeNaam)!'-'}</td>
                            <td>${(showDate(bewoners.geboorte.datum.datum))!'-'}</td>
                            <td>${(bewoners.geheimhoudingPersoonsgegevens)!'-'}</td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </#if>
        </#list>
    <#else>
        <div>Er zijn geen bewoning gegevens beschikbaar.</div>
    </#if>
</div>

</body>
</html>