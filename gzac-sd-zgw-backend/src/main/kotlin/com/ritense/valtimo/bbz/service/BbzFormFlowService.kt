package com.ritense.valtimo.bbz.service

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.MissingNode
import com.fasterxml.jackson.module.kotlin.treeToValue
import com.ritense.formflow.domain.instance.FormFlowStepInstance
import com.ritense.formflow.domain.instance.FormFlowStepInstanceId
import com.ritense.formflow.expression.FormFlowBean
import com.ritense.formflow.repository.FormFlowStepInstanceRepository
import com.ritense.valtimo.contract.json.MapperSingleton
import com.ritense.valueresolver.ValueResolverService
import mu.KotlinLogging
import kotlin.jvm.optionals.getOrNull

@Suppress("UNUSED") // Called from form-flow
@FormFlowBean
class BbzFormFlowService(
    private val valueResolverService: ValueResolverService,
    private val formFlowStepInstanceRepository: FormFlowStepInstanceRepository
) {
    fun resolveValue(documentId: String?, pointer: String): Any? {
        requireNotNull(documentId) {
            "This method must be ran within the scope of an existing case."
        }
        logger.debug { "Resolving value for pointer: $pointer" }

        val resolvedValues = valueResolverService.resolveValues(documentId, listOf(pointer))

        return resolvedValues[pointer]
    }

    fun storeAndClearStepData(processInstanceId: String?, stepInstanceId: FormFlowStepInstanceId, stepSubmissionData: JsonNode?) {
        logger.debug { "Handling submission data of form-flow step with id: $stepInstanceId" }

        requireNotNull(processInstanceId) {
            "This method must be ran within the scope of an existing case and process"
        }

        if (stepSubmissionData != null) {
            handleSubmissionData(processInstanceId, stepSubmissionData)
                .also {
                    logger.debug { "Stored step submission data for form-flow step with id: ${stepInstanceId.id}" }
                }
            clearStepSubmissionData(stepInstanceId)
                .also {
                    logger.debug { "Cleared step submission data in form-flow step with id: ${stepInstanceId.id}" }
                }
        }
    }

    private fun handleSubmissionData(processInstanceId: String, stepSubmissionData: JsonNode) {

        val submissionValues = BBZ_BEOORDELING_FORM_FLOW_MAPPING.entries
            .associate { it.key to getValue(stepSubmissionData, it.value) }
            .filterNot { it.value is MissingNode }

        valueResolverService.handleValues(processInstanceId, null, submissionValues)
    }

    private fun clearStepSubmissionData(stepInstanceId: FormFlowStepInstanceId) {
        val stepInstance: FormFlowStepInstance? = formFlowStepInstanceRepository
            .findById(stepInstanceId)
            .getOrNull()

        if (stepInstance != null) {
            stepInstance.submissionData = null
            stepInstance.temporarySubmissionData = null
            formFlowStepInstanceRepository.save(stepInstance)
        }

    }

    private fun getValue(data: JsonNode, path: String): Any {
        val valueNode = data.at(JsonPointer.valueOf(path))
        if (valueNode.isMissingNode) {
            return valueNode
        }
        return objectMapper.treeToValue(valueNode)
    }

    companion object {
        private val logger = KotlinLogging.logger { }
        private val objectMapper = MapperSingleton.get()
        private val BBZ_BEOORDELING_FORM_FLOW_MAPPING = mapOf(
            "doc:/gegevensBeoordeling" to "/gegevensBeoordeling",
            "doc:/beoordelingEnAfhandeling" to "/beoordelingEnAfhandeling",
            "pv:informatieverzoek" to "/pv/informatieverzoek",
            "pv:uitzettenInformatieverzoek" to "/pv/uitzettenInformatieverzoek",
            "pv:uitzettenLevensvatbaarheidsonderzoek" to "/pv/uitzettenLevensvatbaarheidsonderzoek",
            "pv:submit" to "/submit"
        )
    }
}
