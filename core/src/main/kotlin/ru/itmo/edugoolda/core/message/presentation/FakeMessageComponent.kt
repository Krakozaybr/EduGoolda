package ru.itmo.edugoolda.core.message.presentation

import dev.icerock.moko.resources.desc.Raw
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.flow.MutableStateFlow
import ru.itmo.edugoolda.core.message.domain.Message

class FakeMessageComponent : MessageComponent {

    override val visibleMessage = MutableStateFlow(
        Message(StringDesc.Raw("Message"))
    )

    override fun onActionClick() = Unit
}
