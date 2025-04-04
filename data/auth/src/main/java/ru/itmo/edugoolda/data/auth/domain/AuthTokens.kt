package ru.itmo.edugoolda.data.auth.domain

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String
)
