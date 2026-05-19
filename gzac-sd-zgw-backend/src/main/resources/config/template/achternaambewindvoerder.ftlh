<#if doc.gegevensBeoordeling?exists
&& doc.gegevensBeoordeling.bewindvoerder?exists
&& doc.gegevensBeoordeling.bewindvoerder.persoonsgegevens?exists
&& doc.gegevensBeoordeling.bewindvoerder.persoonsgegevens.achternaam?has_content>
    ${doc.gegevensBeoordeling.bewindvoerder.persoonsgegevens.achternaam}
<#else>
    ${doc.gegevensVanuitAanvraag.bewindvoerder.persoonsgegevens.achternaam}
</#if>