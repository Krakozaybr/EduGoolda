package ru.itmo.edugoolda.features.lesson

import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonId
import ru.itmo.edugoolda.data.solutions.api.SolutionId
import ru.itmo.edugoolda.features.lesson.presentation.createLesson.CreateLessonComponent
import ru.itmo.edugoolda.features.lesson.presentation.createLesson.groupsListForLessonCreating.AddingGroupComponent
import ru.itmo.edugoolda.features.lesson.presentation.studentLessonDetails.StudentLessonDetailsComponent
import ru.itmo.edugoolda.features.lesson.presentation.studentLessonList.LessonInfoListStudentComponent
import ru.itmo.edugoolda.features.lesson.presentation.teacherLessonDetails.TeacherLessonDetailsComponent
import ru.itmo.edugoolda.features.lesson.presentation.teacherLessonList.LessonInfoListTeacherComponent
import ru.itmo.edugoolda.features.lesson.presentation.teacherSolutionDetails.TeacherSolutionDetailsComponent
import ru.itmo.edugoolda.features.solution.presentation.SolutionListComponent

interface LessonsComponent {

    val stack: StateFlow<ChildStack<*, Child>>

    @Serializable
    sealed interface InitialConfiguration {
        @Serializable
        data class StudentLessonDetails(val lessonId: LessonId) : InitialConfiguration

        @Serializable
        data class TeacherLessonDetails(val lessonId: LessonId) : InitialConfiguration

        @Serializable
        data object TeacherLessonInfoList : InitialConfiguration

        @Serializable
        data object TeacherSolutionsInfoList : InitialConfiguration

        @Serializable
        data object TeacherLessonCreate : InitialConfiguration

        @Serializable
        data object StudentLessonInfoList : InitialConfiguration

        @Serializable
        data class TeacherSolutionDetails(val solutionId: SolutionId) : InitialConfiguration

        @Serializable
        data object TeacherAddGroupForLesson : InitialConfiguration
    }

    sealed interface Child {
        data class StudentLessonDetails(val component: StudentLessonDetailsComponent) : Child
        data class TeacherLessonDetails(val component: TeacherLessonDetailsComponent) : Child
        data class TeacherSolutionDetails(val component: TeacherSolutionDetailsComponent) : Child
        data class TeacherLessonInfoList(val component: LessonInfoListTeacherComponent) : Child
        data class StudentLessonInfoList(val component: LessonInfoListStudentComponent) : Child
        data class TeacherSolutionsInfoList(val component: SolutionListComponent) : Child
        data class TeacherLessonCreate(val component: CreateLessonComponent) : Child
        data class TeacherAddGroupForLesson(val component: AddingGroupComponent) : Child
    }

    interface Communication {
        fun onCancel()
        fun onSolutionDetails(solutionId: SolutionId)
        fun onReturnBackFromSolutionListRequested()
    }
}