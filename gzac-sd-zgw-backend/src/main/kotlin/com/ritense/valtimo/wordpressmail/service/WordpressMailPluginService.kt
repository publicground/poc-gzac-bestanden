/*
 * Copyright 2015-2023 Ritense BV, the Netherlands.
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

package com.ritense.valtimo.wordpressmail.service

import com.ritense.documentenapi.DocumentenApiPlugin
import com.ritense.documentenapi.client.DocumentInformatieObject
import com.ritense.documentenapi.service.DocumentenApiService
import com.ritense.plugin.service.PluginService
import com.ritense.smartdocuments.plugin.TemplateDataEntry
import com.ritense.valtimo.contract.basictype.EmailAddress
import com.ritense.valtimo.contract.basictype.SimpleName
import com.ritense.valtimo.contract.mail.model.MailMessageStatus
import com.ritense.valtimo.contract.mail.model.TemplatedMailMessage
import com.ritense.valtimo.contract.mail.model.value.Attachment
import com.ritense.valtimo.contract.mail.model.value.AttachmentCollection
import com.ritense.valtimo.contract.mail.model.value.MailTemplateIdentifier
import com.ritense.valtimo.contract.mail.model.value.Recipient
import com.ritense.valtimo.contract.mail.model.value.Sender
import com.ritense.valtimo.contract.mail.model.value.Subject
import com.ritense.valtimo.contract.mail.model.value.attachment.Content
import com.ritense.valtimo.contract.mail.model.value.attachment.Name
import com.ritense.valtimo.contract.mail.model.value.attachment.Type
import com.ritense.valueresolver.ValueResolverService
import java.net.URI
import java.util.Optional
import mu.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution

class WordpressMailPluginService(
    private val valueResolverService: ValueResolverService,
    private val wordpressMailPluginSender: WordpressMailPluginSender,
    private val pluginService: PluginService,
    private val documentenApiService: DocumentenApiService
) {

    fun sendElementTemplateTaskMail(
        execution: DelegateExecution,
        mailProperties: Array<TemplateDataEntry>,
        templateVariables: Array<TemplateDataEntry>,
        clientProperties: Map<String, Any>
    ): Optional<List<MailMessageStatus>> {
        val mailSettings = getMailSettings(execution, mailProperties, templateVariables)
        return wordpressMailPluginSender.send(
            mailSettings.getTemplatedMailMessage(),
            clientProperties
        )
    }

    fun sendElementTemplateTaskMailWithAttachment(
        execution: DelegateExecution,
        mailProperties: Array<TemplateDataEntry>,
        templateVariables: Array<TemplateDataEntry>,
        clientProperties: Map<String, Any>,
        documentUrls: ArrayList<String>
    ): Optional<List<MailMessageStatus>> {
        val mailSettings = getMailSettings(execution, mailProperties, templateVariables)

        val attachments = documentUrls.mapNotNull { documentUrl ->
            try {
                getAttachment(documentUrl)
            } catch (e: Exception) {
                logger.error("Error while creating attachment for documentUrl: $documentUrl", e.localizedMessage)
                null
            }
        }

        return wordpressMailPluginSender.send(
            mailSettings.getTemplatedMailMessageWithAttachments(AttachmentCollection.from(attachments)),
            clientProperties
        )
    }

    private fun getMailSettings(
        execution: DelegateExecution,
        mailProperties: Array<TemplateDataEntry>,
        templateVariables: Array<TemplateDataEntry>
    ): PluginMailSettings {
        return PluginMailSettings(
            resolveTemplateData(mailProperties, execution),
            resolveTemplateData(templateVariables, execution),
            execution
        )
    }

    private fun resolveTemplateData(
        templateData: Array<TemplateDataEntry>,
        execution: DelegateExecution
    ): Map<String, Any?> {
        val placeHolderValueMap = valueResolverService.resolveValues(
            execution.processInstanceId,
            execution,
            templateData.map { it.value }.toList()
        )
        return templateData.associate { it.key to placeHolderValueMap.getOrDefault(it.value, null) }
    }

    private fun getAttachment(documentUrl: String): Attachment {

        val informatieObject = getInformatieobject(documentUrl)

        val documentenApiPlugin = getDocumentenApiPlugin(informatieObject.url)

        val informatieObjectId = informatieObject.url.path.split("/").last()

        val fileBytes = documentenApiPlugin?.downloadInformatieObject(null, informatieObjectId)?.readAllBytes()

        val fileName = informatieObject.bestandsnaam.toString()

        return Attachment.from(
            Name.from(fileName),
            Type.from(fileName.substringAfterLast(".")),
            Content.from(fileBytes)
        )
    }

    private fun getInformatieobject(documentUrl: String): DocumentInformatieObject {
        val documentId = documentUrl.substringAfterLast("/")

        return documentenApiService
            .getInformatieObject(getPluginConfigurationId(documentUrl), null, documentId)
    }

    private fun getDocumentenApiPlugin(url: URI): DocumentenApiPlugin? {
        return pluginService.createInstance(
            DocumentenApiPlugin::class.java,
            DocumentenApiPlugin.findConfigurationByUrl(url)
        )
    }

    private fun getPluginConfigurationId(documentUrl: String): String {
        return checkNotNull(
            pluginService.findPluginConfiguration(
                DocumentenApiPlugin::class.java,
                DocumentenApiPlugin.findConfigurationByUrl(URI(documentUrl))
            )?.id?.id.toString()
        ) { "Could not find ${DocumentenApiPlugin::class.simpleName} configuration for documentUrl: $documentUrl" }
    }

    data class PluginMailSettings(
        val mailProperties: Map<String, Any?>,
        val templateVariables: Map<String, Any?>,
        val delegateExecution: DelegateExecution
    ) {
        private val mailSendTaskTo: String by mailProperties
        private val mailSendTaskFrom: String by mailProperties
        private val mailSendTaskSubject: String by mailProperties
        private val mailSendTaskTemplate: String by mailProperties

        private fun getRecipient(): Recipient {
            return Recipient.to(EmailAddress.from(mailSendTaskTo), SimpleName.none())
        }

        private fun getSender(): Sender {
            return Sender.from(EmailAddress.from(mailSendTaskFrom))
        }

        private fun getSubject(): Subject {
            return Subject.from(mailSendTaskSubject)
        }

        private fun getPlaceholders(): Map<String, Any> {
            return mapOf<String, Any>(
                "business-key" to delegateExecution.processBusinessKey,
                "var" to templateVariables
            )
        }

        fun getTemplatedMailMessage(): TemplatedMailMessage {
            return TemplatedMailMessage.with(getRecipient(), MailTemplateIdentifier.from(mailSendTaskTemplate))
                .placeholders(getPlaceholders())
                .subject(getSubject())
                .sender(getSender())
                .build()
        }

        fun getTemplatedMailMessageWithAttachments(attachmentCollection: AttachmentCollection): TemplatedMailMessage {
            return TemplatedMailMessage.with(getRecipient(), MailTemplateIdentifier.from(mailSendTaskTemplate))
                .placeholders(getPlaceholders())
                .subject(getSubject())
                .sender(getSender())
                .attachments(attachmentCollection)
                .build()
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}