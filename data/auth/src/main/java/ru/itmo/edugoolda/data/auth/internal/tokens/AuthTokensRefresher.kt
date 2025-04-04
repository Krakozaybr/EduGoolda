package ru.itmo.edugoolda.data.auth.internal.tokens

import ru.itmo.edugoolda.data.auth.internal.domain.AuthTokens

internal interface AuthTokensRefresher {
    suspend fun refreshTokens(refreshToken: String): AuthTokens
}
