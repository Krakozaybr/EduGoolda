package ru.itmo.edugoolda.data.auth.internal

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.itmo.edugoolda.data.auth.api.AuthRepository
import ru.itmo.edugoolda.data.auth.api.AuthStatusProvider
import ru.itmo.edugoolda.data.auth.api.domain.Email
import ru.itmo.edugoolda.data.auth.api.domain.Password
import ru.itmo.edugoolda.data.auth.internal.domain.AuthTokens
import ru.itmo.edugoolda.data.auth.internal.dto.LoginRequest
import ru.itmo.edugoolda.data.auth.internal.dto.RefreshRequest
import ru.itmo.edugoolda.data.auth.internal.dto.RegisterRequest
import ru.itmo.edugoolda.data.auth.internal.dto.toDomain
import ru.itmo.edugoolda.data.auth.internal.tokens.AuthTokensProvider
import ru.itmo.edugoolda.data.auth.internal.tokens.AuthTokensRefresher
import ru.itmo.edugoolda.data.auth.internal.tokens.AuthTokensStorage

internal class AuthRepositoryImpl(
    private val api: AuthApi,
    private val tokensStorage: AuthTokensStorage,
    authTokensProvider: AuthTokensProvider,
    coroutineScope: CoroutineScope
) : AuthRepository, AuthTokensRefresher, AuthStatusProvider {

    override val isAuthorized = authTokensProvider.tokens
        .map { it != null }
        .stateIn(coroutineScope, SharingStarted.Eagerly, false)

    override suspend fun login(email: Email, password: Password) {
        val response = api.login(
            LoginRequest(
                email = email.value,
                password = password.value
            )
        )
        tokensStorage.save(
            AuthTokens(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken,
            )
        )
    }

    override suspend fun register(
        email: Email,
        password: Password,
        name: String,
        role: String
    ) {
        val response = api.register(
            RegisterRequest(
                email = email.value,
                password = password.value,
                name = name,
                role = role
            )
        )
        tokensStorage.save(
            AuthTokens(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken,
            )
        )
    }

    override suspend fun refreshTokens(refreshToken: String): AuthTokens {
        return api.refresh(RefreshRequest(refreshToken)).toDomain()
    }
}