package ru.itmo.edugoolda.features.profile.presentation.viewProfile

import kotlinx.coroutines.flow.MutableStateFlow
import ru.itmo.edugoolda.core.dialog.standard.fakeStandardDialogControl
import ru.itmo.edugoolda.core.utils.LoadableState
import ru.itmo.edugoolda.data.user.api.Profile

class FakeProfileComponent : ProfileComponent {
    override val profileState = MutableStateFlow(LoadableState(data = Profile.MOCK))
    override val isLoggingOutProgress = MutableStateFlow(false)
    override val dialogLogout = fakeStandardDialogControl()

    override fun onRefresh() = Unit
    override fun onRetryClick() = Unit
    override fun onDialogLogoutRequest() = Unit
}