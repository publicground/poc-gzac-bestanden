/*
 * Copyright (c) 2024-2024 Ritense BV, the Netherlands.
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

package com.ritense.valtimo.common.service

import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.common.client.CustomZakenApiClient
import com.ritense.valtimo.common.domain.CreateZaakobjectRequest
import com.ritense.valtimo.common.domain.ZaakUpdateRequest
import com.ritense.valtimo.common.enums.ZaakobjectObjectType
import com.ritense.valtimo.common.enums.ZaakrelatieType
import com.ritense.zakenapi.ZaakUrlProvider
import com.ritense.zakenapi.ZakenApiPlugin
import com.ritense.zakenapi.domain.RelevanteZaak
import com.ritense.zakenapi.service.ZaakDocumentService
import mu.KotlinLogging
import java.net.URI
import java.util.UUID

@Suppress("UNUSED") // Called from a JUEL expression
class CustomZakenApiService(
    private val pluginService: PluginService,
    private val zaakUrlProvider: ZaakUrlProvider,
    private val zakenApiClient: CustomZakenApiClient,
    private val zaakDocumentService: ZaakDocumentService
) {

    fun linkOverigeObjectToZaak(
        objectUrl: String,
        objectTypeOverigeOmschrijving: String,
        businessKey: String
    ) {
        linkObjectToZaak(objectUrl, businessKey, ZaakobjectObjectType.OVERIGE, objectTypeOverigeOmschrijving)
    }

    fun linkObjectToZaak(
        objectUrl: String,
        businessKey: String,
        objectType: ZaakobjectObjectType,
        objectTypeOverigeOmschrijving: String? = null
    ) {
        if (objectType == ZaakobjectObjectType.OVERIGE) {
            require(objectTypeOverigeOmschrijving?.isNotEmpty() ?: false) {
                "objectTypeOverigeOmschrijving is required when object type is Overige"
            }
        }

        if (objectType != ZaakobjectObjectType.OVERIGE) {
            require(objectTypeOverigeOmschrijving?.isEmpty() ?: true) {
                "objectTypeOverigeOmschrijving needs to be null or empty when object type is not Overige"
            }
        }

        val zaakUrl = zaakUrlProvider.getZaakUrl(UUID.fromString(businessKey))
        val zakenApiPlugin = getZakenApiPlugin(zaakUrl)

        val request = CreateZaakobjectRequest(
            zaakUrl = zaakUrl.toASCIIString(),
            objectUrl = objectUrl,
            objectType = objectType.name.lowercase(),
            objectTypeOverige = objectTypeOverigeOmschrijving
        )

        zakenApiClient
            .createZaakobject(
                authentication = zakenApiPlugin.authenticationPluginConfiguration,
                baseUrl = zakenApiPlugin.url,
                request = request
            )
            .also {
                logger.debug { "Successfully created ZAAKOBJECT for zaak $zaakUrl" }
            }
    }

    fun linkZaakToZaak(currentCaseId: String, relevantCaseId: String, relationType: String) {

        val hoofdzaak = zaakDocumentService.getZaakByDocumentIdOrThrow(UUID.fromString(currentCaseId))
        val relevanteZaakUrl = zaakUrlProvider.getZaakUrl(UUID.fromString(relevantCaseId))
        val aardRelatie = ZaakrelatieType.valueOf(relationType)

        val relevanteZaak = RelevanteZaak(relevanteZaakUrl, aardRelatie.relatie)

        val relevanteAndereZaken = (hoofdzaak.relevanteAndereZaken ?: emptyList())
            .toMutableList()
            .apply { add(relevanteZaak) }

        val request = ZaakUpdateRequest(
            bronorganisatie = hoofdzaak.bronorganisatie,
            zaaktype = hoofdzaak.zaaktype.toString(),
            verantwoordelijkeOrganisatie = hoofdzaak.verantwoordelijkeOrganisatie,
            startdatum = hoofdzaak.startdatum,
            relevanteAndereZaken = relevanteAndereZaken.toList()
        )

        val zakenApiPlugin = getZakenApiPlugin(hoofdzaak.url)

        zakenApiClient.updateZaak(
            zaak = hoofdzaak,
            authentication = zakenApiPlugin.authenticationPluginConfiguration,
            baseUrl = zakenApiPlugin.url,
            request = request
        )

        logger.debug { "Successfully linked zaak $relevanteZaakUrl to zaak ${hoofdzaak.url}" }
    }

    private fun getZakenApiPlugin(zaakUrl: URI): ZakenApiPlugin {
        return requireNotNull(
            pluginService
                .createInstance(
                    ZakenApiPlugin::class.java,
                    ZakenApiPlugin.findConfigurationByUrl(zaakUrl)
                )
        ) { "No plugin configuration was found for zaak with URL $zaakUrl" }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}