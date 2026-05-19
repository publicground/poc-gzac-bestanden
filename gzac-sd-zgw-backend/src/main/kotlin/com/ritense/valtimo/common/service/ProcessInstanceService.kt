package com.ritense.valtimo.common.service

import com.ritense.document.service.DocumentService
import com.ritense.processdocument.service.impl.CamundaProcessJsonSchemaDocumentAssociationService
import org.camunda.bpm.engine.RuntimeService

class ProcessInstanceService(
    private val documentService: DocumentService,
    private val documentAssociationService: CamundaProcessJsonSchemaDocumentAssociationService,
    private val runtimeService: RuntimeService
) {

    fun deleteAllProcessInstances(businessKey: String, deleteReason: String) {
        documentAssociationService.findProcessDocumentInstances(documentService.get(businessKey).id())
            .takeIf { it.isNotEmpty() }
            ?.map { it.processDocumentInstanceId().processInstanceId().toString() }
            ?.let { runtimeService.deleteProcessInstancesAsync(it, deleteReason) }
    }
}