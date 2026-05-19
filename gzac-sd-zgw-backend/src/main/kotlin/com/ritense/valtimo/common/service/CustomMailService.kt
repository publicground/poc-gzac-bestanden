package com.ritense.valtimo.common.service

import com.ritense.mail.service.MailService
import com.ritense.openzaak.service.DocumentenService
import com.ritense.resource.domain.OpenZaakResource
import com.ritense.resource.domain.ResourceId
import com.ritense.resource.repository.OpenZaakResourceRepository
import com.ritense.valtimo.contract.mail.MailSender
import com.ritense.valtimo.contract.mail.model.TemplatedMailMessage
import com.ritense.valtimo.contract.mail.model.value.Attachment
import com.ritense.valtimo.contract.mail.model.value.AttachmentCollection
import com.ritense.valtimo.contract.mail.model.value.attachment.Content
import com.ritense.valtimo.contract.mail.model.value.attachment.Name
import com.ritense.valtimo.contract.mail.model.value.attachment.Type
import mu.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.springframework.data.repository.findByIdOrNull
import java.util.UUID
import kotlin.NoSuchElementException

class CustomMailService(
    private val mailService: MailService,
    private val mailSender: MailSender,
    private val openZaakResourceRepository: OpenZaakResourceRepository,
    private val documentenService: DocumentenService
) {

    @Synchronized
    fun sendElementTemplateTaskMail(resourceId: String, execution: DelegateExecution) {
        val mailSettings = mailService.getMailSettings(execution)
        val templatedMail = mailSettings.getTemplatedMailMessage()

        val templatedMailMessageWithAttachment = TemplatedMailMessage
            .with(
                templatedMail.recipients,
                templatedMail.templateIdentifier
            )
            .apply {
                placeholders(templatedMail.placeholders)
                subject(templatedMail.subject)
                sender(templatedMail.sender)
                attachments(getAttachmentCollection(getResourceById(resourceId)))
            }
            .build()

        logger.info { "Sending e-mail for case ${execution.businessKey} with resource ${getResourceById(resourceId)}" }

        mailSender.send(templatedMailMessageWithAttachment)

    }

    private fun getAttachmentCollection(resource: OpenZaakResource): AttachmentCollection {
        return AttachmentCollection.fromSingle(
            Attachment.from(
                Name.from(resource.name()),
                Type.from(resource.extension()),
                Content.from(
                    documentenService.getObjectInformatieObject(resource.informatieObjectUrl)
                )
            )
        )
    }

    private fun getResourceById(resourceId: String): OpenZaakResource {
        return openZaakResourceRepository.findByIdOrNull(ResourceId.existingId(UUID.fromString(resourceId)))
            ?: throw NoSuchElementException("Resource with id [$resourceId] was not found.")
    }

    companion object {
        val logger = KotlinLogging.logger {}
    }
}