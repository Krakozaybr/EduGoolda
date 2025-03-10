package ru.itmo.edugoolda.features.group.presentation.create

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.itmo.edugoolda.core.error_handling.ErrorHandler
import ru.itmo.edugoolda.core.error_handling.safeLaunch
import ru.itmo.edugoolda.core.utils.componentScope
import ru.itmo.edugoolda.core.utils.computed
import ru.itmo.edugoolda.core.utils.withProgress
import ru.itmo.edugoolda.data.group.GroupRepository
import ru.mobileup.kmm_form_validation.control.InputControl

class RealGroupCreateComponent(
    componentContext: ComponentContext,
    private val communication: GroupCreateComponent.Communication,
    private val errorHandler: ErrorHandler,
    private val groupRepository: GroupRepository,
) : GroupCreateComponent, ComponentContext by componentContext {
    override val nameInputControl = InputControl(componentScope)
    override val descriptionInputControl = InputControl(componentScope)
    override val subjectsList = MutableStateFlow(listOf("1","2","3"))
    override val isCreationProgress = MutableStateFlow(false)
    override val isCreationButtonEnabled = computed(nameInputControl.text, String::isNotBlank)
    override val selectedSubject = MutableStateFlow(subjectsList.value[0])


    override fun onCreateClick() {
        if (isCreationProgress.value) return

        componentScope.safeLaunch(errorHandler) {
            withProgress(isCreationProgress) {
                val id = groupRepository.createGroup(
                    selectedSubject = selectedSubject.value,
                    name = nameInputControl.text.value,
                    description = descriptionInputControl.text.value
                )
                communication.onGroupCreated(id)
            }
        }
    }

    override fun onCancelClick() {
        communication.onCancel()
    }

    override fun onSubjectSelect(subject: String) {
        selectedSubject.value = subject
    }
}