package ru.itmo.edugoolda.data.auth.data

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import ru.itmo.edugoolda.data.auth.data.dto.LoginRequest
import ru.itmo.edugoolda.data.auth.data.dto.RefreshRequest
import ru.itmo.edugoolda.data.auth.data.dto.RefreshResponse
import ru.itmo.edugoolda.data.auth.data.dto.RegisterRequest
import ru.itmo.edugoolda.data.auth.data.dto.RegisterResponse

interface AuthApi {
    @POST("/api/v1/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("/api/v1/login")
    suspend fun login(@Body request: LoginRequest): RefreshResponse

    @POST("/api/v1/refresh")
    suspend fun refresh(@Body request: RefreshRequest): RefreshResponse
}