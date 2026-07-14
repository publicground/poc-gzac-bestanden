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

package com.ritense.valtimo.common.client

import com.ritense.valtimo.common.domain.CreateZaakobjectRequest
import com.ritense.valtimo.common.domain.ZaakObjectResponse
import com.ritense.valtimo.common.domain.ZaakUpdateRequest
import com.ritense.zakenapi.ZakenApiAuthentication
import com.ritense.zakenapi.domain.ZaakResponse
import com.ritense.zgw.ClientTools
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.body
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.net.URI

class CustomZakenApiClient(
    private val restClientBuilder: RestClient.Builder
) {
    fun updateZaak(
        zaak: ZaakResponse,
        authentication: ZakenApiAuthentication,
        baseUrl: URI,
        request: ZaakUpdateRequest
    ): ZaakResponse {
        try {
            return runBlocking {
                buildRestClient(authentication)
                    .put()
                    .uri {
                        ClientTools.baseUrlToBuilder(it, baseUrl)
                            .path("zaken/" + zaak.uuid)
                            .build()
                    }
                    .headers { headers ->
                        setDefaultHeaders(headers)
                    }
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body<ZaakResponse>()!!
            }
        } catch (ex: WebClientResponseException) {
            throw HttpClientErrorException(ex.statusCode, ex.responseBodyAsString)
        }
    }

    fun createZaakobject(
        authentication: ZakenApiAuthentication,
        baseUrl: URI,
        request: CreateZaakobjectRequest
    ): ZaakObjectResponse {

        try {
            return runBlocking {
                buildRestClient(authentication)
                    .post()
                    .uri {
                        ClientTools.baseUrlToBuilder(it, baseUrl)
                            .path("zaakobjecten")
                            .build()
                    }
                    .headers { headers ->
                        setDefaultHeaders(headers)
                    }
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body<ZaakObjectResponse>()!!
            }
        } catch (ex: WebClientResponseException) {
            throw HttpClientErrorException(ex.statusCode, ex.responseBodyAsString)
        }
    }

    private fun buildRestClient(authentication: ZakenApiAuthentication): RestClient {
        return restClientBuilder
            .clone()
            .apply {
                authentication.applyAuth(it)
            }
            .build()
    }

    private fun setDefaultHeaders(headers: HttpHeaders) {
        headers.set("Accept-Crs", "EPSG:4326")
        headers.set("Content-Crs", "EPSG:4326")
    }
}