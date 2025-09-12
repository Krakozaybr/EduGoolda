package ru.itmo.edugoolda.features.profile.presentation.viewProfile

import kotlinx.coroutines.flow.StateFlow
import ru.itmo.edugoolda.core.dialog.standard.StandardDialogControl
import ru.itmo.edugoolda.core.utils.LoadableState
import ru.itmo.edugoolda.data.user.api.Profile

interface ProfileComponent {
    val profileState: StateFlow<LoadableState<Profile>>
    val isLoggingOutProgress: StateFlow<Boolean>
    val dialogLogout: StandardDialogControl

    fun onRefresh()
    fun onRetryClick()
    fun onDialogLogoutRequest()

    interface Communication {
        fun onLogoutRequested()
    }
}