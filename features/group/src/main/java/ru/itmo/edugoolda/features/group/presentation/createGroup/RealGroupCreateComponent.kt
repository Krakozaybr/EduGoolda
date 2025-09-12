package ru.itmo.edugoolda.features.group.presentation.createGroup

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import ru.itmo.edugoolda.core.error_handling.ErrorHandler
import ru.itmo.edugoolda.core.error_handling.safeLaunch
import ru.itmo.edugoolda.core.utils.componentScope
import ru.itmo.edugoolda.core.utils.computed
import ru.itmo.edugoolda.core.utils.withProgress
import ru.itmo.edugoolda.data.group.create_group.api.GroupCreateRepository
import ru.mobileup.kmm_form_validation.control.InputControl

class RealGroupCreateComponent(
    componentContext: ComponentContext,
    private val communication: GroupCreateComponent.Communication,
    private val errorHandler: ErrorHandler,
    private val groupCreateRepository: GroupCreateRepository,
) : GroupCreateComponent, ComponentContext by componentContext {
    override val nameInputControl = InputControl(componentScope)
    override val descriptionInputControl = InputControl(componentScope)
    override val subjectInputControl = InputControl(componentScope)
    override val isCreationProgress = MutableStateFlow(false)
    override val isCreationButtonEnabled =
        computed(nameInputControl.text, subjectInputControl.text, descriptionInputControl.text) { text, subject, description ->
            text.isNotBlank() && subject.isNotBlank() && description.isNotBlank()
        }

    override fun onCreateClick() {
        if (isCreationProgress.value) return

        componentScope.safeLaunch(errorHandler) {
            withProgress(isCreationProgress) {
                val subjectId =
                    groupCreateRepository.getSubjectIdByName(subjectInputControl.text.value)
                val groupId = groupCreateRepository.createGroup(
                    name = nameInputControl.text.value,
                    description = descriptionInputControl.text.value,
                    selectedSubject = subjectId
                ).id
                communication.onGroupCreated(groupId)
            }
        }
    }

    override fun onCancelClick() {
        communication.onCancelGroupCreation()
    }
}