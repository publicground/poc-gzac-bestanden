/*
 * Copyright 2015-2024 Ritense BV, the Netherlands.
 *
 * Licensed under EUPL, Version 1.2 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ritense.valtimo.custom.documentenapi.service

import com.ritense.catalogiapi.service.CatalogiService
import com.ritense.documentenapi.DocumentenApiAuthentication
import com.ritense.documentenapi.DocumentenApiPlugin
import com.ritense.documentenapi.client.DocumentInformatieObject
import com.ritense.documentenapi.client.DocumentStatusType
import com.ritense.documentenapi.client.DocumentenApiClient
import com.ritense.documentenapi.client.PatchDocumentRequest
import com.ritense.documentenapi.service.DocumentenApiService
import com.ritense.plugin.domain.PluginConfiguration
import com.ritense.plugin.service.PluginService
import java.net.URI
import mu.KotlinLogging


class CustomDocumentenApiService(
    private val documentenApiService: DocumentenApiService,
    private val catalogiService: CatalogiService,
    private val pluginService: PluginService,
    private val documentenApiClient: DocumentenApiClient
) {

    fun storeTrefwoorden(documentUrl: String, authenticationPluginConfiguration: DocumentenApiAuthentication) {
        val informatieobject = getInformatieobject(documentUrl)

        if (hasDefinitiefStatus(informatieobject)) {
            logger.info("InformatieObject ${documentUrl.substringAfterLast("/")} with status 'definitief' is not updated!")
            return
        }

        if (isExistingTrefwoord(informatieobject)) {
            logger.info("The trefwoord ${getInformatieobjecttypeOmschrijving(informatieobject)} already exists in " +
                "the Informatieobject ${documentUrl.substringAfterLast("/")}")
            return
        }

        val documentLock = documentenApiClient.lockInformatieObject(
            authenticationPluginConfiguration,
            URI(documentUrl)
        )

        val patchRequest = getPatchDocumentRequest(informatieobject).apply {
            lock = documentLock.lock
        }

        try {
            logger.info("Updating informatieObject ${documentUrl.substringAfterLast("/")}")
            documentenApiClient.modifyInformatieObject(
                authenticationPluginConfiguration,
                URI(documentUrl),
                patchRequest
            )
        } finally {
            documentenApiClient.unlockInformatieObject(
                authenticationPluginConfiguration,
                URI(documentUrl),
                documentLock
            )
        }
    }

    private fun getInformatieobject(documentUrl: String): DocumentInformatieObject {
        val pluginConfigurationId = getDocumentenApiPlugin(URI(documentUrl)).id.id.toString()
        val documentId = documentUrl.substringAfterLast("/")

        return documentenApiService
            .getInformatieObject(pluginConfigurationId, null, documentId)
    }

    private fun getPatchDocumentRequest(informatieObject: DocumentInformatieObject): PatchDocumentRequest =
        PatchDocumentRequest(
            creatiedatum = informatieObject.creatiedatum,
            titel = informatieObject.titel,
            vertrouwelijkheidaanduiding = informatieObject.vertrouwelijkheidaanduiding?.key,
            auteur = informatieObject.auteur,
            status = informatieObject.status,
            taal = informatieObject.taal,
            bestandsnaam = informatieObject.bestandsnaam,
            beschrijving = informatieObject.beschrijving,
            ontvangstdatum = informatieObject.ontvangstdatum,
            verzenddatum = informatieObject.verzenddatum,
            indicatieGebruiksrecht = informatieObject.indicatieGebruiksrecht,
            informatieobjecttype = informatieObject.informatieobjecttype,
            trefwoorden = getTrefwoorden(informatieObject)
        )

    private fun getDocumentenApiPlugin(informatieobjectUrl: URI): PluginConfiguration {
        return checkNotNull(
            pluginService.findPluginConfiguration(
                DocumentenApiPlugin::class.java,
                DocumentenApiPlugin.findConfigurationByUrl(informatieobjectUrl)
            )
        ) { "Could not find ${DocumentenApiPlugin::class.simpleName} configuration for informatieobjectUrl: $informatieobjectUrl" }

    }

    private fun getTrefwoorden(informatieObject: DocumentInformatieObject): List<String>? {
        val existingTrefwoorden = informatieObject.trefwoorden
        val newTrefwoord = getInformatieobjecttypeOmschrijving(informatieObject)

        return existingTrefwoorden?.let {
            if (newTrefwoord != null) it + newTrefwoord else it
        } ?: newTrefwoord?.let { listOf(it) }
    }

    private fun hasDefinitiefStatus(informatieObject: DocumentInformatieObject): Boolean =
        informatieObject.status == DocumentStatusType.DEFINITIEF

    private fun isExistingTrefwoord(informatieobject: DocumentInformatieObject): Boolean =
        informatieobject.trefwoorden?.contains(getInformatieobjecttypeOmschrijving(informatieobject)) ?: false

    private fun getInformatieobjecttypeOmschrijving(informatieobject: DocumentInformatieObject): String? {
        return informatieobject.informatieobjecttype?.let { URI(it) }
            ?.let { catalogiService.getInformatieobjecttype(it)?.omschrijving }
    }

    companion object{
        private val logger = KotlinLogging.logger{}
    }
}