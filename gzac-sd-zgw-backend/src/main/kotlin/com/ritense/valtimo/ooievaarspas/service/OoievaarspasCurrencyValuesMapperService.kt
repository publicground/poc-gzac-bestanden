package com.ritense.valtimo.ooievaarspas.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ritense.authorization.AuthorizationContext
import com.ritense.authorization.AuthorizationContext.Companion
import com.ritense.authorization.AuthorizationContext.Companion.runWithoutAuthorization
import com.ritense.document.service.DocumentService
import org.camunda.bpm.engine.delegate.DelegateExecution

class OoievaarspasCurrencyValuesMapperService(
    private val documentService: DocumentService
) {

    // required for smart document templates.

    fun setMappedDocumentDataToProcessVariables(execution: DelegateExecution) {
        val documentContent = getDocumentContentById(execution.businessKey)

        val variables = mapOf(
            "inkomenKlantOpgegeven" to getMappedInkomenNode(documentContent.get("inkomenKlant")),
            "inkomenPartnerOpgegeven" to getMappedInkomenNode(documentContent.get("inkomenPartner")),
            "vermogenOpgegeven" to getMappedVermogenNode(documentContent.get("vermogen")),
            "beoordelingEnAfhandeling" to getMappedBeoordelingEnAfhandelingNode(documentContent.get("beoordelingEnAfhandeling"))
        )

        variables.forEach { execution.setVariable(it.key, it.value) }
    }

    private fun getMappedBeoordelingEnAfhandelingNode(documentBeoordelingEnAfhandelingNode: JsonNode?): JsonNode {
        return documentBeoordelingEnAfhandelingNode?.apply {
            this as ObjectNode
            this.replace("inkomenKlant", getMappedInkomenNode(documentBeoordelingEnAfhandelingNode.get("inkomenKlant")))
            this.replace("inkomenPartner", getMappedInkomenNode(documentBeoordelingEnAfhandelingNode.get("inkomenPartner")))
            this.replace("vermogen", getMappedVermogenNode(documentBeoordelingEnAfhandelingNode.get("vermogen")))
            this.replace("inkomenNorm", getMappedInkomenVermogenNormNode(documentBeoordelingEnAfhandelingNode.get("inkomenNorm")))
            this.replace("vermogenNorm", getMappedInkomenVermogenNormNode(documentBeoordelingEnAfhandelingNode.get("vermogenNorm")))
            this.put("totaalVermogenTBVAanvraagOoievaarspas", formatFloatToTwoDecimalString(getOrZero(documentBeoordelingEnAfhandelingNode.get("totaalVermogenTBVAanvraagOoievaarspas"))))
            this.put("totaalInkomenTBVAanvraagOoievaarspas", formatFloatToTwoDecimalString(getOrZero(documentBeoordelingEnAfhandelingNode.get("totaalInkomenTBVAanvraagOoievaarspas"))))

        }
            ?: return jacksonObjectMapper().createObjectNode()
    }

    private fun getMappedInkomenVermogenNormNode(documentInkomenNormNode: JsonNode?): JsonNode {
        return documentInkomenNormNode?.apply {
            this as ObjectNode
            this.put("normOpBasisVanGezinssituatie", formatFloatToTwoDecimalString(getOrZero(documentInkomenNormNode.get("normOpBasisVanGezinssituatie"))))
        }
            ?: return jacksonObjectMapper().createObjectNode()
    }

    private fun getMappedVermogenNode(documentVermogenNode: JsonNode?): JsonNode {
        val vermogenNode = jacksonObjectMapper().createObjectNode()

        if (documentVermogenNode != null) {
            vermogenNode.set<ObjectNode>("bezittingen", getMappedBezittingenNode(documentVermogenNode.get("bezittingen")))
            vermogenNode.set<ObjectNode>("schulden", getMappedArray("bedrag", documentVermogenNode.get("schulden") as ArrayNode))
        }

        return vermogenNode
    }

    private fun getMappedBezittingenNode(documentBezittingenNode: JsonNode?): JsonNode {
        val bezittingen = jacksonObjectMapper().createObjectNode()

        if (documentBezittingenNode != null) {
            bezittingen.set<ObjectNode>("bankrekeningen", getMappedArray("tegoed", documentBezittingenNode.get("bankrekeningen") as ArrayNode))
            bezittingen.set<ObjectNode>("aandelenObligaties", getMappedArray("waarde", documentBezittingenNode.get("aandelenObligaties") as ArrayNode))
            bezittingen.set<ObjectNode>("cryptoCurrencies", getMappedArray("waarde", documentBezittingenNode.get("cryptoCurrencies") as ArrayNode))
            bezittingen.put("contantGeld", formatFloatToTwoDecimalString(getOrZero(documentBezittingenNode.get("contantGeld"))))
        }

        return bezittingen
    }

    private fun getMappedInkomenNode(documentInkomenNode: JsonNode?): JsonNode {
        val inkomenNode = jacksonObjectMapper().createObjectNode()

        if (documentInkomenNode != null && !documentInkomenNode.isEmpty) {
            inkomenNode.set<ObjectNode>("werkgevers", getMappedArray("nettoloon", documentInkomenNode.get("werkgevers") as ArrayNode))
            inkomenNode.set<ObjectNode>("pensioenen", getMappedArray("nettobedrag", documentInkomenNode.get("pensioenen") as ArrayNode))
            inkomenNode.set<ObjectNode>("uitkeringen", getMappedUitkeringenNode(documentInkomenNode.get("uitkeringen") as ObjectNode))
            inkomenNode.put("nettoKinderalimentatie", formatFloatToTwoDecimalString(getOrZero(documentInkomenNode.get("nettoKinderalimentatie"))))
            inkomenNode.put("bedragOnderhuur", formatFloatToTwoDecimalString(getOrZero(documentInkomenNode.get("bedragOnderhuur"))))
            inkomenNode.put("totaalMaandinkomenWerkgevers", formatFloatToTwoDecimalString(getOrZero(documentInkomenNode.get("totaalMaandinkomenWerkgevers"))))
            inkomenNode.put("totaalMaandinkomenUitkeringen", formatFloatToTwoDecimalString(getOrZero(documentInkomenNode.get("totaalMaandinkomenUitkeringen"))))
            inkomenNode.put("totaalMaandinkomenPensioenen", formatFloatToTwoDecimalString(getOrZero(documentInkomenNode.get("totaalMaandinkomenPensioenen"))))
        }

        return inkomenNode
    }

    private fun getMappedUitkeringenNode(uitkeringen: JsonNode?): JsonNode {
        return uitkeringen?.apply {
            this as ObjectNode
            this.replace("anw", getMappedUitkeringObject(uitkeringen.get("anw") as ObjectNode))
            this.replace("aow", getMappedUitkeringObject(uitkeringen.get("aow") as ObjectNode))
            this.replace("bijstand", getMappedUitkeringObject(uitkeringen.get("bijstand") as ObjectNode))
            this.replace("wajong", getMappedUitkeringObject(uitkeringen.get("wajong") as ObjectNode))
            this.replace("wao", getMappedUitkeringObject(uitkeringen.get("wao") as ObjectNode))
            this.replace("wia", getMappedUitkeringObject(uitkeringen.get("wia") as ObjectNode))
            this.replace("ww", getMappedUitkeringObject(uitkeringen.get("ww") as ObjectNode))
            this.replace("ziektewet", getMappedUitkeringObject(uitkeringen.get("ziektewet") as ObjectNode))
        }
            ?: jacksonObjectMapper().createObjectNode()
    }

    private fun getMappedUitkeringObject(objectNode: JsonNode?): JsonNode {
        return objectNode?.apply {
            this as ObjectNode
            this.put("nettobedrag", formatFloatToTwoDecimalString(getOrZero(objectNode.get("nettobedrag"))))
        }
            ?: jacksonObjectMapper().createObjectNode()
    }

    private fun getMappedArray(fieldName: String, arrayNode: ArrayNode?): ArrayNode {
        val mappedArray = jacksonObjectMapper().createArrayNode()

        if (arrayNode != null) {
            if (!arrayNode.isEmpty) {
                arrayNode.forEach {
                    it as ObjectNode
                    it.put(fieldName, formatFloatToTwoDecimalString(getOrZero(it.get(fieldName))))
                    mappedArray.add(it)
                }
            }
        }

        return mappedArray
    }

    private fun getOrZero(jsonNode: JsonNode?): Float {
        return jsonNode?.floatValue() ?: 0.toFloat()
    }

    private fun formatFloatToTwoDecimalString(floatValue: Float): String {
        return String.format("%.2f", floatValue).replace(".", ",")
    }

    private fun getDocumentContentById(businessKey: String): JsonNode {
        return runWithoutAuthorization { documentService.get(businessKey).content().asJson() }
    }
}