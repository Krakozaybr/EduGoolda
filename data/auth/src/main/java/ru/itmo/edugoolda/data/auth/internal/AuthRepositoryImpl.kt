package ru.itmo.edugoolda.data.auth.internal

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.itmo.edugoolda.data.auth.api.AuthRepository
import ru.itmo.edugoolda.data.auth.api.AuthStatusProvider
import ru.itmo.edugoolda.data.auth.api.domain.Email
import ru.itmo.edugoolda.data.auth.api.domain.Password
import ru.itmo.edugoolda.data.auth.internal.domain.AuthTokens
import ru.itmo.edugoolda.data.auth.internal.dto.AuthResponse
import ru.itmo.edugoolda.data.auth.internal.dto.LoginRequest
import ru.itmo.edugoolda.data.auth.internal.dto.LogoutRequest
import ru.itmo.edugoolda.data.auth.internal.dto.RefreshRequest
import ru.itmo.edugoolda.data.auth.internal.dto.RegisterRequest
import ru.itmo.edugoolda.data.auth.internal.dto.toDomain
import ru.itmo.edugoolda.data.auth.internal.tokens.AuthTokensProvider
import ru.itmo.edugoolda.data.auth.internal.tokens.AuthTokensRefresher
import ru.itmo.edugoolda.data.auth.internal.tokens.AuthTokensStorage
import ru.itmo.edugoolda.data.user.api.UserId
import ru.itmo.edugoolda.data.user.api.UserInfoStore
import ru.itmo.edugoolda.data.user.api.UserRole

internal class AuthRepositoryImpl(
    private val api: AuthApi,
    private val tokensStorage: AuthTokensStorage,
    private val authTokensProvider: AuthTokensProvider,
    private val userInfoStore: UserInfoStore
) : AuthRepository, AuthTokensRefresher, AuthStatusProvider {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

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
        saveAuthData(response)
    }

    override suspend fun register(
        email: Email,
        password: Password,
        name: String,
        role: UserRole
    ) {
        val roleType = when (role) {
            UserRole.Teacher -> "teacher"
            UserRole.Student -> "student"
        }
        val response = api.register(
            RegisterRequest(
                email = email.value,
                password = password.value,
                name = name,
                role = roleType
            )
        )
        saveAuthData(response)
    }

    override suspend fun logout() {
        val tokens = authTokensProvider.tokens.value ?: return
        api.logout(
            LogoutRequest(
                refreshToken = tokens.refreshToken
            )
        )
        tokensStorage.clear()
    }

    private suspend fun saveAuthData(response: AuthResponse) {
        coroutineScope {
            launch {
                userInfoStore.setUserId(UserId(response.userId))
            }
            launch {
                userInfoStore.setUserRole(
                    when (response.userRole) {
                        "student" -> UserRole.Student
                        else -> UserRole.Teacher
                    }
                )
            }
            launch {
                tokensStorage.save(
                    AuthTokens(
                        accessToken = response.accessToken,
                        refreshToken = response.refreshToken,
                    )
                )
            }
        }
    }

    override suspend fun refreshTokens(refreshToken: String): AuthTokens {
        val response = api.refresh(RefreshRequest(refreshToken)).toDomain()

        tokensStorage.save(
            AuthTokens(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken,
            )
        )

        return response
    }
}