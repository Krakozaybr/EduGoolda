package ru.itmo.edugoolda.features.lesson

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import ru.itmo.edugoolda.core.ComponentFactory
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonId
import ru.itmo.edugoolda.features.lesson.presentation.createLesson.CreateLessonComponent
import ru.itmo.edugoolda.features.lesson.presentation.createLesson.RealCreateLessonComponent
import ru.itmo.edugoolda.data.solutions.api.SolutionId
import ru.itmo.edugoolda.features.lesson.presentation.createLesson.groupsListForLessonCreating.AddingGroupComponent
import ru.itmo.edugoolda.features.lesson.presentation.createLesson.groupsListForLessonCreating.RealAddingGroupComponent
import ru.itmo.edugoolda.features.lesson.presentation.studentLessonDetails.RealStudentLessonDetailsComponent
import ru.itmo.edugoolda.features.lesson.presentation.studentLessonDetails.StudentLessonDetailsComponent
import ru.itmo.edugoolda.features.lesson.presentation.studentLessonList.LessonInfoListStudentComponent
import ru.itmo.edugoolda.features.lesson.presentation.studentLessonList.LessonInfoListStudentComponentImpl
import ru.itmo.edugoolda.features.lesson.presentation.teacherLessonList.LessonInfoListTeacherComponent
import ru.itmo.edugoolda.features.lesson.presentation.teacherLessonList.LessonInfoListTeacherComponentImpl
import ru.itmo.edugoolda.features.lesson.presentation.teacherLessonDetails.RealTeacherLessonDetailsComponent
import ru.itmo.edugoolda.features.lesson.presentation.teacherLessonDetails.TeacherLessonDetailsComponent
import ru.itmo.edugoolda.features.lesson.presentation.teacherSolutionDetails.RealTeacherSolutionDetailsComponent
import ru.itmo.edugoolda.features.lesson.presentation.teacherSolutionDetails.TeacherSolutionDetailsComponent

fun ComponentFactory.createLessonsComponent(
    componentContext: ComponentContext,
    initialConfiguration: LessonsComponent.InitialConfiguration,
    communication: LessonsComponent.Communication,
): LessonsComponent {
    return RealLessonsComponent(
        componentContext,
        initialConfiguration,
        communication,
        get(),
        get(),
    )
}

fun ComponentFactory.createStudentLessonDetailsComponent(
    componentContext: ComponentContext,
    lessonId: LessonId,
    communication: StudentLessonDetailsComponent.Communication,
): RealStudentLessonDetailsComponent {
    return RealStudentLessonDetailsComponent(
        lessonId,
        componentContext,
        communication,
        get(),
        get()
    )
}

fun ComponentFactory.createTeacherSolutionDetailsComponent(
    componentContext: ComponentContext,
    solutionId: SolutionId,
    communication: TeacherSolutionDetailsComponent.Communication,
): RealTeacherSolutionDetailsComponent {
    return RealTeacherSolutionDetailsComponent(
        solutionId,
        componentContext,
        communication,
        get(),
        get(),
        get()
    )
}

fun ComponentFactory.createTeacherLessonListInfoComponent(
    componentContext: ComponentContext,
    communication: LessonInfoListTeacherComponent.Communication,
): LessonInfoListTeacherComponent {
    return LessonInfoListTeacherComponentImpl(componentContext, communication, get(), get())
}

fun ComponentFactory.createTeacherLessonDetailsComponent(
    componentContext: ComponentContext,
    lessonId: LessonId,
    communication: TeacherLessonDetailsComponent.Communication,
): RealTeacherLessonDetailsComponent {
    return RealTeacherLessonDetailsComponent(
        lessonId,
        componentContext,
        communication,
        get(),
        get(),
        get()
    )
}

fun ComponentFactory.createLessonCreateComponent(
    componentContext: ComponentContext,
    communication: CreateLessonComponent.Communication,
): RealCreateLessonComponent {
    return RealCreateLessonComponent(componentContext, communication, get(), get())
}

fun ComponentFactory.createLessonInfoListStudentComponent(
    componentContext: ComponentContext,
    communication: LessonInfoListStudentComponent.Communication,
): LessonInfoListStudentComponent {
    return LessonInfoListStudentComponentImpl(
        componentContext,
        communication,
        get(),
        get(),
    )
}

fun ComponentFactory.createAddingGroupComponent(
    componentContext: ComponentContext,
    communication: AddingGroupComponent.Communication,
): RealAddingGroupComponent {
    return RealAddingGroupComponent(componentContext, communication, get(), get())
}