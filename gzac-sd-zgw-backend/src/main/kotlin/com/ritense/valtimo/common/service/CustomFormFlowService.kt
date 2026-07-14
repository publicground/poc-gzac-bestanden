package com.ritense.valtimo.common.service

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.MissingNode
import com.ritense.formflow.expression.FormFlowBean
import com.ritense.valueresolver.ValueResolverService
import org.camunda.bpm.engine.TaskService

@FormFlowBean
class CustomFormFlowService(
    private val valueResolverService: ValueResolverService,
    private val taskService: TaskService,
    private val objectMapper: ObjectMapper
) {

    fun completeTask(additionalProperties: Map<String, Any>, submissionData: JsonNode?, submissionSavePath: Map<String, String>) {

        if (submissionData != null) {
            val processInstanceId = additionalProperties["processInstanceId"] as String
            val submissionValues = submissionSavePath.entries
                .associate { it.key to getValue(submissionData, it.value) }
                .filter { it.value !is MissingNode }
            valueResolverService.handleValues(processInstanceId, null, submissionValues)
        }

        taskService.complete(additionalProperties["taskInstanceId"] as String)

    }

    private fun getValue(data: JsonNode, path: String): Any {
        val valueNode = data.at(JsonPointer.valueOf(path))
        if (valueNode.isMissingNode) {
            return valueNode
        }
        return objectMapper.treeToValue(valueNode, Object::class.java)
    }
}