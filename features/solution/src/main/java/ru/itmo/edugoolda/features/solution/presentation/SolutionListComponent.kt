package ru.itmo.edugoolda.features.solution.presentation

import kotlinx.coroutines.flow.StateFlow
import ru.itmo.edugoolda.core.utils.PagedState
import ru.itmo.edugoolda.data.solutions.api.SolutionId
import ru.itmo.edugoolda.data.solutions.api.SolutionInfoList

interface SolutionListComponent {
    val solutionState: StateFlow<PagedState<SolutionInfoList>>

    fun onSolutionClick(solutionId: SolutionId)
    fun onRefresh()
    fun onRetryClick()
    fun onLoadNext()
    fun onReturnBackClickRequest()

    interface Communication {
        fun onSolutionDetailsRequested(solutionId: SolutionId)
        fun onReturnBackFromSolutionListRequested()
    }
}