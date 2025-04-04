package ru.itmo.edugoolda.data.auth.internal.domain

internal data class AuthTokens(
    val accessToken: String,
    val refreshToken: String
)
