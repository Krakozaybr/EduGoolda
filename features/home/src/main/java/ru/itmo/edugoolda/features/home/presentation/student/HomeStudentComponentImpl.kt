package ru.itmo.edugoolda.features.home.presentation.student

import com.arkivanov.decompose.ComponentContext
import me.aartikov.replica.algebra.normal.combine
import me.aartikov.replica.algebra.paged.toReplica
import ru.itmo.edugoolda.core.error_handling.ErrorHandler
import ru.itmo.edugoolda.core.error_handling.safeLaunch
import ru.itmo.edugoolda.core.utils.componentScope
import ru.itmo.edugoolda.core.utils.observe
import ru.itmo.edugoolda.data.home.api.HomeStudentViewData
import ru.itmo.edugoolda.data.join_requests.api.JoinRequestAction
import ru.itmo.edugoolda.data.join_requests.api.JoinRequestId
import ru.itmo.edugoolda.data.join_requests.api.JoinRequestRepository
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonId
import ru.itmo.edugoolda.data.lesson.lesson_info.api.LessonInfoRepository

class HomeStudentComponentImpl(
    componentContext: ComponentContext,
    private val communication: HomeStudentComponent.Communication,
    private val errorHandler: ErrorHandler,
    private val joinRequestRepository: JoinRequestRepository,
    lessonInfoRepository: LessonInfoRepository,

    ) : HomeStudentComponent, ComponentContext by componentContext {

    private val joinRequestsReplica = joinRequestRepository.joinRequestListReplica.toReplica()
    private val solutionsReplica = lessonInfoRepository.lessonInfoListReplica.toReplica()
    private val mainStateReplica = combine(
        joinRequestsReplica,
        solutionsReplica
    ) { joinRequests, solutions ->
        HomeStudentViewData(
            joinRequests.joinRequestList,
            solutions.lessonInfoList
        )
    }
    override val mainState = mainStateReplica.observe(this, errorHandler)

    override fun onLessonClick(lessonId: LessonId) {
        communication.onLessonDetailsRequested(lessonId)
    }

    override fun onAllSolutionsClick() {
        communication.onAllSolutionsRequested()
    }

    override fun onAllJoinRequestsClick() {
        communication.onAllJoinRequestsRequested()
    }

    override fun onRefresh() {
        mainStateReplica.refresh()
    }

    override fun onRetryClick() {
        mainStateReplica.revalidate()
    }

    override fun onCancelJoinRequestClick(joinRequestId: JoinRequestId) {
        componentScope.safeLaunch(errorHandler) {
            joinRequestRepository.respondToJoinRequest(joinRequestId, JoinRequestAction.Cancel)
        }
    }
}