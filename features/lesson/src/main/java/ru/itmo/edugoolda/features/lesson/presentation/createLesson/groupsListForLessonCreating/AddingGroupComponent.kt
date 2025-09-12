package ru.itmo.edugoolda.features.lesson.presentation.createLesson.groupsListForLessonCreating

import kotlinx.coroutines.flow.StateFlow
import ru.itmo.edugoolda.core.utils.PagedState
import ru.itmo.edugoolda.data.group.group_info.api.GroupInfo
import ru.itmo.edugoolda.data.group.group_list.api.GroupInfoList
import ru.mobileup.kmm_form_validation.control.InputControl

interface AddingGroupComponent {
    val groupSearchInputControl: InputControl
    val teacherGroupState: StateFlow<PagedState<GroupInfoList>>

    fun onRefresh()
    fun onRetryClick()
    fun onLoadNext()
    fun onGroupAddRequestClick(groupInfo: GroupInfo)
    fun onCancelGroupAddingForLessonRequestClick()

    interface Communication {
        fun onGroupAddedRequested(groupInfo: GroupInfo)
        fun onCancelGroupAddingForLessonRequested()
    }
}