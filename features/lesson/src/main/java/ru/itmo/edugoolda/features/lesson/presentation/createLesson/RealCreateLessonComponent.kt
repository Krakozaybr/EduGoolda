package ru.itmo.edugoolda.features.lesson.presentation.createLesson

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import ru.itmo.edugoolda.core.error_handling.ErrorHandler
import ru.itmo.edugoolda.core.error_handling.safeLaunch
import ru.itmo.edugoolda.core.utils.componentScope
import ru.itmo.edugoolda.core.utils.computed
import ru.itmo.edugoolda.core.utils.withProgress
import ru.itmo.edugoolda.data.group.group_info.api.GroupInfo
import ru.itmo.edugoolda.data.group.group_list.api.GroupId
import ru.itmo.edugoolda.data.lesson.lesson_create.api.LessonCreateRepository
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonType
import ru.mobileup.kmm_form_validation.control.InputControl

class RealCreateLessonComponent(
    componentContext: ComponentContext,
    private val communication: CreateLessonComponent.Communication,
    private val errorHandler: ErrorHandler,
    private val lessonCreateRepository: LessonCreateRepository,
) : CreateLessonComponent, ComponentContext by componentContext {
    override val lessonNameInputControl = InputControl(componentScope)
    override val descriptionInputControl = InputControl(componentScope)
    override val selectedLessonType = MutableStateFlow(LessonType.Informational)
    override val isCreatingLessonProgress = MutableStateFlow(false)
    override val groupListState = MutableStateFlow(listOf<GroupInfo>())
    override val isCreateLessonButtonEnabled =
        computed(
            lessonNameInputControl.text,
            descriptionInputControl.text,
            groupListState
        ) { text, description, groupList ->
            text.isNotBlank() && description.isNotBlank() && groupList.isNotEmpty()
        }

    override fun onCreateLesson() {
        if (isCreatingLessonProgress.value || !isCreateLessonButtonEnabled.value) return

        componentScope.safeLaunch(errorHandler) {
            withProgress(isCreatingLessonProgress) {
                val lessonFullDefault = lessonCreateRepository.createLesson(
                    name = lessonNameInputControl.text.value,
                    description = descriptionInputControl.text.value,
                    groupIdList = groupListState.value.map { it.id },
                    isEstimatable = when (selectedLessonType.value) {
                        LessonType.Informational -> false
                        LessonType.Practical -> true
                    },
                    deadline = null,
                    opensAt = null
                )
                communication.onLessonCreated(lessonFullDefault.id)
            }
        }
    }

    override fun onReturnBackClick() {
        communication.onCancelLessonCreation()
    }

    override fun onLessonTypeSelect(newType: LessonType) {
        selectedLessonType.value = newType
    }

    override fun onGroupAdded(groupInfo: GroupInfo) {
        if (!groupListState.value.any { it.id == groupInfo.id }) {
            groupListState.value += listOf(groupInfo)
        }
    }

    override fun onAddGroupButtonClick() {
        communication.onGroupAddRequested()
    }

    override fun onGroupDeleteClick(groupId: GroupId) {
        groupListState.value = groupListState.value.filter {
            it.id != groupId
        }
    }
}