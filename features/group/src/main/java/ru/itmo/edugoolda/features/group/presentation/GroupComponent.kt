package ru.itmo.edugoolda.features.group.presentation

import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow
import ru.itmo.edugoolda.features.group.presentation.create.GroupCreateComponent

interface GroupComponent {

    val childStack: StateFlow<ChildStack<*, Child>>

    sealed interface Child {
        class Creation(val instance: GroupCreateComponent) : Child
    }
}