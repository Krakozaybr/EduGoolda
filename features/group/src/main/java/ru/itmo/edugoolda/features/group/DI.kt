package ru.itmo.edugoolda.features.group

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import ru.itmo.edugoolda.core.ComponentFactory
import ru.itmo.edugoolda.features.group.presentation.create.GroupCreateComponent
import ru.itmo.edugoolda.features.group.presentation.create.RealGroupCreateComponent

fun ComponentFactory.createGroupCreateComponent(
    componentContext: ComponentContext,
    communication: GroupCreateComponent.Communication,
): GroupCreateComponent {
    return RealGroupCreateComponent(componentContext, communication, get(), get())
}