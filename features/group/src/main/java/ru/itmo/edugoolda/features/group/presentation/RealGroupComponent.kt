package ru.itmo.edugoolda.features.group.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.replaceCurrent
import kotlinx.serialization.Serializable
import ru.itmo.edugoolda.core.ComponentFactory
import ru.itmo.edugoolda.core.utils.safePush
import ru.itmo.edugoolda.core.utils.toStateFlow
import ru.itmo.edugoolda.data.group.group_list.api.GroupId
import ru.itmo.edugoolda.features.group.createGroupAddComponent
import ru.itmo.edugoolda.features.group.createGroupCreateComponent
import ru.itmo.edugoolda.features.group.createStudentGroupDetailsComponent
import ru.itmo.edugoolda.features.group.createStudentGroupsComponent
import ru.itmo.edugoolda.features.group.createTeacherGroupDetailsComponent
import ru.itmo.edugoolda.features.group.createTeacherGroupsComponent
import ru.itmo.edugoolda.features.group.presentation.GroupComponent.InitialConfiguration
import ru.itmo.edugoolda.features.group.presentation.addGroup.GroupAddComponent
import ru.itmo.edugoolda.features.group.presentation.createGroup.GroupCreateComponent
import ru.itmo.edugoolda.features.group.presentation.studentGroupDetails.StudentGroupDetailsComponent
import ru.itmo.edugoolda.features.group.presentation.studentGroups.StudentGroupsComponent
import ru.itmo.edugoolda.features.group.presentation.teacherGroupDetails.TeacherGroupDetailsComponent
import ru.itmo.edugoolda.features.group.presentation.teacherGroups.TeacherGroupsComponent

class RealGroupComponent(
    componentContext: ComponentContext,
    initialConfiguration: InitialConfiguration,
    private val communication: GroupComponent.Communication,
    private val componentFactory: ComponentFactory,
) : GroupComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    override val childStack = childStack(
        source = navigation,
        initialConfiguration = when (initialConfiguration) {
            InitialConfiguration.AddGroup -> Config.AddGroup
            InitialConfiguration.CreateGroup -> Config.CreateGroup
            is InitialConfiguration.StudentGroupDetails -> Config.StudentGroupDetails(
                initialConfiguration.groupId
            )

            InitialConfiguration.StudentGroupsList -> Config.StudentGroupsList
            is InitialConfiguration.TeacherGroupDetails -> Config.TeacherGroupDetails(
                initialConfiguration.groupId
            )

            InitialConfiguration.TeacherGroupsList -> Config.TeacherGroupsList
        },
        serializer = Config.serializer(),
        handleBackButton = true,
        childFactory = ::createChild
    ).toStateFlow(lifecycle)

    private inner class TeacherCommunicationResolver :
        GroupCreateComponent.Communication,
        TeacherGroupsComponent.Communication,
        TeacherGroupDetailsComponent.Communication {
        override fun onGroupDetailsRequested(id: GroupId) {
            navigation.safePush(Config.TeacherGroupDetails(id))
        }

        override fun onGroupCreated(id: GroupId) {
            navigation.replaceCurrent(Config.TeacherGroupDetails(id))
        }

        override fun onCancelGroupCreation() = goBack()
        override fun onReturnBackRequested() = goBack()
        override fun onGroupDeleted() = goBack()
    }

    private inner class StudentCommunicationResolver :
        StudentGroupsComponent.Communication,
        StudentGroupDetailsComponent.Communication,
        GroupAddComponent.Communication {
        override fun onGroupDetailsRequested(id: GroupId) {
            navigation.safePush(Config.StudentGroupDetails(id))
        }

        override fun onGroupAddRequested() {
            navigation.safePush(Config.AddGroup)
        }

        override fun onGroupAdded() = goBack()
        override fun onReturnBackRequested() = goBack()
        override fun onGroupQuited() = goBack()
        override fun onCancelGroupAdding() = goBack()
    }

    private fun goBack() = when {
        childStack.value.items.size == 1 -> communication.onCancel()
        else -> navigation.pop()
    }

    private fun createChild(
        config: Config,
        componentContext: ComponentContext,
    ): GroupComponent.Child = when (config) {
        Config.CreateGroup -> GroupComponent.Child.CreateGroup(
            componentFactory.createGroupCreateComponent(
                componentContext,
                TeacherCommunicationResolver()
            )
        )

        is Config.TeacherGroupDetails -> GroupComponent.Child.TeacherGroupDetails(
            componentFactory.createTeacherGroupDetailsComponent(
                groupId = config.id,
                componentContext,
                TeacherCommunicationResolver()
            )
        )

        Config.TeacherGroupsList -> GroupComponent.Child.TeacherGroupsList(
            componentFactory.createTeacherGroupsComponent(
                componentContext,
                TeacherCommunicationResolver()
            )
        )

        Config.AddGroup -> GroupComponent.Child.AddGroup(
            componentFactory.createGroupAddComponent(
                componentContext,
                StudentCommunicationResolver()
            )
        )

        is Config.StudentGroupDetails -> GroupComponent.Child.StudentGroupDetails(
            componentFactory.createStudentGroupDetailsComponent(
                groupId = config.id,
                componentContext,
                StudentCommunicationResolver()
            )
        )

        Config.StudentGroupsList -> GroupComponent.Child.StudentGroupsList(
            componentFactory.createStudentGroupsComponent(
                componentContext,
                StudentCommunicationResolver()
            )
        )
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object CreateGroup : Config

        @Serializable
        data class TeacherGroupDetails(val id: GroupId) : Config

        @Serializable
        data object TeacherGroupsList : Config

        @Serializable
        data object AddGroup : Config

        @Serializable
        data class StudentGroupDetails(val id: GroupId) : Config

        @Serializable
        data object StudentGroupsList : Config
    }
}