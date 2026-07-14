package com.ritense.valtimo.wordpressmail.plugin

import com.ritense.plugin.annotation.Plugin
import com.ritense.plugin.annotation.PluginAction
import com.ritense.plugin.annotation.PluginActionProperty
import com.ritense.plugin.annotation.PluginProperty
import com.ritense.processlink.domain.ActivityTypeWithEventName
import com.ritense.smartdocuments.plugin.TemplateDataEntry
import com.ritense.valtimo.wordpressmail.service.WordpressMailPluginService
import mu.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import java.net.URI
import java.util.ArrayList

@Plugin(
    key = "wordpressmail", title = "Wordpress Mail Plugin", description = "Wordpress mail service plugin"
)
@Suppress("UNUSED")
class WordpressMailPlugin(
    private val wordpressMailPluginService: WordpressMailPluginService
) {
    @PluginProperty(key = "baseUrl", secret = false, required = true)
    lateinit var baseUrl: URI

    @PluginAction(
        key = "send-mail",
        title = "Send email",
        description = "Wordpress mail send email",
        activityTypes = [ActivityTypeWithEventName.SERVICE_TASK_START]
    )
    fun send(
        @PluginActionProperty templateData: Array<TemplateDataEntry>,
        @PluginActionProperty mailSendTaskFrom: String,
        @PluginActionProperty mailSendTaskTemplate: String,
        @PluginActionProperty mailSendTaskSubject: String,
        @PluginActionProperty mailSendTaskTo: String,
        execution: DelegateExecution
    ) {
        logger.info { "sending an email for case ${execution.businessKey}" }
        val mailProperties = arrayOf(
            TemplateDataEntry("mailSendTaskFrom", mailSendTaskFrom),
            TemplateDataEntry("mailSendTaskTemplate", mailSendTaskTemplate),
            TemplateDataEntry("mailSendTaskSubject", mailSendTaskSubject),
            TemplateDataEntry("mailSendTaskTo", mailSendTaskTo)
        )

        wordpressMailPluginService.sendElementTemplateTaskMail(execution,
            mailProperties,
            templateData,
            getMailClientProperties()
        )
    }

    private fun getMailClientProperties(): Map<String, Any> {
        return mapOf("baseUrl" to baseUrl.toString())
    }

    @PluginAction(
        key = "send-mail-with-attachment",
        title = "Send email with attachment",
        description = "Wordpress mail send email with attachment",
        activityTypes = [ActivityTypeWithEventName.SERVICE_TASK_START]
    )
    fun sendWithAttachment(
        @PluginActionProperty documentUrls: ArrayList<String>,
        @PluginActionProperty templateData: Array<TemplateDataEntry>,
        @PluginActionProperty mailSendTaskFrom: String,
        @PluginActionProperty mailSendTaskTemplate: String,
        @PluginActionProperty mailSendTaskSubject: String,
        @PluginActionProperty mailSendTaskTo: String,
        execution: DelegateExecution
    ) {
        logger.info { "sending an email with attachments for case ${execution.businessKey}" }
        val mailProperties = arrayOf(
            TemplateDataEntry("mailSendTaskFrom", mailSendTaskFrom),
            TemplateDataEntry("mailSendTaskTemplate", mailSendTaskTemplate),
            TemplateDataEntry("mailSendTaskSubject", mailSendTaskSubject),
            TemplateDataEntry("mailSendTaskTo", mailSendTaskTo)
        )

        wordpressMailPluginService.sendElementTemplateTaskMailWithAttachment(execution,
            mailProperties,
            templateData,
            getMailClientProperties(),
            documentUrls
        )
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}