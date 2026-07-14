package com.ritense.valtimo.ooievaarspas.service

import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ritense.authorization.AuthorizationContext.Companion.runWithoutAuthorization
import com.ritense.document.domain.Document
import com.ritense.document.service.DocumentService

class OoievaarspasOpgegevenDataToBeoordelingService(
    private val documentService: DocumentService
) {
    fun copyOpgegevenDataToBeoordeling(businessKey: String) {
        val documentContent = getDocumentById(businessKey).content().asJson()

        val root = jacksonObjectMapper().createObjectNode()
        val beoordeling = jacksonObjectMapper().createObjectNode()
        beoordeling.set<ObjectNode>("aanvraaggegevens", documentContent.get("aanvraaggegevens")
            ?: jacksonObjectMapper().createObjectNode())
        beoordeling.set<ObjectNode>("inkomenKlant", documentContent.get("inkomenKlant")
            ?: jacksonObjectMapper().createObjectNode())
        beoordeling.set<ObjectNode>("inkomenPartner", documentContent.get("inkomenPartner")
            ?: jacksonObjectMapper().createObjectNode())
        beoordeling.set<ObjectNode>("vermogen", documentContent.get("vermogen")
            ?: jacksonObjectMapper().createObjectNode())

        root.set<ObjectNode>("beoordelingEnAfhandeling", beoordeling)

        runWithoutAuthorization { documentService.modifyDocument(getDocumentById(businessKey), root) }

    }

    fun getDocumentById(businessKey: String): Document {
        return runWithoutAuthorization { documentService.get(businessKey) }
    }
}