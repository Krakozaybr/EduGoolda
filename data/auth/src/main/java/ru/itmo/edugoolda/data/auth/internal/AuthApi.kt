package ru.itmo.edugoolda.data.auth.internal

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import ru.itmo.edugoolda.data.auth.internal.dto.AuthResponse
import ru.itmo.edugoolda.data.auth.internal.dto.LoginRequest
import ru.itmo.edugoolda.data.auth.internal.dto.LogoutRequest
import ru.itmo.edugoolda.data.auth.internal.dto.RefreshRequest
import ru.itmo.edugoolda.data.auth.internal.dto.RefreshResponse
import ru.itmo.edugoolda.data.auth.internal.dto.RegisterRequest

internal interface AuthApi {
    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("api/v1/auth/refresh")
    suspend fun refresh(@Body request: RefreshRequest): RefreshResponse

    @POST("api/v1/auth/logout")
    suspend fun logout(@Body request: LogoutRequest)
}