<#if doc.gegevensBeoordeling?exists
&& doc.gegevensBeoordeling.bewindvoerder?exists
&& doc.gegevensBeoordeling.bewindvoerder.contactgegevens?exists
&& doc.gegevensBeoordeling.bewindvoerder.contactgegevens.telefoonnummer?has_content>
    ${doc.gegevensBeoordeling.bewindvoerder.contactgegevens.telefoonnummer}
<#else>
    ${doc.gegevensVanuitAanvraag.bewindvoerder.contactgegevens.telefoonnummer}
</#if>