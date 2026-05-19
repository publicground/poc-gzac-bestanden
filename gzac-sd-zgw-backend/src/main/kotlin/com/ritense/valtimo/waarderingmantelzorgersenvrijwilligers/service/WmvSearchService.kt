package com.ritense.valtimo.waarderingmantelzorgersenvrijwilligers.service

import com.ritense.document.domain.Document
import com.ritense.document.domain.search.AdvancedSearchRequest
import com.ritense.document.domain.search.AdvancedSearchRequest.OtherFilter
import com.ritense.document.domain.search.DatabaseSearchType
import com.ritense.document.service.DocumentSearchService
import com.ritense.document.service.impl.SearchCriteria
import com.ritense.document.service.impl.SearchRequest
import com.ritense.valtimo.common.service.DocumentReaderService
import com.ritense.valueresolver.ValueResolverService
import mu.KotlinLogging
import org.springframework.data.domain.Pageable

class WmvSearchService(
    private val documentSearchService: DocumentSearchService,
    private val documentReaderService: DocumentReaderService,
    private val valueResolverService: ValueResolverService
) {

    @Suppress("UNUSED") // Used in camunda processes
    fun findVoorgaandeAanvragen(path: String, businessKey: String): List<String> {

        val currentDocument = documentReaderService.getDocumentById(businessKey)
        val soortAanvrager = SoortAanvrager.valueOf(resolveValue(currentDocument.id().toString(), SOORTAANVRAGER_PATH))
        val aanvraagjaar = resolveValue(currentDocument.id().toString(), DATUM_AANVRAAG_PATH).substring(0,4)
        val documentDefinitionName = currentDocument.definitionId().name()

        val relatedDocuments = searchDocuments(path, businessKey, documentDefinitionName)
            .filter { it.id().toString() != businessKey }

        // Apply business rules as not all related documents are considered "voorgaande aanvraag"
        val voorgaandeAanvragen = relatedDocuments
            .filter { isVoorgaandeAanvraag(it, soortAanvrager, aanvraagjaar) }
            .map { it.id().toString() }

        // Extra business rule for voorgaande aanvragen with same email:
        // Only marked these when the bsn was different in the voorgaande aanvraag
        if (path.contains("email")) {
            val currentBsn = resolveValue(currentDocument.id().toString(), BSN_PATH)
            return voorgaandeAanvragen.filter {
                resolveValue(it, BSN_PATH) != currentBsn
            }
        }

        return voorgaandeAanvragen

    }

    @Suppress("UNUSED") // Used in camunda processes
    fun findAfgewezenAanvragen(path: String, businessKey: String): List<String> {

        val currentDocument = documentReaderService.getDocumentById(businessKey)
        val aanvraagjaar = resolveValue(currentDocument.id().toString(), DATUM_AANVRAAG_PATH).substring(0,4)
        val documentDefinitionName = currentDocument.definitionId().name()

        val relatedDocuments = searchDocuments(path, businessKey, documentDefinitionName)
            .filter { it.id().toString() != businessKey }

        return relatedDocuments
            .filter { isAfgewezenAanvraag(it, aanvraagjaar) }
            .map { it.id().toString() }
    }

    private fun isVoorgaandeAanvraag(
        relatedDocument: Document,
        soortAanvrager: SoortAanvrager,
        aanvraagjaar: String

    ): Boolean {

        val relatedDocumentId = relatedDocument.id().toString()
        val relatedDocumentSoortAanvrager = resolveValue( relatedDocumentId, SOORTAANVRAGER_PATH)
        val relatedDocumentAanvraagjaar = resolveValue( relatedDocumentId, DATUM_AANVRAAG_PATH).substring(0,4)
        val relatedDocumentZaakStatus = resolveValue( relatedDocumentId, STATUSOMSCHRIJVING_PATH)
        val relatedDocumentZaakResultaat = resolveValue( relatedDocumentId, RESULTAATOMSCHRIJVING_PATH)

        if (soortAanvrager.name == SoortAanvrager.MANTELZORGER_VRIJWILLIGER.name ) {
            return (
                        (
                            relatedDocumentZaakResultaat == VERSTREKT ||
                            relatedDocumentZaakStatus != AFGEHANDELD
                        )
                            &&
                        aanvraagjaar == relatedDocumentAanvraagjaar
                    )
        } else {
            return (

                        ( relatedDocumentSoortAanvrager == soortAanvrager.name ||
                            relatedDocumentSoortAanvrager == SoortAanvrager.MANTELZORGER_VRIJWILLIGER.name
                        )
                            &&
                        (
                            relatedDocumentZaakResultaat == VERSTREKT ||
                            relatedDocumentZaakStatus != AFGEHANDELD
                        )
                            &&
                        aanvraagjaar == relatedDocumentAanvraagjaar
                    )
        }
    }

    private fun isAfgewezenAanvraag(
        relatedDocument: Document,
        aanvraagjaar: String
    ): Boolean {

        val relatedDocumentId = relatedDocument.id().toString()
        val relatedDocumentAanvraagjaar = resolveValue( relatedDocumentId, DATUM_AANVRAAG_PATH).substring(0,4)
        val relatedDocumentZaakResultaat = resolveValue( relatedDocumentId, RESULTAATOMSCHRIJVING_PATH)

        return ( relatedDocumentZaakResultaat == GEWEIGERD  && aanvraagjaar == relatedDocumentAanvraagjaar )
    }

    private fun searchDocuments(path: String, businessKey: String, documentDefinitionName: String): List<Document> {
        val searchedValue = resolveValue(businessKey, path)

        val searchRequest = SearchRequest()
        searchRequest.documentDefinitionName = documentDefinitionName

        val searchCriteria = SearchCriteria(toJsonPath(path), searchedValue)
        searchRequest.otherFilters = listOf(searchCriteria)

        val advancedSearchRequest = AdvancedSearchRequest()
            .addOtherFilters(
                OtherFilter()
                    .path(toJsonPath(path))
                    .searchType(DatabaseSearchType.EQUAL)
                    .addValue(searchedValue)
            )

        return documentSearchService.search(documentDefinitionName, advancedSearchRequest, Pageable.unpaged()).content
    }

    fun resolveValue(businessKey: String, path: String, ): String =
        valueResolverService.resolveValues(businessKey, listOf(path)).values.first().toString()


    /**
     * toJsonPath converts a Valtimo doc: path to a JsonPath.
     *
     * Example:
     * ```
     * input: "doc:/some/path"
     * returns: "$.some.path"
     * ```
     * @param path The Valtimo doc: path
     * @return The JsonPath string
     */
    fun toJsonPath(path: String): String { return path
            .replace("doc:/", "doc:")
            .replace("/", ".")
    }

    enum class SoortAanvrager {
        MANTELZORGER,
        VRIJWILLIGER,
        MANTELZORGER_VRIJWILLIGER
    }

    companion object {
        private val logger = KotlinLogging.logger { }
        private const val DATUM_AANVRAAG_PATH = "doc:/aanvraaggegevens/datumAanvraagOntvangen"
        private const val SOORTAANVRAGER_PATH = "doc:/soortAanvrager"
        private const val RESULTAATOMSCHRIJVING_PATH = "doc:/openzaak/resultaatOmschrijving"
        private const val STATUSOMSCHRIJVING_PATH = "doc:/openzaak/statusOmschrijving"
        private const val BSN_PATH = "doc:/aanvrager/persoonsgegevens/bsn"
        private const val AFGEHANDELD = "Afgehandeld"
        private const val VERSTREKT = "Verstrekt"
        private const val GEWEIGERD = "Geweigerd"
    }
}