package ru.itmo.edugoolda.features.group.presentation.addGroup

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import ru.itmo.edugoolda.core.error_handling.ErrorHandler
import ru.itmo.edugoolda.core.error_handling.safeLaunch
import ru.itmo.edugoolda.core.utils.componentScope
import ru.itmo.edugoolda.core.utils.computed
import ru.itmo.edugoolda.core.utils.withProgress
import ru.itmo.edugoolda.data.group.group_invitation_data.api.GroupInvitationCode
import ru.itmo.edugoolda.data.group.group_invitation_data.api.GroupInvitationDataRepository
import ru.mobileup.kmm_form_validation.control.InputControl

class RealGroupAddComponent(
    componentContext: ComponentContext,
    private val communication: GroupAddComponent.Communication,
    private val errorHandler: ErrorHandler,
    private val groupInvitationDataRepository: GroupInvitationDataRepository,
) : GroupAddComponent, ComponentContext by componentContext {
    override val codeInputControl = InputControl(componentScope)
    override val isAddingProgress = MutableStateFlow(false)
    override val isAddButtonEnabled = computed(codeInputControl.text, String::isNotBlank)

    override fun onAddClick() {
        if (isAddingProgress.value) return

        componentScope.safeLaunch(errorHandler) {
            withProgress(isAddingProgress) {
                groupInvitationDataRepository.sendRequestJoinGroup(GroupInvitationCode(codeInputControl.text.value))
            }
            communication.onGroupAdded()
        }
    }

    override fun onCancelClick() {
        communication.onCancelGroupAdding()
    }
}