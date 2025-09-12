package ru.itmo.edugoolda.features.join_requests.presentation.teacher

import com.arkivanov.decompose.ComponentContext
import ru.itmo.edugoolda.core.error_handling.ErrorHandler
import ru.itmo.edugoolda.core.error_handling.safeLaunch
import ru.itmo.edugoolda.core.utils.componentScope
import ru.itmo.edugoolda.core.utils.observe
import ru.itmo.edugoolda.data.join_requests.api.JoinRequestAction
import ru.itmo.edugoolda.data.join_requests.api.JoinRequestId
import ru.itmo.edugoolda.data.join_requests.api.JoinRequestRepository

class JoinRequestsTeacherComponentImpl(
    componentContext: ComponentContext,
    private val communication: JoinRequestsTeacherComponent.Communication,
    private val errorHandler: ErrorHandler,
    private val joinRequestRepository: JoinRequestRepository
) : JoinRequestsTeacherComponent, ComponentContext by componentContext {

    private val invitationReplica = joinRequestRepository.joinRequestListReplica
    override val joinRequestState = invitationReplica.observe(this, errorHandler)

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

    override fun onRefresh() {
        invitationReplica.refresh()
    }

    override fun onRetryClick() {
        invitationReplica.revalidate()
    }

    override fun onLoadNext() {
        invitationReplica.loadNext()
    }

    override fun onReturnBackClickRequest() {
        communication.onReturnBackFromJoinRequestsRequested()
    }
}