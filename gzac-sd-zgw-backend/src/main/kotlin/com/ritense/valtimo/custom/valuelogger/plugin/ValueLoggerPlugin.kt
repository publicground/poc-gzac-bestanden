package com.ritense.valtimo.custom.valuelogger.plugin

import com.fasterxml.jackson.databind.ObjectMapper
import com.ritense.plugin.annotation.Plugin
import com.ritense.plugin.annotation.PluginAction
import com.ritense.plugin.annotation.PluginActionProperty
import com.ritense.processlink.domain.ActivityTypeWithEventName
import com.ritense.valueresolver.ValueResolverService
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.ritense.smartdocuments.plugin.TemplateDataEntry
import mu.KLogger
import mu.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution

@Plugin(
    key = "valueLogger",
    title = "Value Logger Plugin",
    description = "Value Logger Plugin for logging values from BPMN to the log"
)
class ValueLoggerPlugin(
    private val valueResolverService: ValueResolverService,
) {
    @PluginAction(
        key = "log-values",
        title = "Log values",
        description = "Logs values from BPMN to the log",
        activityTypes = [ActivityTypeWithEventName.SERVICE_TASK_START]
    )
    fun logValues(
        execution: DelegateExecution,
        @PluginActionProperty templateData: Array<TemplateDataEntry>,
        @PluginActionProperty outputFormat: String

    ) {
        val resolvedTemplateData = resolveTemplateData(templateData, execution)

        when (outputFormat) {
            "Smart Documents XML" -> logSmartDocumentXml(resolvedTemplateData)
            "Smart Documents JSON" -> logSmartDocumentJson(resolvedTemplateData)
            "JSON" -> logJson(resolvedTemplateData)
        }
    }

    private fun logSmartDocumentXml(resolvedTemplateData: Map<String, Any?>) {
        logger.info("""$VALUE_LOGGER: ${xmlMapper
            .writer().withRootName("customerData")
            .writeValueAsString(resolvedTemplateData)}"""
        )
    }

    private fun logSmartDocumentJson(resolvedTemplateData: Map<String, Any?>) {
        logger.info("""$VALUE_LOGGER: ${objectMapper.writer()
            .withRootName("customerData")
            .writeValueAsString(resolvedTemplateData)}""")
    }

    private fun logJson(resolvedTemplateData: Map<String, Any?>) {
        logger.info("""$VALUE_LOGGER: ${objectMapper.writer()
            .writeValueAsString(resolvedTemplateData)}""")
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

    companion object{
        val logger: KLogger = KotlinLogging.logger {}
        val xmlMapper = XmlMapper()
        val objectMapper = ObjectMapper()
        private const val VALUE_LOGGER = """VALUE LOGGER"""
    }
}
