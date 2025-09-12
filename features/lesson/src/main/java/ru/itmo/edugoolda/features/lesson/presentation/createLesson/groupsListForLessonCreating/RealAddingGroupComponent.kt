package ru.itmo.edugoolda.features.lesson.presentation.createLesson.groupsListForLessonCreating

import com.arkivanov.decompose.ComponentContext
import me.aartikov.replica.algebra.paged.withKey
import ru.itmo.edugoolda.core.error_handling.ErrorHandler
import ru.itmo.edugoolda.core.utils.componentScope
import ru.itmo.edugoolda.core.utils.observe
import ru.itmo.edugoolda.data.group.group_info.api.GroupInfo
import ru.itmo.edugoolda.data.group.group_list.api.GroupListRepository
import ru.mobileup.kmm_form_validation.control.InputControl

class RealAddingGroupComponent(
    componentContext: ComponentContext,
    private val communication: AddingGroupComponent.Communication,
    private val errorHandler: ErrorHandler,
    private val groupListRepository: GroupListRepository,
) : AddingGroupComponent, ComponentContext by componentContext {
    override val groupSearchInputControl = InputControl(componentScope)
    private val teacherGroupReplica = groupListRepository.groupInfoListReplica.withKey(groupSearchInputControl.text)
    override val teacherGroupState = teacherGroupReplica.observe(this, errorHandler)

    override fun onRefresh() {
        teacherGroupReplica.refresh()
    }

    override fun onRetryClick() {
        teacherGroupReplica.revalidate()
    }

    override fun onLoadNext() {
        teacherGroupReplica.loadNext()
    }

    override fun onGroupAddRequestClick(groupInfo: GroupInfo) {
        communication.onGroupAddedRequested(groupInfo)
    }

    override fun onCancelGroupAddingForLessonRequestClick() {
        communication.onCancelGroupAddingForLessonRequested()
    }
}