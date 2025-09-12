package ru.itmo.edugoolda.features.lesson.presentation.studentLessonDetails

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import me.aartikov.replica.algebra.normal.withKey
import ru.itmo.edugoolda.core.error_handling.ErrorHandler
import ru.itmo.edugoolda.core.error_handling.safeLaunch
import ru.itmo.edugoolda.core.utils.componentScope
import ru.itmo.edugoolda.core.utils.computed
import ru.itmo.edugoolda.core.utils.observe
import ru.itmo.edugoolda.core.utils.withProgress
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonDetailsRepository
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonId
import ru.mobileup.kmm_form_validation.control.InputControl

class RealStudentLessonDetailsComponent(
    private val lessonId: LessonId,
    componentContext: ComponentContext,
    private val communication: StudentLessonDetailsComponent.Communication,
    private val errorHandler: ErrorHandler,
    private val lessonDetailsRepository: LessonDetailsRepository,
) : StudentLessonDetailsComponent, ComponentContext by componentContext {
    private val lessonStudentDetailsReplica = lessonDetailsRepository.lessonStudentDetailsReplica.withKey(lessonId)
    override val lessonStudentDetailsState = lessonStudentDetailsReplica.observe(this, errorHandler)
    override val replyInputControl = InputControl(componentScope)
    override val isSendingMessageProgress = MutableStateFlow(false)
    override val isReplyButtonEnabled = computed(replyInputControl.text, String::isNotBlank)

    override fun onRefresh() {
        lessonStudentDetailsReplica.refresh()
    }

    override fun onRetryClick() {
        lessonStudentDetailsReplica.revalidate()
    }

    override fun onSendCommentClick() {
        if (isSendingMessageProgress.value) return

        componentScope.safeLaunch(errorHandler) {
            withProgress(isSendingMessageProgress) {
                lessonDetailsRepository.sendMessageByStudent(lessonId, replyInputControl.text.value)
                replyInputControl.setText("")
            }
        }
    }

    override fun onReturnBackClick() {
        communication.onReturnBackStudentLessonDetails()
    }
}