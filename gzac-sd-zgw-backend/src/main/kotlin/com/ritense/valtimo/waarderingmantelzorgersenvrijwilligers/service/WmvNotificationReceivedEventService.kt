package com.ritense.valtimo.waarderingmantelzorgersenvrijwilligers.service

import com.fasterxml.jackson.databind.JsonNode
import com.ritense.notificatiesapi.event.NotificatiesApiNotificationReceivedEvent
import com.ritense.notificatiesapi.exception.NotificatiesNotificationEventException
import com.ritense.objectenapi.ObjectenApiPlugin
import com.ritense.objectmanagement.domain.ObjectManagement
import com.ritense.objectmanagement.service.ObjectManagementService
import com.ritense.plugin.domain.PluginConfigurationId
import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.common.service.MessageCorrelationService
import org.springframework.context.event.EventListener
import java.net.URI

class WmvNotificationReceivedEventService(
    private val messageCorrelationService: MessageCorrelationService,
    private val objectManagementService: ObjectManagementService,
    private val pluginService: PluginService,
) {
    @EventListener(NotificatiesApiNotificationReceivedEvent::class)
    fun handleNotificationReceived(event: NotificatiesApiNotificationReceivedEvent) {

        if (isObjectUpdateNotification(event)) {

            val objectManagement = getObjectManagement(event) ?: return

            if (objectManagement.title == OBJECT_MANAGEMENT_TITLE) {

                val objectData = getWaarderingsobjectData(objectManagement, event.resourceUrl)

                val waarderingsobjectStatus = objectData.get("aanvraag")?.get("status").toString().replace("\"", "")
                val businessKey = objectData.get("id").toString().replace("\"", "")

                if (waarderingsobjectStatus == STATUS_OPGEHAALD_DOOR_WEBSHOP) {
                    messageCorrelationService.sendCorrelationMessageByBusinessKey(
                        OBJECT_GEWIJZIGD_ONTVANGEN_2024_MESSAGE,
                        businessKey
                    )
                }
            }
        }
    }

    private fun isObjectUpdateNotification(event: NotificatiesApiNotificationReceivedEvent): Boolean {
        return (event.kanaal.equals("objecten", ignoreCase = true)
            && event.actie.equals("update", ignoreCase = true)
            && event.kenmerken["objectType"] != null)
    }

    private fun getObjectManagement(event: NotificatiesApiNotificationReceivedEvent): ObjectManagement? {

        val objectTypeUrl = event.kenmerken["objectType"]
        val objectTypeUuid = objectTypeUrl?.substringAfterLast("/")

        return objectTypeUuid?.let { objectManagementService.findByObjectTypeId(it.substringAfterLast("/")) }

    }

    private fun getWaarderingsobjectData(
        objectManagement: ObjectManagement,
        resourceUrl: String
    ): JsonNode {
        val objectenApiPlugin = pluginService.createInstance(
            PluginConfigurationId(
                objectManagement.objectenApiPluginConfigurationId
            )
        ) as ObjectenApiPlugin
        return objectenApiPlugin.getObject(URI(resourceUrl)).record.data
            ?: throw NotificatiesNotificationEventException(
                "Waarderingsobject meta data was empty!"
            )
    }

    companion object {
        private const val OBJECT_GEWIJZIGD_ONTVANGEN_2024_MESSAGE = "NOTIFICATIE_OBJECT_GEWIJZIGD_ONTVANGEN_2024_MESSAGE"
        private const val OBJECT_MANAGEMENT_TITLE = "WMV Waarderingsobject"
        private const val STATUS_OPGEHAALD_DOOR_WEBSHOP = "2"
    }
}