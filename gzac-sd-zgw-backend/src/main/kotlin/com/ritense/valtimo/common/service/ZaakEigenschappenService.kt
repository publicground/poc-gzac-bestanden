/*
 * Copyright (c) 2024-2024 Ritense BV, the Netherlands.
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

package com.ritense.valtimo.common.service

import com.ritense.document.service.DocumentService
import com.ritense.openzaak.listener.EigenschappenSubmittedListener
import com.ritense.valtimo.contract.event.ExternalDataSubmittedEvent

@Suppress("UNUSED") // Called from a JUEL expression
class ZaakEigenschappenService(
    val documentService: DocumentService,
    private val eigenschappenSubmittedListener: EigenschappenSubmittedListener
) {

    fun setEigenschap(key: String, value: Any, businessKey: String) {
        val document = documentService.get(businessKey)
        val eigenschapEvent = ExternalDataSubmittedEvent(
            mapOf(EIGENSCHAPPEN_EXTERNAL_FORM_FIELD_RESOLVER_PREFIX to mapOf(key to value)),
            document.definitionId().name(),
            document.id().id
        )

        eigenschappenSubmittedListener.handle(eigenschapEvent)
    }

    companion object {
        const val EIGENSCHAPPEN_EXTERNAL_FORM_FIELD_RESOLVER_PREFIX = "openzaak"
    }
}
