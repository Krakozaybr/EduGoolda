package ru.itmo.edugoolda.features.lesson.presentation.createLesson.groupsListForLessonCreating

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import ru.itmo.edugoolda.core.utils.PagedState
import ru.itmo.edugoolda.data.group.group_info.api.GroupInfo
import ru.itmo.edugoolda.data.group.group_list.api.GroupInfoList
import ru.mobileup.kmm_form_validation.control.InputControl

class FakeAddingGroupComponent : AddingGroupComponent {
    override val groupSearchInputControl = InputControl(GlobalScope)
    override val teacherGroupState = MutableStateFlow(PagedState(data = GroupInfoList.MOCK))

    override fun onRefresh() = Unit

    override fun onRetryClick() = Unit

    override fun onLoadNext() = Unit

    override fun onGroupAddRequestClick(groupInfo: GroupInfo) = Unit

    override fun onCancelGroupAddingForLessonRequestClick() = Unit
}