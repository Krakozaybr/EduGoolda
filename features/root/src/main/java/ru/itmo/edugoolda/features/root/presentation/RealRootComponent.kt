package ru.itmo.edugoolda.features.root.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import kotlinx.serialization.Serializable
import ru.itmo.edugoolda.core.ComponentFactory
import ru.itmo.edugoolda.core.createMessageComponent
import ru.itmo.edugoolda.core.utils.toStateFlow

class RealRootComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory
) : ComponentContext by componentContext, RootComponent {

    private val navigation = StackNavigation<ChildConfig>()

    override val childStack = childStack(
        source = navigation,
        initialConfiguration = TODO(),
        serializer = ChildConfig.serializer(),
        handleBackButton = true,
        childFactory = ::createChild
    ).toStateFlow(lifecycle)

    override val messageComponent = componentFactory.createMessageComponent(
        childContext(key = "message")
    )

    private fun createChild(
        config: ChildConfig,
        componentContext: ComponentContext
    ): RootComponent.Child = TODO()

    @Serializable
    sealed interface ChildConfig {
        // TODO: Add configs
    }
}
