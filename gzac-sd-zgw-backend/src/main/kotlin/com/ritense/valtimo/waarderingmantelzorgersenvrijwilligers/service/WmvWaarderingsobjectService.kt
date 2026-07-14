package com.ritense.valtimo.waarderingmantelzorgersenvrijwilligers.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ritense.authorization.AuthorizationContext
import com.ritense.authorization.AuthorizationContext.Companion
import com.ritense.authorization.AuthorizationContext.Companion.runWithoutAuthorization
import com.ritense.document.service.DocumentService

class WmvWaarderingsobjectService(
    private val documentService: DocumentService

) {
    fun getWaarderingsobjectData(businessKey: String): ObjectNode {
        val data = jacksonObjectMapper().createObjectNode()
        data.set<ObjectNode>("personalia", getPersonaliaNode(getDocumentContent(businessKey)))
        data.set<ObjectNode>("contactgegevens", getContactgegevensNode(getDocumentContent(businessKey)))
        data.set<ObjectNode>("adres", getAdresgegevensNode(getDocumentContent(businessKey)))
        data.set<ObjectNode>("aanvraag", getAanvraagNode(getDocumentContent(businessKey)))
        data.put("id", businessKey)

        return data
    }

    private fun getPersonaliaNode(documentContent: ObjectNode): JsonNode {
        val personalia = jacksonObjectMapper().createObjectNode()
        personalia.put("voornaam", documentContent.get("aanvrager").get("voornaam").asText())
        personalia.put("achternaam", documentContent.get("aanvrager").get("achternaam").asText())
        personalia.put("tussenvoegsel", documentContent.get("aanvrager").get("tussenvoegsel")?.asText() ?: "")

        return personalia
    }

    private fun getContactgegevensNode(documentContent: ObjectNode): JsonNode {
        val contactgegevens = jacksonObjectMapper().createObjectNode()
        contactgegevens.put("e-mail", documentContent.get("aanvrager").get("emailadres").asText())
        contactgegevens.put("telefoonnummer", documentContent.get("aanvrager").get("telefoonnummer")?.asText() ?: "")

        return contactgegevens
    }

    private fun getAdresgegevensNode(documentContent: ObjectNode): JsonNode {
        val adresNode = documentContent.get("aanvrager").get("adres").apply {
            this as ObjectNode
            this.remove("volledigAdres")
        }
        return adresNode
    }

    private fun getAanvraagNode(documentContent: ObjectNode): JsonNode? {
        val aanvraag = jacksonObjectMapper().createObjectNode()
        aanvraag.put("soortAanvraag", documentContent.get("aanvraaggegevens").get("soortAanvraag").asText())
        aanvraag.put("status", WAARDERINGSOBJECT_INITIAL_STATUS)

        return aanvraag
    }

    private fun getDocumentContent(businessKey: String): ObjectNode {
        val document = runWithoutAuthorization { documentService.get(businessKey) }
        return document.content().asJson() as ObjectNode
    }

    companion object {
        private const val WAARDERINGSOBJECT_INITIAL_STATUS = "1"
        // 1 = In afwachting van Webshop
    }
}
