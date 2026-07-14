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

import com.ritense.mail.wordpressmail.domain.EmailSendRequest
import com.ritense.mail.wordpressmail.domain.NamedByteArrayResource
import com.ritense.mail.event.MailSendEvent
import com.ritense.valtimo.contract.mail.MailFilter
import com.ritense.valtimo.contract.mail.model.MailMessageStatus
import com.ritense.valtimo.contract.mail.model.RawMailMessage
import com.ritense.valtimo.contract.mail.model.TemplatedMailMessage
import com.ritense.valtimo.contract.mail.model.value.AttachmentCollection
import com.ritense.valtimo.wordpressmail.client.WordpressMailPluginClient
import com.ritense.valtimo.wordpressmail.client.WordpressMailProperties
import org.springframework.context.ApplicationEventPublisher
import org.springframework.core.io.Resource
import java.util.*
import kotlin.streams.toList

class WordpressMailPluginSender(
    private val wordpressMailPluginClient: WordpressMailPluginClient,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val filters: Collection<MailFilter>
) {

    fun send(rawMailMessage: RawMailMessage) {
        TODO("implement raw mail message")
    }

    fun send(templatedMailMessage: TemplatedMailMessage,
             clientProperties: Map<String, Any>): Optional<List<MailMessageStatus>> {
        val optionalMessage = applyFilters(templatedMailMessage)
        if (optionalMessage.isPresent) {
            return Optional.of(sendFiltered(templatedMailMessage, clientProperties))
        }
        return Optional.empty()
    }

    private fun sendFiltered(templatedMailMessage: TemplatedMailMessage,
                             properties: Map<String, Any>): MutableList<MailMessageStatus> {
        wordpressMailPluginClient.setProperties(WordpressMailProperties(properties))

        val statusList = mutableListOf<MailMessageStatus>()
        val templateId = getTemplateIdByName(templatedMailMessage.templateIdentifier.get())
        val sendRequests = EmailSendRequest.from(templatedMailMessage)
        for (sendRequest in sendRequests) {
            val attachments = attachmentsToResources(templatedMailMessage.attachments)
            val response = wordpressMailPluginClient.send(templateId, sendRequest, attachments)
            val mailStatus = response.toMailMessageStatus()
            statusList.add(mailStatus)

            if (response.success == true) {
                applicationEventPublisher.publishEvent(
                    MailSendEvent(
                        mailStatus.email,
                        templatedMailMessage.subject
                    )
                )
            }
        }
        return statusList
    }

    private fun attachmentsToResources(attachments: AttachmentCollection): List<Resource>? {
        return attachments.get()?.stream()
            ?.map { NamedByteArrayResource(it.name.get(), it.content.get()) }
            ?.toList<NamedByteArrayResource>()
    }

    fun getMaximumSizeAttachments(): Int {
        return MAX_SIZE_EMAIL_BODY_IN_BYTES
    }

    private fun getTemplateIdByName(templateName: String): String {
        val template = wordpressMailPluginClient.getEmailTemplates().emails.firstOrNull { it.postTitle == templateName }
        if (template == null) {
            throw IllegalStateException("No e-mail template found with name: '$templateName'")
        } else {
            return template.id
        }
    }

    private fun applyFilters(templatedMailMessage: TemplatedMailMessage): Optional<TemplatedMailMessage> {
        var filteredTemplatedMailMessage = templatedMailMessage
        val filters = prioritizedFilters()
        filters.forEach {
            val filteredMailMessageOptional = it.doFilter(filteredTemplatedMailMessage)
            if (filteredMailMessageOptional.isPresent) {
                filteredTemplatedMailMessage = filteredMailMessageOptional.get()
            } else {
                return Optional.empty()
            }
        }
        return Optional.of(filteredTemplatedMailMessage)
    }

    private fun prioritizedFilters(): Collection<MailFilter> {
        return filters.stream()
            .filter { it.isEnabled }
            .sorted(compareBy { it.priority })
            .toList()
    }

    companion object {
        const val MAX_SIZE_EMAIL_BODY_IN_BYTES: Int = 20000000  // 20mb. TODO: verify
    }
}
