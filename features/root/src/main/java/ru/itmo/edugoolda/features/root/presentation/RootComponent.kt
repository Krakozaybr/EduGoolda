package ru.itmo.edugoolda.features.root.presentation

import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow
import ru.itmo.edugoolda.core.message.presentation.MessageComponent
import ru.itmo.edugoolda.features.auth.presentation.auth.AuthComponent
import ru.itmo.edugoolda.features.group.presentation.GroupComponent
import ru.itmo.edugoolda.features.join_requests.presentation.student.JoinRequestsStudentComponent
import ru.itmo.edugoolda.features.join_requests.presentation.teacher.JoinRequestsTeacherComponent
import ru.itmo.edugoolda.features.lesson.LessonsComponent
import ru.itmo.edugoolda.features.main.presentation.student.MainStudentComponent
import ru.itmo.edugoolda.features.main.presentation.teacher.MainTeacherComponent
import ru.itmo.edugoolda.features.root.presentation.start.StartComponent

/**
 * A root of a Decompose component tree.
 *
 * Note: Try to minimize child count in RootComponent. It should operate by flows of screens rather than separate screens.
 */
interface RootComponent {

    val childStack: StateFlow<ChildStack<*, Child>>

    val messageComponent: MessageComponent

    sealed interface Child {
        class Auth(val instance: AuthComponent) : Child
        class StudentMain(val instance: MainStudentComponent) : Child
        class TeacherMain(val instance: MainTeacherComponent) : Child
        class Group(val instance: GroupComponent) : Child
        class JoinRequestsTeacher(val instance: JoinRequestsTeacherComponent) : Child
        class JoinRequestsStudent(val instance: JoinRequestsStudentComponent) : Child
        class Lessons(val instance: LessonsComponent) : Child
        class Start(val instance: StartComponent) : Child
    }
}
