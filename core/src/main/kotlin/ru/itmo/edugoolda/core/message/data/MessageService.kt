package ru.itmo.edugoolda.core.message.data

import kotlinx.coroutines.flow.Flow
import ru.itmo.edugoolda.core.message.domain.Message

/**
 * A service for centralized message showing
 */
interface MessageService {

    val messageFlow: Flow<Message>

    fun showMessage(message: Message)
}