package ru.itmo.edugoolda.features.lesson

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import ru.itmo.edugoolda.features.lesson.presentation.createLesson.CreateLessonUi
import ru.itmo.edugoolda.features.lesson.presentation.createLesson.groupsListForLessonCreating.AddingGroupUi
import ru.itmo.edugoolda.features.lesson.presentation.studentLessonDetails.StudentLessonDetailsUi
import ru.itmo.edugoolda.features.lesson.presentation.studentLessonList.LessonInfoListStudentUi
import ru.itmo.edugoolda.features.lesson.presentation.teacherLessonDetails.TeacherLessonDetailsUi
import ru.itmo.edugoolda.features.lesson.presentation.teacherLessonList.LessonInfoListTeacherUi
import ru.itmo.edugoolda.features.lesson.presentation.teacherSolutionDetails.TeacherSolutionDetailsUi
import ru.itmo.edugoolda.features.solution.presentation.SolutionListUi

@Composable
fun LessonsUi(
    component: LessonsComponent,
    modifier: Modifier = Modifier
) {
    val stack by component.stack.collectAsState()

    Children(stack, modifier) {
        when (val instance = it.instance) {
            is LessonsComponent.Child.StudentLessonDetails -> StudentLessonDetailsUi(instance.component)
            is LessonsComponent.Child.TeacherLessonDetails -> TeacherLessonDetailsUi(instance.component)
            is LessonsComponent.Child.TeacherSolutionDetails -> TeacherSolutionDetailsUi(instance.component)
            is LessonsComponent.Child.StudentLessonInfoList -> LessonInfoListStudentUi(instance.component)
            is LessonsComponent.Child.TeacherLessonInfoList -> LessonInfoListTeacherUi(instance.component)
            is LessonsComponent.Child.TeacherLessonCreate -> CreateLessonUi(instance.component)
            is LessonsComponent.Child.TeacherSolutionsInfoList -> SolutionListUi(instance.component)
            is LessonsComponent.Child.TeacherAddGroupForLesson -> AddingGroupUi(instance.component)
        }
    }
}