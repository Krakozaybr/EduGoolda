package ru.itmo.edugoolda.features.lesson

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.replaceCurrent
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.Serializable
import ru.itmo.edugoolda.core.ComponentFactory
import ru.itmo.edugoolda.core.utils.componentScope
import ru.itmo.edugoolda.core.utils.getChild
import ru.itmo.edugoolda.core.utils.safePush
import ru.itmo.edugoolda.core.utils.toStateFlow
import ru.itmo.edugoolda.data.group.group_info.api.GroupInfo
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonId
import ru.itmo.edugoolda.data.solutions.api.SolutionId
import ru.itmo.edugoolda.data.user.api.UserInfoStore
import ru.itmo.edugoolda.data.user.api.UserRole
import ru.itmo.edugoolda.features.lesson.LessonsComponent.InitialConfiguration
import ru.itmo.edugoolda.features.lesson.presentation.createLesson.CreateLessonComponent
import ru.itmo.edugoolda.features.lesson.presentation.createLesson.groupsListForLessonCreating.AddingGroupComponent
import ru.itmo.edugoolda.features.lesson.presentation.studentLessonDetails.StudentLessonDetailsComponent
import ru.itmo.edugoolda.features.lesson.presentation.studentLessonList.LessonInfoListStudentComponent
import ru.itmo.edugoolda.features.lesson.presentation.teacherLessonDetails.TeacherLessonDetailsComponent
import ru.itmo.edugoolda.features.lesson.presentation.teacherLessonList.LessonInfoListTeacherComponent
import ru.itmo.edugoolda.features.lesson.presentation.teacherSolutionDetails.TeacherSolutionDetailsComponent
import ru.itmo.edugoolda.features.solution.createSolutionListComponent
import ru.itmo.edugoolda.features.solution.presentation.SolutionListComponent

