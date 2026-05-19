<#assign soortUitkering =
(doc.gegevensBeoordeling.bbzLevensonderhoud)!((doc.gegevensVanuitAanvraag.bbzLevensonderhoud)!"")
>

<#if soortUitkering == "BBZ_STARTEND"> Startende ondernemer
<#elseif soortUitkering == "BBZ_GEVESTIGD"> Gevestigde ondernemers
<#elseif soortUitkering == "BBZ_OUDERE"> Oudere ondernemer
<#elseif soortUitkering == "BBZ_STOPPEND"> Stoppende ondernemer
<#elseif soortUitkering == "IOAZ_STOPPEND"> IOAZ
<#else>
    -
</#if>