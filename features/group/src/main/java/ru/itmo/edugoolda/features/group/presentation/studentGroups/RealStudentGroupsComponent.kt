package ru.itmo.edugoolda.features.group.presentation.studentGroups

import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.strResDesc
import kotlinx.coroutines.flow.MutableStateFlow
import me.aartikov.replica.algebra.paged.withKey
import ru.itmo.edugoolda.core.dialog.standard.DialogButton
import ru.itmo.edugoolda.core.dialog.standard.StandardDialogData
import ru.itmo.edugoolda.core.dialog.standard.standardDialogControl
import ru.itmo.edugoolda.core.error_handling.ErrorHandler
import ru.itmo.edugoolda.core.error_handling.safeLaunch
import ru.itmo.edugoolda.core.message.data.MessageService
import ru.itmo.edugoolda.core.message.domain.Message
import ru.itmo.edugoolda.core.utils.componentScope
import ru.itmo.edugoolda.core.utils.observe
import ru.itmo.edugoolda.core.utils.withProgress
import ru.itmo.edugoolda.data.group.group_invitation_data.api.GroupInvitationCode
import ru.itmo.edugoolda.data.group.group_invitation_data.api.GroupInvitationDataRepository
import ru.itmo.edugoolda.data.group.group_list.api.GroupId
import ru.itmo.edugoolda.data.group.group_list.api.GroupListRepository
import ru.itmo.edugoolda.features.group.R
import ru.mobileup.kmm_form_validation.control.InputControl

class RealStudentGroupsComponent(
    componentContext: ComponentContext,
    private val communication: StudentGroupsComponent.Communication,
    private val errorHandler: ErrorHandler,
    private val groupListRepository: GroupListRepository,
    private val groupInvitationDataRepository: GroupInvitationDataRepository,
    private val messageService: MessageService,
) : StudentGroupsComponent, ComponentContext by componentContext {
    override val groupSearchInputControl = InputControl(componentScope)
    override val groupEnteringCodeInputControl = InputControl(componentScope)
    private val studentGroupReplica =
        groupListRepository.groupInfoListReplica.withKey(groupSearchInputControl.text)
    override val studentGroupState = studentGroupReplica.observe(this, errorHandler)
    override val dialogAddGroup = standardDialogControl("add group")
    override val isAddingProgress = MutableStateFlow(false)

    override fun onRefresh() {
        studentGroupReplica.refresh()
    }

    override fun onRetryClick() {
        studentGroupReplica.revalidate()
    }

    override fun onLoadNext() {
        studentGroupReplica.loadNext()
    }

    override fun onGroupDetailsRequestClick(id: GroupId) {
        communication.onGroupDetailsRequested(id)
    }

    private fun onGroupAddRequestClick() {
        componentScope.safeLaunch(
            errorHandler,
            showError = false,
            onErrorHandled = { showCodeInputControlError() }
        ) {
            withProgress(isAddingProgress) {
                groupInvitationDataRepository.sendRequestJoinGroup(
                    GroupInvitationCode(
                        groupEnteringCodeInputControl.text.value
                    )
                )
                dialogAddGroup.dismiss()
                messageService.showMessage(
                    Message(text = R.string.message_service_valid_code_sent.strResDesc())
                )
            }
        }
    }

    private fun showCodeInputControlError() {
        groupEnteringCodeInputControl.error.value =
            R.string.message_service_invalid_code.strResDesc()
    }

    override fun onDialogAddGroup() {
        groupEnteringCodeInputControl.setText("")
        groupEnteringCodeInputControl.error.value = null
        dialogAddGroup.show(
            StandardDialogData(
                title = R.string.dialog_add_group_title.strResDesc(),
                message = R.string.dialog_add_group_message.strResDesc(),
                confirmButton = DialogButton(
                    text = R.string.dialog_add_group_confirm.strResDesc(),
                    action = {
                        onGroupAddRequestClick()
                    }
                ),
                dismissButton = DialogButton(
                    text = R.string.dialog_add_group_dismiss.strResDesc(),
                    action = {
                        dialogAddGroup.dismiss()
                    }
                ),
                dismissableByUser = true
            )
        )
    }
}