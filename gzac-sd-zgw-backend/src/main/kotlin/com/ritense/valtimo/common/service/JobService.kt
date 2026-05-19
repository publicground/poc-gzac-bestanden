package com.ritense.valtimo.common.service

import java.time.ZonedDateTime
import java.util.Date
import mu.KotlinLogging
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.ProcessEngineException
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.runtime.Job

class JobService {
    lateinit var processEngine: ProcessEngine

    fun addOffsetInMillisToTimerDueDateByActivityId(millisecondsToAdd: Long, activityId: String, execution: DelegateExecution) {
        processEngine = execution.processEngine

        getJobByActivityIdAndProcessInstanceId(activityId, execution.processInstanceId).apply {
            val dueDate = Date.from(
                this.duedate.toInstant().plusMillis(millisecondsToAdd)
            )

            updateJobDueDate(
                this,
                dueDate
            ).also {
                logger.debug {
                    "Changing the date of timer ${this.id} from ${this.duedate} to $dueDate " +
                        "for process instance $processInstanceId"
                }
            }
        }
    }

    fun updateTimerDueDateByActivityId(dueDateString: String, activityId: String, execution: DelegateExecution) {

        processEngine = execution.processEngine
        val dueDate = Date.from(ZonedDateTime.parse(dueDateString).toInstant())

        getJobByActivityIdAndProcessInstanceId(activityId, execution.processInstanceId).apply {
            updateJobDueDate(
                this,
                dueDate
            ).also {
                logger.debug {
                    "Changing the date of timer ${this.id} from ${this.duedate} to $dueDate " +
                        "for process instance $processInstanceId"
                }
            }
        }
    }

    private fun updateJobDueDate(job: Job, newDate: Date) {
        processEngine.managementService.setJobDuedate(job.id, newDate)
    }

    private fun getJobByActivityIdAndProcessInstanceId(
        jobActivityId: String,
        processInstanceId: String,
    ): Job {

        return processEngine.managementService
            .createJobQuery()
            .timers()
            .processInstanceId(processInstanceId)
            .activityId(jobActivityId)
            .singleResult()
            ?: throw ProcessEngineException(
                "No job with $jobActivityId found for process with Id $processInstanceId"
            )
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}