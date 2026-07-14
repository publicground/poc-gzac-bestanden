package com.ritense.valtimo.common.service

import com.ritense.authorization.AuthorizationContext.Companion.runWithoutAuthorization
import com.ritense.document.service.DocumentService
import com.ritense.openzaak.service.ZaakResultaatService
import com.ritense.openzaak.service.ZaakService
import com.ritense.openzaak.service.ZaakStatusService
import com.ritense.valtimo.common.client.OpenzaakClient
import com.ritense.valtimo.common.enums.ZaakResultaat
import com.ritense.valtimo.common.enums.ZaakStatus
import com.ritense.valtimo.common.enums.ZaakrelatieType
import com.ritense.zakenapi.link.ZaakInstanceLinkService
import mu.KotlinLogging
import java.util.UUID

class ZaakHandleService(
    private val zaakInstanceLinkService: ZaakInstanceLinkService,
    private val openzaakClient: OpenzaakClient,
    private val zaakService: ZaakService,
    private val zaakStatusService: ZaakStatusService,
    private val documentService: DocumentService,
    private val zaakResultaatService: ZaakResultaatService
) {

    fun setZaakStatus(documentId: String, status: String) {
        val zaakStatus = ZaakStatus.valueOf(status)

        logger.debug { "Setting Status ${zaakStatus.status} in Zaak with documentId $documentId." }

        zaakStatusService.setStatus(
            runWithoutAuthorization { documentService.get(documentId).id() },
            zaakStatus.status
        )

    }

    fun setZaakResultaat(documentId: String, resultaat: String) {
        val zaak = zaakService.getZaakByDocumentId(UUID.fromString(documentId))
        val zaakresultaat = ZaakResultaat.valueOf(resultaat)

        val resultaatTypes = zaakResultaatService.getResultaatTypes(zaak.zaaktype)
        val resultaatType = resultaatTypes.results.first {
            it.omschrijving == zaakresultaat.resultaat
        }

        zaakService.setZaakResultaat(
            zaak = zaak.url,
            resultaatType = resultaatType.url
                ?: throw IllegalArgumentException("ResultaatType ${resultaatType.omschrijving} has no URL available")
        )
    }

    fun linkZaak(currentCaseId: String, relevantCaseId: String, relationType: String) {

        val hoofdzaakUrl = zaakInstanceLinkService.getByDocumentId(UUID.fromString(currentCaseId))
        val relevanteZaakUrl = zaakInstanceLinkService.getByDocumentId(UUID.fromString(relevantCaseId))

        val hoofdzaak = zaakService.getZaak(hoofdzaakUrl.zaakInstanceId)
        val relevanteZaak = zaakService.getZaak(relevanteZaakUrl.zaakInstanceId)

        openzaakClient.linkZaakToZaak(hoofdzaak, relevanteZaak, ZaakrelatieType.valueOf(relationType))
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
