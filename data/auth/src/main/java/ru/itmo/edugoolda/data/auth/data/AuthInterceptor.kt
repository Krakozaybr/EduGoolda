package ru.itmo.edugoolda.data.auth.data

import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.Sender
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.itmo.edugoolda.core.network.NetworkApiFactory
import ru.itmo.edugoolda.data.auth.data.tokens.AuthTokensProvider
import ru.itmo.edugoolda.data.auth.data.tokens.AuthTokensRefresher

class AuthInterceptor(
    authTokensRefresher: Lazy<AuthTokensRefresher>,
    private val authTokensProvider: AuthTokensProvider
) : NetworkApiFactory.Interceptor {

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
    }

    private val refresher by authTokensRefresher

    private val mutex = Mutex()

    override suspend fun intercept(sender: Sender, request: HttpRequestBuilder): HttpClientCall {
        val accessToken = authTokensProvider.tokens.value?.accessToken

        if (accessToken != null) {
            request.headers[AUTHORIZATION_HEADER] = accessToken
        }

        val call = sender.execute(request)

        return if (accessToken != null && call.response.status == HttpStatusCode.Unauthorized) {
            request.headers[AUTHORIZATION_HEADER] = getNewAccessToken(accessToken)
            sender.execute(request)
        } else {
            call
        }
    }

    private suspend fun getNewAccessToken(oldToken: String) = mutex.withLock {
        val currentTokens = authTokensProvider.tokens.value
        if (currentTokens != null && oldToken != currentTokens.accessToken) {
            currentTokens
        } else {
            refresher.refreshTokens()
        }.accessToken
    }
}