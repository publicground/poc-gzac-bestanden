package com.ritense.valtimo.listener

import mu.KotlinLogging
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.delegate.DelegateTask
import org.camunda.bpm.engine.delegate.TaskListener

class CustomPortaalTaakListener(
    private val runtimeService: RuntimeService
) : TaskListener {

    override fun notify(delegateTask: DelegateTask) {

        runtimeService
            .setVariable(
                delegateTask.execution.id,
                PORTAL_TASK_ID_VARIABLE_NAME,
                delegateTask.id
            )
            .also {
                logger.debug { "\"verwerkerTaakId\" stored in process ${delegateTask.processInstanceId}" }
            }
    }

    companion object {
        private const val PORTAL_TASK_ID_VARIABLE_NAME = "verwerkerTaakId"
        private val logger = KotlinLogging.logger { }
    }
}