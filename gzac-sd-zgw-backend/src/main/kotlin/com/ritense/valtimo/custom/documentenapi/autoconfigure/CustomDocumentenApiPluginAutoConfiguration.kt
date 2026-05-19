/*
 * Copyright 2015-2024 Ritense BV, the Netherlands.
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

package com.ritense.valtimo.custom.documentenapi.autoconfigure

import com.ritense.catalogiapi.service.CatalogiService
import com.ritense.documentenapi.client.DocumentenApiClient
import com.ritense.documentenapi.service.DocumentenApiService
import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.custom.documentenapi.plugin.CustomDocumentenApiPluginFactory
import com.ritense.valtimo.custom.documentenapi.service.CustomDocumentenApiService
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties
class CustomDocumentenApiPluginAutoConfiguration {

    @Bean
    fun customDocumentenApiPluginFactory(
        pluginService: PluginService,
        customDocumentenApiService: CustomDocumentenApiService
    ): CustomDocumentenApiPluginFactory {

        return CustomDocumentenApiPluginFactory(
            pluginService,
            customDocumentenApiService
        )
    }

    @Bean
    fun customDocumentenApiService(
        documentenApiService: DocumentenApiService,
        catalogiService: CatalogiService,
        pluginService: PluginService,
        documentenApiClient: DocumentenApiClient
    ): CustomDocumentenApiService {

        return CustomDocumentenApiService(
            documentenApiService,
            catalogiService,
            pluginService,
            documentenApiClient
        )
    }
}