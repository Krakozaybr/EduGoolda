package ru.itmo.edugoolda.data.auth.internal

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import ru.itmo.edugoolda.data.auth.internal.dto.LoginRequest
import ru.itmo.edugoolda.data.auth.internal.dto.RefreshRequest
import ru.itmo.edugoolda.data.auth.internal.dto.RefreshResponse
import ru.itmo.edugoolda.data.auth.internal.dto.RegisterRequest
import ru.itmo.edugoolda.data.auth.internal.dto.RegisterResponse

internal interface AuthApi {
    @POST("/api/v1/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("/api/v1/login")
    suspend fun login(@Body request: LoginRequest): RefreshResponse

    @POST("/api/v1/refresh")
    suspend fun refresh(@Body request: RefreshRequest): RefreshResponse
}