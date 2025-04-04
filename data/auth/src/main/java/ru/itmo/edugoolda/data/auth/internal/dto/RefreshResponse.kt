package ru.itmo.edugoolda.data.auth.internal.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.itmo.edugoolda.data.auth.internal.domain.AuthTokens

@Serializable
internal data class RefreshResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String
)

internal fun RefreshResponse.toDomain() = AuthTokens(
    accessToken = accessToken,
    refreshToken = refreshToken
)
