package ru.itmo.edugoolda.features.home.presentation.teacher

import kotlinx.coroutines.flow.StateFlow
import ru.itmo.edugoolda.core.utils.LoadableState
import ru.itmo.edugoolda.data.home.api.HomeTeacherViewData
import ru.itmo.edugoolda.data.join_requests.api.JoinRequestId
import ru.itmo.edugoolda.data.solutions.api.SolutionId

interface HomeTeacherComponent {
    val mainState: StateFlow<LoadableState<HomeTeacherViewData>>

    fun onAcceptJoinRequestClick(joinRequestId: JoinRequestId)
    fun onDeclineJoinRequestClick(joinRequestId: JoinRequestId)
    fun onRefresh()
    fun onRetryClick()
    fun onSolutionClick(solutionId: SolutionId)
    fun onAllSolutionsClick()
    fun onAllJoinRequestsClick()

    interface Communication {
        fun onSolutionDetailsRequested(solutionId: SolutionId)
        fun onAllSolutionsRequested()
        fun onAllJoinRequestsRequested()
    }
}