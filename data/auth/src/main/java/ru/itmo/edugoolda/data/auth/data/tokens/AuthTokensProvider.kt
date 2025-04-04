package ru.itmo.edugoolda.data.auth.data.tokens

import kotlinx.coroutines.flow.StateFlow
import ru.itmo.edugoolda.data.auth.domain.AuthTokens

interface AuthTokensProvider {
    val tokens: StateFlow<AuthTokens?>
}