package ru.itmo.edugoolda.features.lesson.presentation.teacherSolutionDetails

import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.strResDesc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.aartikov.replica.algebra.normal.withKey
import ru.itmo.edugoolda.core.error_handling.ErrorHandler
import ru.itmo.edugoolda.core.error_handling.safeLaunch
import ru.itmo.edugoolda.core.message.data.MessageService
import ru.itmo.edugoolda.core.message.domain.Message
import ru.itmo.edugoolda.core.utils.LoadableState
import ru.itmo.edugoolda.core.utils.componentScope
import ru.itmo.edugoolda.core.utils.computed
import ru.itmo.edugoolda.core.utils.observe
import ru.itmo.edugoolda.core.utils.withProgress
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonDetailsRepository
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonStatus
import ru.itmo.edugoolda.data.lesson.lesson_details.api.SolutionDetails
import ru.itmo.edugoolda.data.solutions.api.SolutionId
import ru.itmo.edugoolda.features.lesson.R
import ru.mobileup.kmm_form_validation.control.InputControl

class RealTeacherSolutionDetailsComponent(
    private val solutionId: SolutionId,
    componentContext: ComponentContext,
    private val communication: TeacherSolutionDetailsComponent.Communication,
    private val errorHandler: ErrorHandler,
    private val lessonDetailsRepository: LessonDetailsRepository,
    private val messageService: MessageService
) : TeacherSolutionDetailsComponent, ComponentContext by componentContext {
    private val solutionTeacherDetailsReplica = lessonDetailsRepository.solutionTeacherDetailsReplica.withKey(solutionId)
    override val solutionTeacherDetailsState: StateFlow<LoadableState<SolutionDetails>> = solutionTeacherDetailsReplica.observe(this, errorHandler)
    override val replyInputControl = InputControl(componentScope)
    override val isSendingMessageProgress = MutableStateFlow(false)
    override val isChangingSolutionStatus = MutableStateFlow(false)
    override val isReplyButtonEnabled = computed(replyInputControl.text, String::isNotBlank)

    override fun onRefresh() {
        solutionTeacherDetailsReplica.refresh()
    }

    override fun onRetryClick() {
        solutionTeacherDetailsReplica.revalidate()
    }

    override fun onSendCommentClick() {
        if (isSendingMessageProgress.value) return

        componentScope.safeLaunch(errorHandler) {
            withProgress(isSendingMessageProgress) {
                lessonDetailsRepository.sendMessageByTeacher(solutionId, replyInputControl.text.value)
                replyInputControl.setText("")
            }
        }
    }

    override fun onSetSolutionStatus(status: LessonStatus) {
        if (isChangingSolutionStatus.value) return

        componentScope.safeLaunch(errorHandler) {
            withProgress(isChangingSolutionStatus) {
                lessonDetailsRepository.setSolutionStatus(solutionId, status)
                when (status) {
                    LessonStatus.Pending -> messageService.showMessage(
                        Message(text = R.string.solution_change_checked_type_to_pending.strResDesc())
                    )
                    LessonStatus.Reviewed -> messageService.showMessage(
                        Message(text = R.string.solution_change_checked_type_to_reviewed.strResDesc())
                    )
                }
            }
        }
    }

    override fun onReturnBackClick() {
        communication.onReturnBackTeacherSolutionDetails()
    }
}