package ru.itmo.edugoolda.features.lesson.presentation.teacherLessonDetails

import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.strResDesc
import kotlinx.coroutines.flow.MutableStateFlow
import me.aartikov.replica.algebra.normal.withKey
import ru.itmo.edugoolda.core.dialog.standard.DialogButton
import ru.itmo.edugoolda.core.dialog.standard.StandardDialogData
import ru.itmo.edugoolda.core.dialog.standard.standardDialogControl
import ru.itmo.edugoolda.core.error_handling.ErrorHandler
import ru.itmo.edugoolda.core.error_handling.safeLaunch
import ru.itmo.edugoolda.core.utils.ResourceFormatted
import ru.itmo.edugoolda.core.utils.componentScope
import ru.itmo.edugoolda.core.utils.observe
import ru.itmo.edugoolda.core.utils.withProgress
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonDetailsRepository
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonId
import ru.itmo.edugoolda.data.lesson.lesson_info.api.LessonInfoRepository
import ru.itmo.edugoolda.features.lesson.R

class RealTeacherLessonDetailsComponent(
    private val lessonId: LessonId,
    componentContext: ComponentContext,
    private val communication: TeacherLessonDetailsComponent.Communication,
    private val errorHandler: ErrorHandler,
    private val lessonDetailsRepository: LessonDetailsRepository,
    private val lessonInfoRepository: LessonInfoRepository
) : TeacherLessonDetailsComponent, ComponentContext by componentContext {
    private val lessonTeacherDetailsReplica = lessonDetailsRepository.lessonTeacherDetailsReplica.withKey(lessonId)
    override val lessonTeacherDetailsState = lessonTeacherDetailsReplica.observe(this, errorHandler)
    override val isDeletingLessonProgress = MutableStateFlow(false)
    override val dialogDeleteLesson = standardDialogControl("delete lesson")

    override fun onRefresh() {
        lessonTeacherDetailsReplica.refresh()
    }

    override fun onRetryClick() {
        lessonTeacherDetailsReplica.revalidate()
    }

    override fun onReturnBackClick() {
        communication.onReturnBackTeacherLessonDetails()
    }

    private fun onLessonDelete() {
        if (isDeletingLessonProgress.value) return

        componentScope.safeLaunch(errorHandler) {
            withProgress(isDeletingLessonProgress) {
                lessonInfoRepository.deleteLesson(lessonId)
            }
            communication.onTeacherLessonDeleted()
        }
    }

    override fun onDialogLessonDelete() {
        val data = lessonTeacherDetailsState.value.data ?: return
        dialogDeleteLesson.show(
            StandardDialogData(
                title = R.string.delete_lesson.strResDesc(),
                message = StringDesc.ResourceFormatted(
                    R.string.delete_lesson_message,
                    data.name
                ),
                confirmButton = DialogButton(
                    text = R.string.lesson_confirm.strResDesc(),
                    action = {
                        onLessonDelete()
                        dialogDeleteLesson.dismiss()
                    }
                ),
                dismissButton = DialogButton(
                    text = R.string.lesson_dismiss.strResDesc(),
                    action = {
                        dialogDeleteLesson.dismiss()
                    }
                ),
                dismissableByUser = true
            )
        )
    }
}