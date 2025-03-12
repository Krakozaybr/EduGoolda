package ru.itmo.edugoolda.features.group.presentation.create

import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.kmm_form_validation.control.InputControl

interface GroupCreateComponent {
    val nameInputControl: InputControl
    val descriptionInputControl: InputControl
    val selectedSubject: StateFlow<String>
    val subjectsList: StateFlow<List<String>>
    val isCreationProgress: StateFlow<Boolean>
    val isCreationButtonEnabled: StateFlow<Boolean>

    fun onCreateClick()
    fun onCancelClick()
    fun onSubjectSelect(subject: String)

    interface Communication {
        fun onGroupCreated(id: String)
        fun onCancel()
    }
}