package com.ritense.valtimo.common.service

import com.fasterxml.jackson.databind.node.ObjectNode
import com.ritense.objectenapi.ObjectenApiPlugin
import com.ritense.objectenapi.client.ObjectRecord
import com.ritense.objectenapi.client.ObjectRequest
import com.ritense.objectmanagement.domain.ObjectManagement
import com.ritense.objectmanagement.repository.ObjectManagementRepository
import com.ritense.objecttypenapi.ObjecttypenApiPlugin
import com.ritense.plugin.domain.PluginConfigurationId
import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.common.exception.ObjectNotFoundException
import mu.KotlinLogging
import org.springframework.data.domain.Pageable
import java.net.URI
import java.time.LocalDate

class CustomObjectenApiService(
    private val objectManagementRepository: ObjectManagementRepository,
    private val pluginService: PluginService,

    ) {

    private lateinit var objectManagement: ObjectManagement
    private lateinit var objectenApiPlugin: ObjectenApiPlugin
    private lateinit var objecttypenApiPlugin: ObjecttypenApiPlugin

    fun createObject(objectManagementTitle: String, objectData: ObjectNode): URI {

        objectManagement = objectManagementRepository.findByTitle(objectManagementTitle)
            ?: throw IllegalStateException("Object management not found for title $objectManagementTitle.")

        objectenApiPlugin = pluginService.createInstance(
            PluginConfigurationId
                .existingId(objectManagement.objectenApiPluginConfigurationId)
        ) as ObjectenApiPlugin


        objecttypenApiPlugin = pluginService
            .createInstance(PluginConfigurationId(objectManagement.objecttypenApiPluginConfigurationId)) as ObjecttypenApiPlugin
        val objectTypeUrl = objecttypenApiPlugin.getObjectTypeUrlById(objectManagement.objecttypeId)

        val createObjectRequest = ObjectRequest(
            objectTypeUrl,
            ObjectRecord(
                typeVersion = objectManagement.objecttypeVersion,
                data = objectData,
                startAt = LocalDate.now()
            )
        )
        return objectenApiPlugin.createObject(createObjectRequest).url
    }

    fun findObjectUrlBySearchStringAndObjectManagementTitle(
        searchString: String,
        objectManagementTitle: String
    ): String {
        require(searchString.isNotEmpty()) {
            "searchString must not be empty!"
        }
        require(searchString.isNotEmpty()) {
            "objectManagementTitle must not be empty!"
        }

        objectManagement = objectManagementRepository.findByTitle(objectManagementTitle)
            ?: throw IllegalStateException("Object management configuration with title $objectManagementTitle was not found.")
        objectenApiPlugin =
            pluginService.createInstance(objectManagement.objectenApiPluginConfigurationId)
        objecttypenApiPlugin =
            pluginService.createInstance(objectManagement.objecttypenApiPluginConfigurationId)

        logger.debug { "Attempting to find object by search string and object management title [$objectManagementTitle]" }

        val objectUrl = objectenApiPlugin
            .getObjectsByObjectTypeIdWithSearchParams(
                objecttypesApiUrl = objecttypenApiPlugin.url,
                objecttypeId = objectManagement.objecttypeId,
                searchString = searchString,
                pageable = Pageable.ofSize(500)
            )
            .results
            .firstOrNull()
            ?.url
            ?.toString()
            ?: throw ObjectNotFoundException(
                "No object matches provided search string and object management title. Is Zaakdetailsync configured?"
            )

        return objectUrl
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}