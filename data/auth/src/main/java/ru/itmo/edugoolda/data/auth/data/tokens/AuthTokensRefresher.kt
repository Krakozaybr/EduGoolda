package ru.itmo.edugoolda.data.auth.data.tokens

import ru.itmo.edugoolda.data.auth.domain.AuthTokens

interface AuthTokensRefresher {
    suspend fun refreshTokens(refreshToken: String): AuthTokens
}
