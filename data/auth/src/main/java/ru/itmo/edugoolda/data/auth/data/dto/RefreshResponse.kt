package ru.itmo.edugoolda.data.auth.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.itmo.edugoolda.data.auth.domain.AuthTokens

@Serializable
data class RefreshResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String
)

fun RefreshResponse.toDomain() = AuthTokens(
    accessToken = accessToken,
    refreshToken = refreshToken
)
