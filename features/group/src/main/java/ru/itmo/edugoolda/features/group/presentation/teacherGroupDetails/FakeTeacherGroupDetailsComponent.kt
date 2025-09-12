package ru.itmo.edugoolda.features.group.presentation.teacherGroupDetails

import kotlinx.coroutines.flow.MutableStateFlow
import ru.itmo.edugoolda.core.dialog.standard.fakeStandardDialogControl
import ru.itmo.edugoolda.core.utils.LoadableState
import ru.itmo.edugoolda.core.utils.PagedState
import ru.itmo.edugoolda.data.group.group_info.api.GroupFullInfo
import ru.itmo.edugoolda.data.group.group_invitation_data.api.GroupInvitationData
import ru.itmo.edugoolda.data.group.group_students_list.api.GroupStudentsList
import ru.itmo.edugoolda.data.group.group_students_list.api.KickType
import ru.itmo.edugoolda.data.user.api.UserId

class FakeTeacherGroupDetailsComponent() : TeacherGroupDetailsComponent {
    override val groupOfStudentsState = MutableStateFlow(PagedState(data = GroupStudentsList.MOCK))
    override val groupInvitationDataState = MutableStateFlow(GroupInvitationData.MOCK)
    override val groupInfoState = MutableStateFlow(LoadableState(data = GroupFullInfo.MOCK))
    override val isGettingCodeProgress = MutableStateFlow(false)
    override val isKickingMemberProgress = MutableStateFlow(false)
    override val isDeletingGroupProgress = MutableStateFlow(false)
    override val dialogDeleteGroup = fakeStandardDialogControl()
    override val dialogKickMember = fakeStandardDialogControl()

    override fun onRefresh() = Unit
    override fun onRetryClick() = Unit
    override fun onLoadNext() = Unit
    override fun onReturnBackRequestClick() = Unit
    override fun onGroupCodeGenerateRequestClick(onCodeGenerated: (String) -> Unit) = Unit
    override fun onDialogDeleteGroup() = Unit
    override fun onDialogKickMember(action: KickType, userId: UserId) = Unit
    override fun onShowMessageCodeCopied(code: String) = Unit
}