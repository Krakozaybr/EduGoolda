package ru.itmo.edugoolda.features.group.presentation.teacherGroupDetails

import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.strResDesc
import kotlinx.coroutines.flow.MutableStateFlow
import me.aartikov.replica.algebra.normal.withKey
import me.aartikov.replica.algebra.paged.withKey
import ru.itmo.edugoolda.core.dialog.standard.DialogButton
import ru.itmo.edugoolda.core.dialog.standard.StandardDialogData
import ru.itmo.edugoolda.core.dialog.standard.standardDialogControl
import ru.itmo.edugoolda.core.error_handling.ErrorHandler
import ru.itmo.edugoolda.core.error_handling.safeLaunch
import ru.itmo.edugoolda.core.message.data.MessageService
import ru.itmo.edugoolda.core.message.domain.Message
import ru.itmo.edugoolda.core.utils.ResourceFormatted
import ru.itmo.edugoolda.core.utils.componentScope
import ru.itmo.edugoolda.core.utils.observe
import ru.itmo.edugoolda.core.utils.withProgress
import ru.itmo.edugoolda.data.group.group_info.api.GroupFullInfoRepository
import ru.itmo.edugoolda.data.group.group_invitation_data.api.GroupInvitationData
import ru.itmo.edugoolda.data.group.group_invitation_data.api.GroupInvitationDataRepository
import ru.itmo.edugoolda.data.group.group_list.api.GroupId
import ru.itmo.edugoolda.data.group.group_list.api.GroupListRepository
import ru.itmo.edugoolda.data.group.group_students_list.api.GroupStudentsRepository
import ru.itmo.edugoolda.data.group.group_students_list.api.KickType
import ru.itmo.edugoolda.data.user.api.UserId
import ru.itmo.edugoolda.features.group.R

class RealTeacherGroupDetailsComponent(
    private val groupId: GroupId,
    componentContext: ComponentContext,
    private val communication: TeacherGroupDetailsComponent.Communication,
    private val errorHandler: ErrorHandler,
    private val repositoryGroupOfStudents: GroupStudentsRepository,
    private val repositoryGroupFullInfo: GroupFullInfoRepository,
    private val repositoryGroupInvitationData: GroupInvitationDataRepository,
    private val repositoryGroupList: GroupListRepository,
    private val messageService: MessageService
) : TeacherGroupDetailsComponent, ComponentContext by componentContext {
    private val groupOfStudentsReplica =
        repositoryGroupOfStudents.groupOfStudentsReplica.withKey(groupId)
    override val groupOfStudentsState = groupOfStudentsReplica.observe(this, errorHandler)

    private val groupInfoReplica =
        repositoryGroupFullInfo.groupInfoReplica.withKey(groupId)
    override val groupInfoState = groupInfoReplica.observe(this, errorHandler)

    override val groupInvitationDataState: MutableStateFlow<GroupInvitationData?> = MutableStateFlow(null)
    override val isGettingCodeProgress = MutableStateFlow(false)
    override val isKickingMemberProgress = MutableStateFlow(false)
    override val isDeletingGroupProgress = MutableStateFlow(false)
    override val dialogDeleteGroup = standardDialogControl("delete group")
    override val dialogKickMember = standardDialogControl("kick member")

    override fun onRefresh() {
        groupOfStudentsReplica.refresh()
        groupInfoReplica.refresh()
    }

    override fun onRetryClick() {
        groupOfStudentsReplica.revalidate()
        groupInfoReplica.revalidate()
    }

    override fun onLoadNext() {
        groupOfStudentsReplica.loadNext()
    }

    override fun onReturnBackRequestClick() {
        communication.onReturnBackRequested()
    }

    private fun onGroupDeleteRequestClick() {
        if (isDeletingGroupProgress.value) return

        componentScope.safeLaunch(errorHandler) {
            withProgress(isDeletingGroupProgress) {
                repositoryGroupList.deleteGroup(groupId)
            }
            communication.onGroupDeleted()
        }
    }

    private fun onGroupMemberKickRequestClick(action: KickType, userId: UserId) {
        if (isKickingMemberProgress.value) return

        componentScope.safeLaunch(errorHandler) {
            withProgress(isKickingMemberProgress) {
                repositoryGroupOfStudents.kickStudentFromGroup(action, groupId, userId)
            }
        }
    }

    override fun onGroupCodeGenerateRequestClick(onCodeGenerated: (String) -> Unit) {
        if (isGettingCodeProgress.value) return

        componentScope.safeLaunch(errorHandler) {
            withProgress(isGettingCodeProgress) {
                val groupInvitationData =
                    repositoryGroupInvitationData.getGroupInvitationData(groupId)
                groupInvitationDataState.value = groupInvitationData
                onCodeGenerated(groupInvitationData.code.value)
            }
        }
    }

    override fun onDialogDeleteGroup() {
        val data = groupInfoState.value.data ?: return
        dialogDeleteGroup.show(
            StandardDialogData(
                title = R.string.delete_group.strResDesc(),
                message = StringDesc.ResourceFormatted(
                    R.string.delete_group_message,
                    data.name
                ),
                confirmButton = DialogButton(
                    text = R.string.group_confirm.strResDesc(),
                    action = {
                        onGroupDeleteRequestClick()
                        dialogDeleteGroup.dismiss()
                    }
                ),
                dismissButton = DialogButton(
                    text = R.string.group_dismiss.strResDesc(),
                    action = {
                        dialogDeleteGroup.dismiss()
                    }
                ),
                dismissableByUser = true
            )
        )
    }

    override fun onDialogKickMember(action: KickType, userId: UserId) {
        val data = groupInfoState.value.data ?: return
        dialogKickMember.show(
            StandardDialogData(
                title = R.string.delete_participant.strResDesc(),
                message = StringDesc.ResourceFormatted(
                    R.string.delete_participant_message_secondary,
                    data.name
                ),
                confirmButton = DialogButton(
                    text = R.string.group_confirm.strResDesc(),
                    action = {
                        onGroupMemberKickRequestClick(action, userId)
                        dialogDeleteGroup.dismiss()
                    }
                ),
                dismissButton = DialogButton(
                    text = R.string.group_dismiss.strResDesc(),
                    action = {
                        dialogDeleteGroup.dismiss()
                    }
                ),
                dismissableByUser = true
            )
        )
    }

    override fun onShowMessageCodeCopied(code: String) {
        messageService.showMessage(
            Message(text = StringDesc.ResourceFormatted(
                R.string.group_message_code_copied,
                code
            ))
        )
    }
}