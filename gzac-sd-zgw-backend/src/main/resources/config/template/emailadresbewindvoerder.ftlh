<#if doc.gegevensBeoordeling?exists
&& doc.gegevensBeoordeling.bewindvoerder?exists
&& doc.gegevensBeoordeling.bewindvoerder.contactgegevens?exists
&& doc.gegevensBeoordeling.bewindvoerder.contactgegevens.emailadres?has_content>
    ${doc.gegevensBeoordeling.bewindvoerder.contactgegevens.emailadres}
<#else>
    ${doc.gegevensVanuitAanvraag.bewindvoerder.contactgegevens.emailadres}
</#if>