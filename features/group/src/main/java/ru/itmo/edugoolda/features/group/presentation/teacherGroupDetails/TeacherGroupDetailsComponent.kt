package ru.itmo.edugoolda.features.group.presentation.teacherGroupDetails

import kotlinx.coroutines.flow.StateFlow
import ru.itmo.edugoolda.core.dialog.standard.StandardDialogControl
import ru.itmo.edugoolda.core.utils.LoadableState
import ru.itmo.edugoolda.core.utils.PagedState
import ru.itmo.edugoolda.data.group.group_info.api.GroupFullInfo
import ru.itmo.edugoolda.data.group.group_invitation_data.api.GroupInvitationData
import ru.itmo.edugoolda.data.group.group_students_list.api.GroupStudentsList
import ru.itmo.edugoolda.data.group.group_students_list.api.KickType
import ru.itmo.edugoolda.data.user.api.UserId

interface TeacherGroupDetailsComponent {
    val groupOfStudentsState: StateFlow<PagedState<GroupStudentsList>>
    val groupInvitationDataState: StateFlow<GroupInvitationData?>
    val groupInfoState: StateFlow<LoadableState<GroupFullInfo>>
    val isGettingCodeProgress: StateFlow<Boolean>
    val isKickingMemberProgress: StateFlow<Boolean>
    val isDeletingGroupProgress: StateFlow<Boolean>
    val dialogDeleteGroup: StandardDialogControl
    val dialogKickMember: StandardDialogControl

    fun onRefresh()
    fun onRetryClick()
    fun onLoadNext()
    fun onReturnBackRequestClick()
    fun onGroupCodeGenerateRequestClick(onCodeGenerated: (String) -> Unit)
    fun onDialogDeleteGroup()
    fun onDialogKickMember(action: KickType, userId: UserId)
    fun onShowMessageCodeCopied(code: String)

    interface Communication {
        fun onReturnBackRequested()
        fun onGroupDeleted()
    }
}