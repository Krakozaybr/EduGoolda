package ru.itmo.edugoolda.data.auth.data

import ru.itmo.edugoolda.data.auth.domain.Email
import ru.itmo.edugoolda.data.auth.domain.Password

interface AuthRepository {
    suspend fun login(email: Email, password: Password)
    suspend fun register(email: Email, password: Password, name: String, role: String)
}