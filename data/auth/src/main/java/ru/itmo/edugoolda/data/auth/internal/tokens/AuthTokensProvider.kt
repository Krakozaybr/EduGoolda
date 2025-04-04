package ru.itmo.edugoolda.data.auth.internal.tokens

import kotlinx.coroutines.flow.StateFlow
import ru.itmo.edugoolda.data.auth.internal.domain.AuthTokens

internal interface AuthTokensProvider {
    val tokens: StateFlow<AuthTokens?>
}