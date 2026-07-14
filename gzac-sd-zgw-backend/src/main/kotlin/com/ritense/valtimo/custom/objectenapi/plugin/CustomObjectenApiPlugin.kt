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

package com.ritense.valtimo.custom.objectenapi.plugin

import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ritense.objectmanagement.service.ObjectManagementFacade
import com.ritense.plugin.annotation.Plugin
import com.ritense.plugin.annotation.PluginAction
import com.ritense.plugin.annotation.PluginActionProperty
import com.ritense.processlink.domain.ActivityTypeWithEventName
import mu.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution

@Plugin(
    key = "custom-objecten-api-plugin",
    title = "Custom Objecten API",
    description = "Creates Objecten with ObjectenApi"
)
class CustomObjectenApiPlugin(
    private val objectManagementFacade: ObjectManagementFacade
) {

    @PluginAction(
        key = "custom-create-object",
        title = "Create object",
        description = "Store an object in the Object API",
        activityTypes = [ActivityTypeWithEventName.SERVICE_TASK_START]
    )
    fun createObject(
        @PluginActionProperty resultObjectUrl: String,
        @PluginActionProperty objectManagementTitle: String,
        @PluginActionProperty waarderingObject: LinkedHashMap<String,Any>,
        execution: DelegateExecution
    ) {
        logger.info { "Creating object for case ${execution.businessKey}" }

        objectManagementFacade.createObject(
            objectManagementTitle,
            objectMapper.convertValue(waarderingObject, ObjectNode::class.java)
        ).let {
            execution.processInstance.setVariable(
                resultObjectUrl, it.url
            )
        }
    }

    companion object{
        private val objectMapper = jacksonObjectMapper()
        private val logger = KotlinLogging.logger{}
    }

}