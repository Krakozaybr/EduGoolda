package ru.itmo.edugoolda.data.auth.internal.tokens

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import ru.itmo.edugoolda.core.settings.SettingsFactory
import ru.itmo.edugoolda.data.auth.internal.domain.AuthTokens

internal interface AuthTokensStorage {

    suspend fun clear()
    suspend fun save(tokens: AuthTokens)

    class Base(
        settingsFactory: SettingsFactory
    ) : AuthTokensStorage, AuthTokensProvider {

        companion object {
            private const val STORAGE_NAME = "tokens_storage"
            private const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY"
            private const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN_KEY"
        }

        private val storage = settingsFactory.createEncryptedSettings(STORAGE_NAME)

        override suspend fun clear() {
            storage.clear()
            tokens.value = null
        }

        override suspend fun save(tokens: AuthTokens) {
            this.tokens.value = tokens

            storage.putString(ACCESS_TOKEN_KEY, tokens.accessToken)
            storage.putString(REFRESH_TOKEN_KEY, tokens.refreshToken)
        }

        private suspend fun getTokens(): AuthTokens? {
            return AuthTokens(
                accessToken = storage.getString(ACCESS_TOKEN_KEY) ?: return null,
                refreshToken = storage.getString(REFRESH_TOKEN_KEY) ?: return null,
            )
        }

        override val tokens = MutableStateFlow(
            runBlocking {
                getTokens().also {
                    Log.d("TAGOLOLOLO", "$it: ")
                }
            }
        )
    }
}