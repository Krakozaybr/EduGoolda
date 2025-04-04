package ru.itmo.edugoolda.data.auth.api

import kotlinx.coroutines.flow.StateFlow

interface AuthStatusProvider {
    val isAuthorized: StateFlow<Boolean>
}