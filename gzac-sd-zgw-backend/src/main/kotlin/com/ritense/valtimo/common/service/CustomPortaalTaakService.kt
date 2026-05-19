package com.ritense.valtimo.common.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ritense.objectenapi.ObjectenApiPlugin
import com.ritense.objectenapi.client.ObjectRecord
import com.ritense.objectenapi.client.ObjectRequest
import com.ritense.objectenapi.client.ObjectWrapper
import com.ritense.objectmanagement.repository.ObjectManagementRepository
import com.ritense.objectmanagement.service.ObjectManagementFacade
import com.ritense.objecttypenapi.ObjecttypenApiPlugin
import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.common.domain.CustomTaakObject
import com.ritense.zakenapi.service.ZaakDocumentService
import java.util.UUID
import org.springframework.data.domain.Pageable
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class CustomPortaalTaakService(
    private val objectManagementFacade: ObjectManagementFacade,
    private val objectManagementRepository: ObjectManagementRepository,
    private val pluginService: PluginService,
    private val zaakDocumentService: ZaakDocumentService
) {
    private lateinit var objectenApiPlugin: ObjectenApiPlugin
    private lateinit var objecttypenApiPlugin: ObjecttypenApiPlugin

    fun cancelPortaalTaak(verwerkerTaakId: String, objectManagementTitle: String) {

        require(verwerkerTaakId.isNotEmpty()) {
            "verwerkerTaakId must not be null!"
        }
        require(objectManagementTitle.isNotEmpty()) {
            "objectManagementTitle must not be null!"
        }

        val objectManagement = objectManagementRepository.findByTitle(objectManagementTitle)
        if (objectManagement == null) {
            logger.error("Object management configuration with title $objectManagementTitle was not found.")
            return
        }

        objectenApiPlugin =
            pluginService.createInstance(objectManagement.objectenApiPluginConfigurationId)
        objecttypenApiPlugin =
            pluginService.createInstance(objectManagement.objecttypenApiPluginConfigurationId)

        val responseObject = objectenApiPlugin.getObjectsByObjectTypeIdWithSearchParams(
            objecttypesApiUrl = objecttypenApiPlugin.url,
            objecttypeId = objectManagement.objecttypeId,
            searchString = "verwerker_taak_id__exact__$verwerkerTaakId",
            pageable = Pageable.ofSize(500)
        )
            .results
            .firstOrNull()

        if (responseObject == null) {
            logger.error("Portaaltaak with verwerkerTaakId $verwerkerTaakId was not found.")
            return
        }

        val taakObject: CustomTaakObject = objectMapper
            .convertValue(
                responseObject.record.data ?: throw RuntimeException("Portaaltaak meta data was empty!")
            )
        val cancelledTaakObject = getCancelledTaakObject(taakObject)
        val portaalTaakMetaObjectUpdated =
            changeDataInPortalTaakObject(responseObject, cancelledTaakObject)

        objectenApiPlugin.objectPatch(responseObject.url, portaalTaakMetaObjectUpdated)

    }

    fun updatePortaalTaak(verwerkerTaakId: String, objectManagementTitle: String, newDeadline: String) {
        require(verwerkerTaakId.isNotBlank()) { "verwerkerTaakId must not be blank!" }
        require(objectManagementTitle.isNotBlank()) { "objectManagementTitle must not be blank!" }

        val objectManagement = objectManagementRepository.findByTitle(objectManagementTitle)
            ?: run {
                logger.error("Object management configuration with title '$objectManagementTitle' was not found.")
                return
            }

        val responseObject = objectManagementFacade.getObjectsUnpaged(
            objectName = objectManagement.title,
            searchString = "verwerker_taak_id__exact__$verwerkerTaakId",
            ordering = null
        ).results.firstOrNull()
            ?: run {
                logger.error("Portaaltaak with verwerkerTaakId '$verwerkerTaakId' was not found.")
                return
            }

        val objectData = (responseObject.record.data as? ObjectNode)
            ?: return logger.error("Object data is not of type ObjectNode.")

        val verzoekNode = objectData.getNestedNode("data", "informatieverzoek")
            ?: return logger.error("verzoek node not found in object data.")

        val formattedDate = formatDateTime(newDeadline, "dd-MM-yyyy")
        val formattedDateTime = formatDateTime(newDeadline, "yyyy-MM-dd'T'HH:mm:ss.SSS") + "Z"

        verzoekNode.put("deadlineInformatieverzoek", formattedDateTime)

        objectData.put("verloopdatum", formattedDateTime)
        objectData.getNestedNode("data")?.put("prettyDeadline", formattedDate)

        objectManagementFacade.updateObject(
            objectId = responseObject.uuid,
            objectName = objectManagement.title,
            data = objectData
        )

        logger.info("Successfully updated 'taak' object with verwerkerTaakId: '$verwerkerTaakId'.")
    }

    @Suppress("UNUSED") // Called from camunda process
    fun getRelatedFilesFromPortaaltaakBestanden(bestanden: Map<String, List<String>>, businessKey: String): Any {
        if (bestanden.isNotEmpty()) {
            val zaakResources = zaakDocumentService.getInformatieObjectenAsRelatedFiles(UUID.fromString(businessKey))
            val bestandenEntries = bestanden.entries
            val mappedEntries = bestandenEntries.associate { entry ->
                entry.key to entry.value.map { url ->
                    zaakResources
                        .firstOrNull {
                            it.fileId.toString() == url.substringAfterLast("/")
                        }
                }
            }
            return objectMapper.convertValue(mappedEntries)
        }

        return objectMapper.convertValue(emptyMap<String, Any>())
    }

    private fun changeDataInPortalTaakObject(
        portaalTaakMetaObject: ObjectWrapper,
        convertValue: JsonNode?
    ): ObjectRequest {
        return ObjectRequest(
            type = portaalTaakMetaObject.type,
            record = ObjectRecord(
                data = convertValue ?: objectMapper.createObjectNode(),
                correctedBy = portaalTaakMetaObject.record.correctedBy,
                endAt = portaalTaakMetaObject.record.endAt,
                index = portaalTaakMetaObject.record.index,
                geometry = portaalTaakMetaObject.record.geometry,
                registrationAt = portaalTaakMetaObject.record.registrationAt,
                startAt = portaalTaakMetaObject.record.startAt,
                typeVersion = portaalTaakMetaObject.record.typeVersion
            )
        )
    }

    private fun getCancelledTaakObject(taakObject: CustomTaakObject): JsonNode {
        return objectMapper
            .convertValue<ObjectNode>(taakObject)
            .put("status", "gesloten")
    }

    private fun JsonNode.getNestedNode(vararg path: String): ObjectNode? {
        var current: JsonNode? = this
        for (key in path) {
            current = current?.get(key)
            if (current == null) return null
        }
        return current as? ObjectNode
    }

    private fun formatDateTime(dateTime: String, pattern: String): String {
        return OffsetDateTime.parse(dateTime)
            .format(DateTimeFormatter.ofPattern(pattern))
    }

    companion object {
        private val logger = mu.KotlinLogging.logger {}
        private val objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())
    }
}