package ru.itmo.edugoolda.features.group.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import kotlinx.serialization.Serializable
import ru.itmo.edugoolda.core.ComponentFactory
import ru.itmo.edugoolda.core.utils.toStateFlow
import ru.itmo.edugoolda.features.group.createGroupCreateComponent
import ru.itmo.edugoolda.features.group.presentation.create.GroupCreateComponent

class RealGroupComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory,
) : GroupComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    override val childStack = childStack(
        source = navigation,
        initialConfiguration = Config.Creation,
        serializer = Config.serializer(),
        handleBackButton = true,
        childFactory = ::createChild
    ).toStateFlow(lifecycle)

    private inner class CommunicationResolver : GroupCreateComponent.Communication {
        override fun onGroupCreated(id: String) {
            TODO("Not yet implemented")
        }

        override fun onCancel() {
            TODO("Not yet implemented")
        }
    }

    private fun createChild(
        config: Config,
        componentContext: ComponentContext
    ): GroupComponent.Child = when (config) {
        Config.Creation -> GroupComponent.Child.Creation(
            componentFactory.createGroupCreateComponent(
                componentContext,
                CommunicationResolver())
        )
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Creation : Config
    }
}