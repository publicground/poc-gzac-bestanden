package com.ritense.valtimo.common.service

import org.camunda.bpm.engine.RuntimeService

class ActiveProcessCheckService(
    private val runtimeService: RuntimeService
) {

    fun checkIfProcessIsActive(
        processDefinition: String,
        businessKey: String
    ): Boolean {
        val activeInstances = runtimeService
            .createProcessInstanceQuery()
            .processDefinitionKeyIn(processDefinition)
            .processInstanceBusinessKey(businessKey)
            .active()
            .count()

        return activeInstances > 0L
    }
}