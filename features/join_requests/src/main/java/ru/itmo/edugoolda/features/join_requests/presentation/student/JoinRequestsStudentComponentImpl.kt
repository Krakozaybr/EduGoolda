package ru.itmo.edugoolda.features.join_requests.presentation.student

import com.arkivanov.decompose.ComponentContext
import ru.itmo.edugoolda.core.error_handling.ErrorHandler
import ru.itmo.edugoolda.core.error_handling.safeLaunch
import ru.itmo.edugoolda.core.utils.componentScope
import ru.itmo.edugoolda.core.utils.observe
import ru.itmo.edugoolda.data.join_requests.api.JoinRequestAction
import ru.itmo.edugoolda.data.join_requests.api.JoinRequestId
import ru.itmo.edugoolda.data.join_requests.api.JoinRequestRepository

class JoinRequestsStudentComponentImpl(
    componentContext: ComponentContext,
    private val communication: JoinRequestsStudentComponent.Communication,
    private val errorHandler: ErrorHandler,
    private val joinRequestRepository: JoinRequestRepository
) : JoinRequestsStudentComponent, ComponentContext by componentContext {

    private val invitationReplica = joinRequestRepository.joinRequestListReplica
    override val joinRequestState = invitationReplica.observe(this, errorHandler)

    override fun onCancelJoinRequestClick(joinRequestId: JoinRequestId) {
        componentScope.safeLaunch(errorHandler) {
            joinRequestRepository.respondToJoinRequest(joinRequestId, JoinRequestAction.Cancel)
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