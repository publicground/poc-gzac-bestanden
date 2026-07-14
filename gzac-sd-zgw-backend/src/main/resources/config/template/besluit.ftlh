<#assign besluitEnum = {
"TOEGEKEND": "Aanvraag toegekend",
"AFGEWEZEN": "Aanvraag afgewezen",
"BUITEN_BEHANDELING_GESTELD": "Aanvraag buiten behandeling gesteld",
"INGETROKKEN": "Aanvraag ingetrokken"
} />
<#if doc.besluiten?? && doc.besluiten?has_content>
    <#assign last = doc.besluiten[doc.besluiten?size - 1] />
    <#if last.genomenBesluit?has_content && (!(last.vervaldatum??) || last.vervaldatum == "")>
        ${besluitEnum[last.genomenBesluit]!last.genomenBesluit}
    <#else>
        -
    </#if>
<#else>
    -
</#if>