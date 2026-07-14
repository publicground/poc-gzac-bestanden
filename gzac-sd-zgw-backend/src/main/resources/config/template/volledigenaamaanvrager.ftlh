<#assign a = (doc.gegevensVaststelling.aanvrager)!{} />
<#assign achternaam  = (a.achternaam)!"" />
<#assign voorvoegsel = (a.voorvoegsel)!"" />
<#assign voorletters = (a.voorletters)!"" />

<#if voorvoegsel?has_content>
    ${achternaam} ${voorvoegsel}, ${voorletters}
<#else>
    ${achternaam}, ${voorletters}
</#if>