package ru.itmo.edugoolda.core.network

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.Sender
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.serialization.kotlinx.json.json

/**
 * Creates implementations of Ktorfit APIs.
 */
class NetworkApiFactory(
    private val loggingEnabled: Boolean,
    private val backendUrl: String,
    private val httpClientEngine: HttpClientEngine,
    private val interceptors: List<Interceptor>
) {
    companion object {
        private const val CONNECT_TIMEOUT_MILLISECONDS = 30000L
        private const val READ_WRITE_TIMEOUT_MILLISECONDS = 60000L
    }

    fun interface Interceptor {
        suspend fun intercept(sender: Sender, request: HttpRequestBuilder): HttpClientCall
    }

    private val json = createDefaultJson()
    private val authorizedHttpClient = createHttpClient(authorized = true)
    private val unauthorizedHttpClient = createHttpClient(authorized = false)

    /**
     * Ktorfit for creating of authorized APIs
     */
    val authorizedKtorfit = createKtorfit(authorizedHttpClient)

    /**
     * Ktorfit for creating of APIs that don't require authorization
     */
    val unauthorizedKtorfit = createKtorfit(unauthorizedHttpClient)

    private fun createHttpClient(authorized: Boolean): HttpClient {
        return HttpClient(httpClientEngine) {
            install(Logging) {
                logger = createKtorLogger()
                level = if (loggingEnabled) LogLevel.ALL else LogLevel.NONE
            }

            install(ContentNegotiation) {
                json(json)
            }

            install(HttpTimeout) {
                connectTimeoutMillis = CONNECT_TIMEOUT_MILLISECONDS
                requestTimeoutMillis = READ_WRITE_TIMEOUT_MILLISECONDS
            }

            defaultRequest {
                url(backendUrl)
            }

            setupErrorConverter()
        }.apply {
            interceptors.forEach { interceptor ->
                plugin(HttpSend).intercept {
                    interceptor.intercept(this, it)
                }
            }
        }
    }

    private fun createKtorfit(httpClient: HttpClient): Ktorfit {
        return Ktorfit.Builder()
            .baseUrl(backendUrl)
            .httpClient(httpClient)
            .build()
    }
}
