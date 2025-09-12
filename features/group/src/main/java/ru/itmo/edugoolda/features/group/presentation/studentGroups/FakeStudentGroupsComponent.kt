package ru.itmo.edugoolda.features.group.presentation.studentGroups

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import ru.itmo.edugoolda.core.dialog.standard.fakeStandardDialogControl
import ru.itmo.edugoolda.core.utils.PagedState
import ru.itmo.edugoolda.data.group.group_list.api.GroupId
import ru.itmo.edugoolda.data.group.group_list.api.GroupInfoList
import ru.mobileup.kmm_form_validation.control.InputControl

class FakeStudentGroupsComponent : StudentGroupsComponent {
    override val groupSearchInputControl = InputControl(GlobalScope)
    override val groupEnteringCodeInputControl = InputControl(GlobalScope)
    override val studentGroupState = MutableStateFlow(PagedState(data = GroupInfoList.MOCK))
    override val dialogAddGroup = fakeStandardDialogControl()
    override val isAddingProgress = MutableStateFlow(false)

    override fun onRefresh() = Unit

    override fun onRetryClick() = Unit

    override fun onLoadNext() = Unit

    override fun onGroupDetailsRequestClick(id: GroupId) = Unit

    override fun onDialogAddGroup() = Unit
}