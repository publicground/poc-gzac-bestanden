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

package com.ritense.valtimo.custom.zakenapi.plugin

import com.ritense.plugin.annotation.Plugin
import com.ritense.plugin.annotation.PluginAction
import com.ritense.plugin.annotation.PluginActionProperty
import com.ritense.processlink.domain.ActivityTypeWithEventName
import com.ritense.valtimo.common.enums.ZaakobjectObjectType
import com.ritense.valtimo.common.service.CustomZakenApiService
import org.camunda.bpm.engine.delegate.DelegateExecution

@Suppress("UNUSED")
@Plugin(
    key = "customzakenapi",
    title = "Custom Zaken API",
    description = "Plugin to support more Zaken API Endpoints"
)
class CustomZakenApiPlugin(
    private val customZakenApiService: CustomZakenApiService
) {

    @PluginAction(
        key = "link-object-to-zaak",
        title = "Link Object to zaak",
        description = "Link Object to zaak in ZakenAPI",
        activityTypes = [ActivityTypeWithEventName.SERVICE_TASK_START]
    )
    fun linkObject(
        @PluginActionProperty objectUrl: String,
        @PluginActionProperty objectType: String,
        @PluginActionProperty objectTypeOverige: String?,
        execution: DelegateExecution
    ) {
        customZakenApiService.linkObjectToZaak(
            objectUrl,
            execution.businessKey,
            ZaakobjectObjectType.valueOf(objectType.uppercase()),
            objectTypeOverige
        )
    }
}