package com.ritense.valtimo.haalcentraalauthenticationplugin.client

import io.netty.channel.ChannelOption
import io.netty.handler.ssl.SslContextBuilder
import mu.KotlinLogging
import reactor.netty.http.client.HttpClient
import java.io.FileInputStream
import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.TrustManagerFactory

class ClientFactoryHelper {

    fun httpClient(httpClientConfig: HttpClientConfig): HttpClient {
        val sslContextBuilder: SslContextBuilder = SslContextBuilder.forClient()

        // Configure truststore
        if (!httpClientConfig.truststorePath.isNullOrBlank()) {
            val trustManagerFactory = buildTrustManagerFactory(httpClientConfig.truststorePath, httpClientConfig.truststoreSecret)
                ?: throw IllegalStateException("Failed to create TrustManagerFactory for $httpClientConfig.truststorePath")
            sslContextBuilder.trustManager(trustManagerFactory)
        } else {
            // Use default JVM truststore
            val defaultTrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            defaultTrustManagerFactory.init(null as KeyStore?) // Passing null uses the default truststore
            sslContextBuilder.trustManager(defaultTrustManagerFactory)
        }

        // Configure keystore with the client private key and client cert
        buildKeyManagerFactory(
            httpClientConfig.keystorePath, httpClientConfig.keystoreSecret
        )?.let { sslContextBuilder.keyManager(it) }

        val sslContext = sslContextBuilder.build()

        val localConnectionTimeout = httpClientConfig.connectionTimeout ?: 10000
        val localResponseTimeout = (httpClientConfig.responseTimeout ?: 10000) * 1L

        return HttpClient.create().secure { sslSpec ->
            sslSpec.sslContext(sslContext)
        }.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, localConnectionTimeout)
            .responseTimeout(java.time.Duration.ofMillis(localResponseTimeout))
    }

    private fun buildKeyManagerFactory(
        keystoreCertificate: String?, keystoreKey: String?
    ): KeyManagerFactory? {

        if (keystoreCertificate.isNullOrEmpty() || keystoreKey.isNullOrEmpty()) {
            logger.info("Keystore not set")
            return null
        }

        logger.info("wsgKeyManagerFactory certificate: $keystoreCertificate")
        val keyStore = KeyStore.getInstance("jks")
        keyStore.load(FileInputStream(keystoreCertificate), keystoreKey.toCharArray())
        val keystoreManagerFactory = KeyManagerFactory.getInstance("SunX509")
        keystoreManagerFactory?.init(keyStore, keystoreKey.toCharArray())
        return keystoreManagerFactory
    }

    private fun buildTrustManagerFactory(
        truststoreCertificate: String?, truststoreKey: String?
    ): TrustManagerFactory? {
        if (truststoreCertificate.isNullOrEmpty() || truststoreKey.isNullOrEmpty()) {
            SamlTokenClient.logger.debug("Truststore not set.")
            return null
        }

        val trustStore = KeyStore.getInstance("jks")
        SamlTokenClient.logger.debug("wsgTrustManagerFactory certificate: $truststoreCertificate")

        trustStore.load(FileInputStream(truststoreCertificate), truststoreKey.toCharArray())
        val trustManagerFactory = TrustManagerFactory.getInstance("SunX509")
        trustManagerFactory?.init(trustStore)
        return trustManagerFactory
    }

    companion object {
        val logger = KotlinLogging.logger {}
    }
}