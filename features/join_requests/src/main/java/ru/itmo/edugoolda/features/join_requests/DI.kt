package ru.itmo.edugoolda.features.join_requests

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import ru.itmo.edugoolda.core.ComponentFactory
import ru.itmo.edugoolda.features.join_requests.presentation.student.JoinRequestsStudentComponent
import ru.itmo.edugoolda.features.join_requests.presentation.student.JoinRequestsStudentComponentImpl
import ru.itmo.edugoolda.features.join_requests.presentation.teacher.JoinRequestsTeacherComponent
import ru.itmo.edugoolda.features.join_requests.presentation.teacher.JoinRequestsTeacherComponentImpl

fun ComponentFactory.createJoinRequestsTeacherComponent(
    componentContext: ComponentContext,
    communication: JoinRequestsTeacherComponent.Communication
): JoinRequestsTeacherComponent {
    return JoinRequestsTeacherComponentImpl(componentContext, communication, get(), get())
}

fun ComponentFactory.createJoinRequestsStudentComponent(
    componentContext: ComponentContext,
    communication: JoinRequestsStudentComponent.Communication
): JoinRequestsStudentComponent {
    return JoinRequestsStudentComponentImpl(componentContext, communication, get(), get())
}
