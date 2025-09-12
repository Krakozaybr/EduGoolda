package ru.itmo.edugoolda.features.join_requests.presentation.student

import kotlinx.coroutines.flow.StateFlow
import ru.itmo.edugoolda.core.utils.PagedState
import ru.itmo.edugoolda.data.join_requests.api.JoinRequestId
import ru.itmo.edugoolda.data.join_requests.api.JoinRequestList

interface JoinRequestsStudentComponent {
    val joinRequestState: StateFlow<PagedState<JoinRequestList>>

    fun onCancelJoinRequestClick(joinRequestId: JoinRequestId)
    fun onRefresh()
    fun onRetryClick()
    fun onLoadNext()
    fun onReturnBackClickRequest()

    interface Communication {
        fun onReturnBackFromJoinRequestsRequested()
    }
}