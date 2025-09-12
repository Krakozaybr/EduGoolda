package ru.itmo.edugoolda.data.auth.api

import ru.itmo.edugoolda.data.auth.api.domain.Email
import ru.itmo.edugoolda.data.auth.api.domain.Password
import ru.itmo.edugoolda.data.user.api.UserRole

interface AuthRepository {
    suspend fun login(email: Email, password: Password)
    suspend fun register(email: Email, password: Password, name: String, role: UserRole)
    suspend fun logout()
}