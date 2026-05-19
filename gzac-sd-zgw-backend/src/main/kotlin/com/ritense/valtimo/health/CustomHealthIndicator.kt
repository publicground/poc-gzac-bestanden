package com.ritense.valtimo.health

import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import io.micrometer.core.instrument.MeterRegistry
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component("livenessState")
class CustomHealthIndicator(
    val meterRegistry: MeterRegistry,
) : HealthIndicator {

    @Value("\${implementation.databaseConnectionPool.unhealthyThreshold}")
    var unhealthyThreshold: Int = 99

    override fun health(): Health {
        return if (checkLiveness()) {
            Health.up().withDetail("liveness", LIVENESS_HEALTHY_MESSAGE).build()
        } else {
            logger.error { LIVENESS_UNHEALTHY_LOG_MESSAGE }
            Health.down().withDetail("liveness", LIVENESS_UNHEALTHY_MESSAGE).build()
        }
    }

    private fun checkLiveness(): Boolean {
        return try {
            val activeConnections = meterRegistry.get("hikaricp.connections.active").gauge().value().toInt()
            logger.info { "Active database connections: $activeConnections, threshold: $unhealthyThreshold" }
            activeConnections <= unhealthyThreshold
        } catch (e: Exception) {
            logger.error { "ERROR_MESSAGE - $e"  }
            // If there is an exception querying the metric, assume the application is up
            true
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
        const val LIVENESS_HEALTHY_MESSAGE = "Database connection pool is healthy"
        const val LIVENESS_UNHEALTHY_LOG_MESSAGE = "Health check failed - Too many active database connections."
        const val LIVENESS_UNHEALTHY_MESSAGE = "Database connection pool is getting full"
        private const val ERROR_MESSAGE =
            "An error occurred while checking active database connections on which the liveness probe is based."
    }
}
