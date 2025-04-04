package ru.itmo.edugoolda.data.auth.data

import ru.itmo.edugoolda.data.auth.data.dto.LoginRequest
import ru.itmo.edugoolda.data.auth.data.dto.RefreshRequest
import ru.itmo.edugoolda.data.auth.data.dto.RegisterRequest
import ru.itmo.edugoolda.data.auth.data.dto.toDomain
import ru.itmo.edugoolda.data.auth.data.tokens.AuthTokensRefresher
import ru.itmo.edugoolda.data.auth.data.tokens.AuthTokensStorage
import ru.itmo.edugoolda.data.auth.domain.AuthTokens
import ru.itmo.edugoolda.data.auth.domain.Email
import ru.itmo.edugoolda.data.auth.domain.Password

internal class AuthRepositoryImpl(
    private val api: AuthApi,
    private val tokensStorage: AuthTokensStorage
) : AuthRepository, AuthTokensRefresher {
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