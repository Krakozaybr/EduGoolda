package ru.itmo.edugoolda.features.group.presentation.studentGroups

import kotlinx.coroutines.flow.StateFlow
import ru.itmo.edugoolda.core.dialog.standard.StandardDialogControl
import ru.itmo.edugoolda.core.utils.PagedState
import ru.itmo.edugoolda.data.group.group_list.api.GroupId
import ru.itmo.edugoolda.data.group.group_list.api.GroupInfoList
import ru.mobileup.kmm_form_validation.control.InputControl

interface StudentGroupsComponent {
    val groupSearchInputControl: InputControl
    val groupEnteringCodeInputControl: InputControl
    val studentGroupState: StateFlow<PagedState<GroupInfoList>>
    val dialogAddGroup: StandardDialogControl
    val isAddingProgress: StateFlow<Boolean>

    fun onRefresh()
    fun onRetryClick()
    fun onLoadNext()
    fun onGroupDetailsRequestClick(id: GroupId)
    fun onDialogAddGroup()

    interface Communication {
        fun onGroupDetailsRequested(id: GroupId)
        fun onGroupAddRequested()
    }
}