class RealLessonsComponent(
    componentContext: ComponentContext,
    initialConfiguration: InitialConfiguration,
    private val communication: LessonsComponent.Communication,
    private val componentFactory: ComponentFactory,
    private val userInfoStore: UserInfoStore,
) : ComponentContext by componentContext, LessonsComponent {

    private val navigation = StackNavigation<Config>()

    override val stack: StateFlow<ChildStack<*, LessonsComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = when (initialConfiguration) {
            is InitialConfiguration.StudentLessonDetails -> Config.StudentLessonDetails(
                initialConfiguration.lessonId
            )

            is InitialConfiguration.TeacherLessonDetails -> Config.TeacherLessonDetails(
                initialConfiguration.lessonId
            )

            is InitialConfiguration.TeacherSolutionDetails -> Config.TeacherSolutionDetails(
                initialConfiguration.solutionId
            )

            InitialConfiguration.StudentLessonInfoList -> Config.StudentLessonInfoList
            InitialConfiguration.TeacherLessonInfoList -> Config.TeacherLessonInfoList
            InitialConfiguration.TeacherSolutionsInfoList -> Config.TeacherSolutionsInfoList
            InitialConfiguration.TeacherLessonCreate -> Config.TeacherLessonCreate
            InitialConfiguration.TeacherAddGroupForLesson -> Config.TeacherAddGroupForLesson
        },
        serializer = Config.serializer(),
        childFactory = ::createChild
    ).toStateFlow(lifecycle)

    private val userRole = userInfoStore.getUserRole()
        .stateIn(componentScope, SharingStarted.Eagerly, null)

    private fun createChild(
        config: Config,
        componentContext: ComponentContext,
    ) = when (config) {
        is Config.StudentLessonDetails -> LessonsComponent.Child.StudentLessonDetails(
            componentFactory.createStudentLessonDetailsComponent(
                componentContext,
                config.lessonId,
                CommunicationResolver()
            )
        )

        is Config.TeacherLessonDetails -> LessonsComponent.Child.TeacherLessonDetails(
            componentFactory.createTeacherLessonDetailsComponent(
                componentContext,
                config.lessonId,
                CommunicationResolver()
            )
        )

        is Config.TeacherSolutionDetails -> LessonsComponent.Child.TeacherSolutionDetails(
            componentFactory.createTeacherSolutionDetailsComponent(
                componentContext,
                config.solutionId,
                CommunicationResolver()
            )
        )

        is Config.StudentLessonInfoList -> LessonsComponent.Child.StudentLessonInfoList(
            componentFactory.createLessonInfoListStudentComponent(
                componentContext,
                CommunicationResolver()
            )
        )

        is Config.TeacherLessonInfoList -> LessonsComponent.Child.TeacherLessonInfoList(
            componentFactory.createTeacherLessonListInfoComponent(
                componentContext,
                CommunicationResolver()
            )
        )

        is Config.TeacherSolutionsInfoList -> LessonsComponent.Child.TeacherSolutionsInfoList(
            componentFactory.createSolutionListComponent(
                componentContext,
                CommunicationResolver()
            )
        )

        is Config.TeacherLessonCreate -> LessonsComponent.Child.TeacherLessonCreate(
            componentFactory.createLessonCreateComponent(
                componentContext,
                CommunicationResolver()
            )
        )

        is Config.TeacherAddGroupForLesson -> LessonsComponent.Child.TeacherAddGroupForLesson(
            componentFactory.createAddingGroupComponent(
                componentContext,
                CommunicationResolver()
            )
        )
    }

    private inner class CommunicationResolver :
        StudentLessonDetailsComponent.Communication,
        TeacherLessonDetailsComponent.Communication,
        LessonInfoListTeacherComponent.Communication,
        LessonInfoListStudentComponent.Communication,
        SolutionListComponent.Communication,
        CreateLessonComponent.Communication,
        TeacherSolutionDetailsComponent.Communication,
        AddingGroupComponent.Communication {

        override fun onReturnBackStudentLessonDetails() = goBack()
        override fun onReturnBackTeacherLessonDetails() = goBack()
        override fun onTeacherLessonDeleted() = goBack()
        override fun onReturnBackTeacherSolutionDetails() = goBack()

        override fun onLessonDetailsRequested(lessonId: LessonId) {
            navigation.safePush(
                when (userRole.value) {
                    UserRole.Teacher -> Config.TeacherLessonDetails(lessonId)
                    else -> Config.StudentLessonDetails(lessonId)
                }
            )
        }

        override fun onSolutionDetailsRequested(solutionId: SolutionId) {
            communication.onSolutionDetails(solutionId)
        }

        override fun onReturnBackFromSolutionListRequested() {
            communication.onReturnBackFromSolutionListRequested()
        }

        override fun onCancelLessonCreation() {
            goBack()
        }

        override fun onLessonCreated(lessonId: LessonId) {
            navigation.replaceCurrent(
                Config.TeacherLessonDetails(lessonId)
            )
        }

        override fun onGroupAddRequested() {
            navigation.safePush(
                Config.TeacherAddGroupForLesson
            )
        }

        override fun onGroupAddedRequested(groupInfo: GroupInfo) {
            stack.value
                .getChild<LessonsComponent.Child.TeacherLessonCreate>()
                ?.component
                ?.onGroupAdded(groupInfo)
            goBack()
        }

        override fun onCancelGroupAddingForLessonRequested() {
            goBack()
        }
    }

    private fun goBack() = when {
        stack.value.items.size == 1 -> communication.onCancel()
        else -> navigation.pop()
    }

    @Serializable
    private sealed interface Config {

        @Serializable
        data class StudentLessonDetails(val lessonId: LessonId) : Config

        @Serializable
        data class TeacherLessonDetails(val lessonId: LessonId) : Config

        @Serializable
        data class TeacherSolutionDetails(val solutionId: SolutionId) : Config

        @Serializable
        data object TeacherLessonInfoList : Config

        @Serializable
        data object StudentLessonInfoList : Config

        @Serializable
        data object TeacherSolutionsInfoList : Config

        @Serializable
        data object TeacherLessonCreate : Config

        @Serializable
        data object TeacherAddGroupForLesson : Config
    }
}