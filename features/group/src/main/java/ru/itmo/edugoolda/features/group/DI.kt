package ru.itmo.edugoolda.features.group

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import ru.itmo.edugoolda.core.ComponentFactory
import ru.itmo.edugoolda.data.group.group_list.api.GroupId
import ru.itmo.edugoolda.features.group.presentation.GroupComponent
import ru.itmo.edugoolda.features.group.presentation.RealGroupComponent
import ru.itmo.edugoolda.features.group.presentation.addGroup.GroupAddComponent
import ru.itmo.edugoolda.features.group.presentation.addGroup.RealGroupAddComponent
import ru.itmo.edugoolda.features.group.presentation.createGroup.GroupCreateComponent
import ru.itmo.edugoolda.features.group.presentation.createGroup.RealGroupCreateComponent
import ru.itmo.edugoolda.features.group.presentation.studentGroupDetails.RealStudentGroupDetailsComponent
import ru.itmo.edugoolda.features.group.presentation.studentGroupDetails.StudentGroupDetailsComponent
import ru.itmo.edugoolda.features.group.presentation.studentGroups.RealStudentGroupsComponent
import ru.itmo.edugoolda.features.group.presentation.studentGroups.StudentGroupsComponent
import ru.itmo.edugoolda.features.group.presentation.teacherGroupDetails.RealTeacherGroupDetailsComponent
import ru.itmo.edugoolda.features.group.presentation.teacherGroupDetails.TeacherGroupDetailsComponent
import ru.itmo.edugoolda.features.group.presentation.teacherGroups.RealTeacherGroupsComponent
import ru.itmo.edugoolda.features.group.presentation.teacherGroups.TeacherGroupsComponent

fun ComponentFactory.createGroupComponent(
    componentContext: ComponentContext,
    communication: GroupComponent.Communication,
    config: GroupComponent.InitialConfiguration
): GroupComponent {
    return RealGroupComponent(
        componentContext,
        config,
        communication,
        get()
    )
}

fun ComponentFactory.createGroupCreateComponent(
    componentContext: ComponentContext,
    communication: GroupCreateComponent.Communication,
): RealGroupCreateComponent {
    return RealGroupCreateComponent(componentContext, communication, get(), get())
}

fun ComponentFactory.createStudentGroupsComponent(
    componentContext: ComponentContext,
    communication: StudentGroupsComponent.Communication,
): RealStudentGroupsComponent {
    return RealStudentGroupsComponent(componentContext, communication, get(), get(), get(), get())
}

fun ComponentFactory.createTeacherGroupsComponent(
    componentContext: ComponentContext,
    communication: TeacherGroupsComponent.Communication,
): RealTeacherGroupsComponent {
    return RealTeacherGroupsComponent(componentContext, communication, get(), get())
}

fun ComponentFactory.createTeacherGroupDetailsComponent(
    groupId: GroupId,
    componentContext: ComponentContext,
    communication: TeacherGroupDetailsComponent.Communication,
    ): RealTeacherGroupDetailsComponent {
    return RealTeacherGroupDetailsComponent(groupId, componentContext, communication, get(), get(), get(), get(), get(), get())
}

fun ComponentFactory.createGroupAddComponent(
    componentContext: ComponentContext,
    communication: GroupAddComponent.Communication,
): RealGroupAddComponent {
    return RealGroupAddComponent(componentContext, communication, get(), get())
}

fun ComponentFactory.createStudentGroupDetailsComponent(
    groupId: GroupId,
    componentContext: ComponentContext,
    communication: StudentGroupDetailsComponent.Communication,
): RealStudentGroupDetailsComponent {
    return RealStudentGroupDetailsComponent(groupId, componentContext, communication, get(), get(), get())
}