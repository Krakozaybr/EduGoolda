package ru.itmo.edugoolda.features.home.presentation.teacher

import com.arkivanov.decompose.ComponentContext
import me.aartikov.replica.algebra.normal.combine
import me.aartikov.replica.algebra.paged.toReplica
import ru.itmo.edugoolda.core.error_handling.ErrorHandler
import ru.itmo.edugoolda.core.error_handling.safeLaunch
import ru.itmo.edugoolda.core.utils.componentScope
import ru.itmo.edugoolda.core.utils.observe
import ru.itmo.edugoolda.data.home.api.HomeTeacherViewData
import ru.itmo.edugoolda.data.join_requests.api.JoinRequestAction
import ru.itmo.edugoolda.data.join_requests.api.JoinRequestId
import ru.itmo.edugoolda.data.join_requests.api.JoinRequestRepository
import ru.itmo.edugoolda.data.solutions.api.SolutionId
import ru.itmo.edugoolda.data.solutions.api.SolutionRepository

class HomeTeacherComponentImpl(
    componentContext: ComponentContext,
    private val communication: HomeTeacherComponent.Communication,
    private val errorHandler: ErrorHandler,
    private val joinRequestRepository: JoinRequestRepository,
    solutionRepository: SolutionRepository,
) : HomeTeacherComponent, ComponentContext by componentContext {
    private val joinRequestsReplica = joinRequestRepository.joinRequestListReplica.toReplica()
    private val solutionsReplica = solutionRepository.solutionListReplica.toReplica()
    private val mainStateReplica = combine(
        joinRequestsReplica,
        solutionsReplica
    ) { joinRequests, solutions ->
        HomeTeacherViewData(
            joinRequests.joinRequestList,
            solutions.solutionInfoList
        )
    }
    override val mainState = mainStateReplica.observe(this, errorHandler)

    override fun onAcceptJoinRequestClick(joinRequestId: JoinRequestId) {
        componentScope.safeLaunch(errorHandler) {
            joinRequestRepository.respondToJoinRequest(joinRequestId, JoinRequestAction.Accept)
        }
    }

    override fun onDeclineJoinRequestClick(joinRequestId: JoinRequestId) {
        componentScope.safeLaunch(errorHandler) {
            joinRequestRepository.respondToJoinRequest(joinRequestId, JoinRequestAction.Decline)
        }
    }

    override fun onSolutionClick(solutionId: SolutionId) {
        communication.onSolutionDetailsRequested(solutionId)
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
}