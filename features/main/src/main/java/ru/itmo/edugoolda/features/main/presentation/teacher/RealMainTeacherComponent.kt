package ru.itmo.edugoolda.features.main.presentation.teacher

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import ru.itmo.edugoolda.core.ComponentFactory
import ru.itmo.edugoolda.core.dialog.simple.SimpleDialogControl
import ru.itmo.edugoolda.core.dialog.simple.simpleDialogControl
import ru.itmo.edugoolda.core.utils.toStateFlow
import ru.itmo.edugoolda.features.group.createTeacherGroupsComponent
import ru.itmo.edugoolda.features.group.presentation.teacherGroups.TeacherGroupsComponent
import ru.itmo.edugoolda.features.home.createHomeTeacherComponent
import ru.itmo.edugoolda.features.home.presentation.teacher.HomeTeacherComponent
import ru.itmo.edugoolda.features.lesson.createTeacherLessonListInfoComponent
import ru.itmo.edugoolda.features.lesson.presentation.teacherLessonList.LessonInfoListTeacherComponent
import ru.itmo.edugoolda.features.main.presentation.teacher.MainTeacherComponent.Tab
import ru.itmo.edugoolda.features.profile.createProfileComponent
import ru.itmo.edugoolda.features.profile.presentation.viewProfile.ProfileComponent

class RealMainTeacherComponent(
    componentContext: ComponentContext,
    private val communication: MainTeacherComponent.Communication,
    private val componentFactory: ComponentFactory
) : ComponentContext by componentContext, MainTeacherComponent {

    private val navigation = StackNavigation<Config>()

    override val stack: StateFlow<ChildStack<*, MainTeacherComponent.Child>> =
        childStack(
            source = navigation,
            initialConfiguration = Config.Home,
            serializer = Config.serializer(),
            childFactory = ::createChild
        ).toStateFlow(lifecycle)

    override val simpleDialogControl: SimpleDialogControl<Unit> =
        simpleDialogControl("simpleDialogControl")

    override val selectedTab = MutableStateFlow(Tab.Home)

    override fun onTabClick(tab: Tab) {
        navigation.bringToFront(
            when (tab) {
                Tab.Create -> {
                    simpleDialogControl.show(Unit)
                    return
                }

                Tab.Home -> Config.Home
                Tab.Groups -> Config.Groups
                Tab.Profile -> Config.Profile
                Tab.Lessons -> Config.Lessons
            }
        )

        selectedTab.value = tab
    }

    override fun onCreateLessonClick() {
        simpleDialogControl.dismiss()
        communication.createLessonRequested()
    }

    override fun onCreateGroupClick() {
        simpleDialogControl.dismiss()
        communication.createGroupRequested()
    }

    private inner class ChildCommunication :
        TeacherGroupsComponent.Communication,
        HomeTeacherComponent.Communication,
        MainTeacherComponent.Communication by communication,
        LessonInfoListTeacherComponent.Communication,
        ProfileComponent.Communication

    private fun createChild(
        config: Config,
        componentContext: ComponentContext
    ) = when (config) {
        Config.Groups -> MainTeacherComponent.Child.Groups(
            componentFactory.createTeacherGroupsComponent(
                componentContext,
                ChildCommunication()
            )
        )

        Config.Home -> MainTeacherComponent.Child.Home(
            componentFactory.createHomeTeacherComponent(
                componentContext,
                ChildCommunication()
            )
        )

        Config.Profile -> MainTeacherComponent.Child.Profile(
            componentFactory.createProfileComponent(
                componentContext,
                null,
                ChildCommunication()
            )
        )

        Config.Lessons -> MainTeacherComponent.Child.Lessons(
            componentFactory.createTeacherLessonListInfoComponent(
                componentContext,
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