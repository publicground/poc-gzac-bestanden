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

package com.ritense.valtimo.custom.documentenapi.plugin

import com.ritense.documentenapi.DocumentenApiAuthentication
import com.ritense.plugin.annotation.Plugin
import com.ritense.plugin.annotation.PluginAction
import com.ritense.plugin.annotation.PluginActionProperty
import com.ritense.plugin.annotation.PluginProperty
import com.ritense.processlink.domain.ActivityTypeWithEventName
import com.ritense.valtimo.contract.validation.Url
import com.ritense.valtimo.custom.documentenapi.service.CustomDocumentenApiService
import java.net.URI

@Plugin(
    key = "customdocumentenapi",
    title = "Custom Documenten API",
    description = "Connects to the Documenten Api to update document"
)
class CustomDocumentenApiPlugin(
    private val customDocumentenApiService: CustomDocumentenApiService
) {

    @Url
    @PluginProperty(key = "url", secret = false)
    lateinit var url: URI

    @PluginProperty(key = "authenticationPluginConfiguration", secret = false)
    lateinit var authenticationPluginConfiguration: DocumentenApiAuthentication

    @PluginAction(
        key = "store-informatieobjecttype-trefwoord",
        title = "Store informatieobjecttype trefwoord",
        description = "Store a trefwoord equal to the informatieobjecttype description to a document in the documenten API",
        activityTypes = [ActivityTypeWithEventName.SERVICE_TASK_START]
    )
    fun storeTrefwoorden(
        @PluginActionProperty documentUrl: String
    ) {
        customDocumentenApiService.storeTrefwoorden(
            documentUrl,
            authenticationPluginConfiguration
        )
    }
}