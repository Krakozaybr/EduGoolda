package ru.itmo.edugoolda.features.lesson.presentation.studentLessonList

import com.arkivanov.decompose.ComponentContext
import ru.itmo.edugoolda.core.error_handling.ErrorHandler
import ru.itmo.edugoolda.core.utils.observe
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonId
import ru.itmo.edugoolda.data.lesson.lesson_info.api.LessonInfoRepository

class LessonInfoListStudentComponentImpl(
    componentContext: ComponentContext,
    private val communication: LessonInfoListStudentComponent.Communication,
    private val errorHandler: ErrorHandler,
    private val lessonInfoRepository: LessonInfoRepository,
    ) : LessonInfoListStudentComponent, ComponentContext by componentContext {

    private val lessonInfoReplica = lessonInfoRepository.lessonInfoListReplica
    override val lessonInfoState = lessonInfoReplica.observe(this, errorHandler)

    override fun onLessonClick(lessonId: LessonId) {
        communication.onLessonDetailsRequested(lessonId)
    }

    override fun onRefresh() {
        lessonInfoReplica.refresh()
    }

    override fun onRetryClick() {
        lessonInfoReplica.revalidate()
    }

    override fun onLoadNext() {
        lessonInfoReplica.loadNext()
    }
}