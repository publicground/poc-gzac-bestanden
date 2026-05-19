<#if (doc.gegevensVaststelling.aanvrager.verblijfadres.postcode)??
&& doc.gegevensVaststelling.aanvrager.verblijfadres.postcode?replace(" ", "")?has_content>
    ${doc.gegevensVaststelling.aanvrager.verblijfadres.postcode}
<#elseif (doc.gegevensVanuitAanvraag.aanvrager.verblijfadres.postcode)??
&& doc.gegevensVanuitAanvraag.aanvrager.verblijfadres.postcode?replace(" ", "")?has_content>
    ${doc.gegevensVanuitAanvraag.aanvrager.verblijfadres.postcode}
<#elseif (doc.gegevensVanuitAanvraag.aanvrager.adresBrp.postcode)??>
    ${doc.gegevensVanuitAanvraag.aanvrager.adresBrp.postcode}
<#else>
    -
</#if>