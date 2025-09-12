package ru.itmo.edugoolda.data.auth.internal.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class LogoutRequest(
    @SerialName("refresh_token") val refreshToken: String
)