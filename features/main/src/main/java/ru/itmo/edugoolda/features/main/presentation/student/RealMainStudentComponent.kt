package ru.itmo.edugoolda.features.main.presentation.student

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable
import ru.itmo.edugoolda.core.ComponentFactory
import ru.itmo.edugoolda.core.utils.toStateFlow
import ru.itmo.edugoolda.features.group.createStudentGroupsComponent
import ru.itmo.edugoolda.features.group.presentation.studentGroups.StudentGroupsComponent
import ru.itmo.edugoolda.features.home.createHomeStudentComponent
import ru.itmo.edugoolda.features.home.presentation.student.HomeStudentComponent
import ru.itmo.edugoolda.features.lesson.createLessonInfoListStudentComponent
import ru.itmo.edugoolda.features.lesson.presentation.studentLessonList.LessonInfoListStudentComponent
import ru.itmo.edugoolda.features.main.presentation.student.MainStudentComponent.Tab
import ru.itmo.edugoolda.features.profile.createProfileComponent
import ru.itmo.edugoolda.features.profile.presentation.viewProfile.ProfileComponent

class RealMainStudentComponent(
    componentContext: ComponentContext,
    private val communication: MainStudentComponent.Communication,
    private val componentFactory: ComponentFactory,
) : ComponentContext by componentContext, MainStudentComponent {

    private val navigation = StackNavigation<Config>()

    override val selectedTab = MutableStateFlow(Tab.Home)

    override val stack: StateFlow<ChildStack<*, MainStudentComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Home,
        serializer = Config.serializer(),
        childFactory = ::createChild
    ).toStateFlow(lifecycle)

    override fun onTabClick(tab: Tab) {
        selectedTab.update { tab }

        navigation.bringToFront(
            when (tab) {
                Tab.Home -> Config.Home
                Tab.Lessons -> Config.Lessons
                Tab.Groups -> Config.Groups
                Tab.Profile -> Config.Profile
            }
        )
    }

    private inner class ChildCommunication : MainStudentComponent.Communication by communication,
        StudentGroupsComponent.Communication,
        HomeStudentComponent.Communication,
        LessonInfoListStudentComponent.Communication,
        ProfileComponent.Communication {
        override fun onAllSolutionsRequested() = onTabClick(Tab.Lessons)
    }

    private fun createChild(
        config: Config,
        componentContext: ComponentContext,
    ) = when (config) {
        Config.Groups -> MainStudentComponent.Child.Groups(
            componentFactory.createStudentGroupsComponent(
                componentContext,
                ChildCommunication()
            )
        )

        Config.Home -> MainStudentComponent.Child.Home(
            componentFactory.createHomeStudentComponent(
                componentContext,
                ChildCommunication()
            )
        )

        Config.Lessons -> MainStudentComponent.Child.Lessons(
            componentFactory.createLessonInfoListStudentComponent(
                componentContext,
                ChildCommunication()
            )
        )

        Config.Profile -> MainStudentComponent.Child.Profile(
            componentFactory.createProfileComponent(
                componentContext,
                null,
                ChildCommunication()
            )
        )
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Home : Config

        @Serializable
        data object Groups : Config

        @Serializable
        data object Profile : Config

        @Serializable
        data object Lessons : Config
    }
}