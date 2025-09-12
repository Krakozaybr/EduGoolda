package ru.itmo.edugoolda.features.root.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.router.stack.replaceCurrent
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import ru.itmo.edugoolda.core.ComponentFactory
import ru.itmo.edugoolda.core.createMessageComponent
import ru.itmo.edugoolda.core.utils.componentScope
import ru.itmo.edugoolda.core.utils.safePush
import ru.itmo.edugoolda.core.utils.toStateFlow
import ru.itmo.edugoolda.data.auth.api.AuthStatusProvider
import ru.itmo.edugoolda.data.group.group_list.api.GroupId
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonId
import ru.itmo.edugoolda.data.solutions.api.SolutionId
import ru.itmo.edugoolda.data.user.api.UserInfoStore
import ru.itmo.edugoolda.data.user.api.UserRole
import ru.itmo.edugoolda.features.auth.presentation.auth.AuthComponent
import ru.itmo.edugoolda.features.group.createGroupComponent
import ru.itmo.edugoolda.features.group.presentation.GroupComponent
import ru.itmo.edugoolda.features.join_requests.createJoinRequestsStudentComponent
import ru.itmo.edugoolda.features.join_requests.createJoinRequestsTeacherComponent
import ru.itmo.edugoolda.features.join_requests.presentation.student.JoinRequestsStudentComponent
import ru.itmo.edugoolda.features.join_requests.presentation.teacher.JoinRequestsTeacherComponent
import ru.itmo.edugoolda.features.lesson.LessonsComponent
import ru.itmo.edugoolda.features.lesson.createLessonsComponent
import ru.itmo.edugoolda.features.lesson.presentation.teacherLessonList.LessonInfoListTeacherComponent
import ru.itmo.edugoolda.features.main.createMainStudentComponent
import ru.itmo.edugoolda.features.main.createMainTeacherComponent
import ru.itmo.edugoolda.features.main.presentation.student.MainStudentComponent
import ru.itmo.edugoolda.features.main.presentation.teacher.MainTeacherComponent
import ru.itmo.edugoolda.features.root.createAuthComponent
import ru.itmo.edugoolda.features.root.createStartComponent
import ru.itmo.edugoolda.features.root.presentation.start.StartComponent

class RealRootComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory,
    userInfoStore: UserInfoStore,
    authStatusProvider: AuthStatusProvider,
) : ComponentContext by componentContext, RootComponent {

    private val userRole =
        userInfoStore.getUserRole().stateIn(componentScope, SharingStarted.Eagerly, null)

    private val navigation = StackNavigation<Config>()

    init {
        authStatusProvider
            .isAuthorized
            .onEach {
                if (!it) {
                    navigation.safePush(Config.Auth)
                }
            }
            .launchIn(componentScope)
    }

    override val childStack = childStack(
        source = navigation,
        initialConfiguration = Config.Start,
        serializer = Config.serializer(),
        handleBackButton = true,
        childFactory = ::createChild
    ).toStateFlow(lifecycle)

    override val messageComponent = componentFactory.createMessageComponent(
        childContext(key = "message")
    )

    private fun createChild(
        config: Config,
        componentContext: ComponentContext,
    ): RootComponent.Child = when (config) {
        Config.Auth -> RootComponent.Child.Auth(
            componentFactory.createAuthComponent(
                componentContext,
                GeneralCommunicationResolver()
            )
        )

        Config.MainStudent -> RootComponent.Child.StudentMain(
            componentFactory.createMainStudentComponent(
                componentContext,
                StudentCommunicationResolver()
            )
        )

        Config.MainTeacher -> RootComponent.Child.TeacherMain(
            componentFactory.createMainTeacherComponent(
                componentContext,
                TeacherCommunicationResolver()
            )
        )

        is Config.Group -> RootComponent.Child.Group(
            componentFactory.createGroupComponent(
                componentContext,
                GeneralCommunicationResolver(),
                config.configuration,
            )
        )

        Config.JoinRequestsTeacher -> RootComponent.Child.JoinRequestsTeacher(
            componentFactory.createJoinRequestsTeacherComponent(
                componentContext,
                TeacherCommunicationResolver()
            )
        )

        Config.JoinRequestsStudent -> RootComponent.Child.JoinRequestsStudent(
            componentFactory.createJoinRequestsStudentComponent(
                componentContext,
                StudentCommunicationResolver()
            )
        )

        is Config.Lessons -> RootComponent.Child.Lessons(
            componentFactory.createLessonsComponent(
                componentContext,
                config.configuration,
                GeneralCommunicationResolver()
            )
        )

        Config.Start -> RootComponent.Child.Start(
            componentFactory.createStartComponent(
                componentContext,
                GeneralCommunicationResolver()
            )
        )
    }

    private inner class GeneralCommunicationResolver :
        AuthComponent.Communication,
        LessonsComponent.Communication,
        StartComponent.Communication,
        GroupComponent.Communication {
        override fun onAuthEnded() {
            componentScope.launch {
                val role = userRole.filterNotNull().first()
                navigation.replaceAll(
                    when (role) {
                        UserRole.Teacher -> Config.MainTeacher
                        UserRole.Student -> Config.MainStudent
                    }
                )
            }
        }

        override fun onCancel() {
            navigation.pop()
        }

        override fun onSolutionDetails(solutionId: SolutionId) {
            navigation.safePush(
                Config.Lessons(
                    LessonsComponent.InitialConfiguration.TeacherSolutionDetails(solutionId)
                )
            )
        }

        override fun onReturnBackFromSolutionListRequested() {
            navigation.pop()
        }

        override fun authRequired() {
            navigation.replaceAll(Config.Auth)
        }

        override fun teacherDetailsRequired() {
            navigation.replaceCurrent(Config.MainTeacher)
        }

        override fun studentDetailsRequired() {
            navigation.replaceCurrent(Config.MainStudent)
        }
    }

    private inner class TeacherCommunicationResolver :
        MainTeacherComponent.Communication,
        JoinRequestsTeacherComponent.Communication,
        LessonInfoListTeacherComponent.Communication {
        override fun onGroupDetailsRequested(id: GroupId) {
            navigation.safePush(
                Config.Group(
                    GroupComponent.InitialConfiguration.TeacherGroupDetails(id)
                )
            )
        }

        override fun onSolutionDetailsRequested(solutionId: SolutionId) {
            navigation.safePush(
                Config.Lessons(
                    LessonsComponent.InitialConfiguration.TeacherSolutionDetails(solutionId)
                )
            )
        }

        override fun onAllSolutionsRequested() {
            navigation.safePush(
                Config.Lessons(
                    LessonsComponent.InitialConfiguration.TeacherSolutionsInfoList
                )
            )
        }

        override fun onAllJoinRequestsRequested() {
            navigation.safePush(Config.JoinRequestsTeacher)
        }

        override fun createLessonRequested() {
            navigation.safePush(
                Config.Lessons(
                    LessonsComponent.InitialConfiguration.TeacherLessonCreate
                )
            )
        }

        override fun createGroupRequested() {
            navigation.safePush(
                Config.Group(
                    GroupComponent.InitialConfiguration.CreateGroup
                )
            )
        }

        override fun onReturnBackFromJoinRequestsRequested() {
            navigation.pop()
        }

        override fun onLessonDetailsRequested(lessonId: LessonId) {
            navigation.safePush(
                Config.Lessons(
                    LessonsComponent.InitialConfiguration.TeacherLessonDetails(lessonId)
                )
            )
        }

        override fun onLogoutRequested() {
            navigation.replaceAll(Config.Auth)
        }
    }

    private inner class StudentCommunicationResolver : MainStudentComponent.Communication,
        JoinRequestsStudentComponent.Communication {
        override fun onLessonDetailsRequested(lessonId: LessonId) {
            navigation.safePush(
                Config.Lessons(
                    LessonsComponent.InitialConfiguration.StudentLessonDetails(lessonId)
                )
            )
        }

        override fun onLogoutRequested() {
            navigation.replaceAll(Config.Auth)
        }

        override fun onGroupDetailsRequested(id: GroupId) {
            navigation.safePush(
                Config.Group(
                    GroupComponent.InitialConfiguration.StudentGroupDetails(id)
                )
            )
        }

        override fun onGroupAddRequested() {
            navigation.safePush(
                Config.Group(
                    GroupComponent.InitialConfiguration.AddGroup
                )
            )
        }

        override fun onAllJoinRequestsRequested() {
            navigation.safePush(Config.JoinRequestsStudent)
        }

        override fun onReturnBackFromJoinRequestsRequested() {
            navigation.pop()
        }
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Auth : Config

        @Serializable
        data object MainStudent : Config

        @Serializable
        data object MainTeacher : Config

        @Serializable
        data class Group(
            val configuration: GroupComponent.InitialConfiguration,
        ) : Config

        @Serializable
        data class Lessons(
            val configuration: LessonsComponent.InitialConfiguration,
        ) : Config

        @Serializable
        data object JoinRequestsTeacher : Config

        @Serializable
        data object JoinRequestsStudent : Config

        @Serializable
        data object Start : Config
    }
}
