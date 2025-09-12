package ru.itmo.edugoolda.features.lesson.presentation.createLesson

import kotlinx.coroutines.flow.StateFlow
import ru.itmo.edugoolda.data.group.group_info.api.GroupInfo
import ru.itmo.edugoolda.data.group.group_list.api.GroupId
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonId
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonType
import ru.mobileup.kmm_form_validation.control.InputControl

interface CreateLessonComponent {
    val lessonNameInputControl: InputControl
    val descriptionInputControl: InputControl
    val selectedLessonType: StateFlow<LessonType>
    val isCreatingLessonProgress: StateFlow<Boolean>
    val isCreateLessonButtonEnabled: StateFlow<Boolean>
    val groupListState: StateFlow<List<GroupInfo>>

    fun onCreateLesson()
    fun onReturnBackClick()
    fun onLessonTypeSelect(newType: LessonType)
    fun onGroupAdded(groupInfo: GroupInfo)
    fun onAddGroupButtonClick()
    fun onGroupDeleteClick(groupId: GroupId)

    interface Communication {
        fun onCancelLessonCreation()
        fun onLessonCreated(lessonId: LessonId)
        fun onGroupAddRequested()
    }
}