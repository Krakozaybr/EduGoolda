package ru.itmo.edugoolda.features.profile.presentation.viewProfile

import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.strResDesc
import kotlinx.coroutines.flow.MutableStateFlow
import me.aartikov.replica.algebra.normal.withKey
import ru.itmo.edugoolda.core.dialog.standard.DialogButton
import ru.itmo.edugoolda.core.dialog.standard.StandardDialogData
import ru.itmo.edugoolda.core.dialog.standard.standardDialogControl
import ru.itmo.edugoolda.core.error_handling.ErrorHandler
import ru.itmo.edugoolda.core.error_handling.safeLaunch
import ru.itmo.edugoolda.core.utils.componentScope
import ru.itmo.edugoolda.core.utils.observe
import ru.itmo.edugoolda.core.utils.withProgress
import ru.itmo.edugoolda.data.auth.api.AuthRepository
import ru.itmo.edugoolda.data.user.api.UserId
import ru.itmo.edugoolda.data.user.api.UserRepository
import ru.itmo.edugoolda.features.profile.R

class RealProfileComponent(
    userId: UserId?,
    componentContext: ComponentContext,
    private val communication: ProfileComponent.Communication,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val errorHandler: ErrorHandler,
) : ProfileComponent, ComponentContext by componentContext {
    override val isLoggingOutProgress = MutableStateFlow(false)
    override val dialogLogout = standardDialogControl("logout")

    private val profileReplica = when (userId) {
        null -> userRepository.currentProfileReplica
        else -> userRepository.profileReplica.withKey(userId)
    }

    override val profileState = profileReplica.observe(this, errorHandler)

    override fun onRefresh() {
        profileReplica.refresh()
    }

    override fun onRetryClick() {
        profileReplica.revalidate()
    }

    private fun onLogoutRequestClick() {
        if (isLoggingOutProgress.value) return

        componentScope.safeLaunch(errorHandler) {
            withProgress(isLoggingOutProgress) {
                authRepository.logout()
            }
            communication.onLogoutRequested()
        }
    }

    override fun onDialogLogoutRequest() {
        dialogLogout.show(
            StandardDialogData(
                title = R.string.dialog_title_logout.strResDesc(),
                message = R.string.dialog_message_logout.strResDesc(),
                confirmButton = DialogButton(
                    text = R.string.dialog_confirm.strResDesc(),
                    action = {
                        onLogoutRequestClick()
                        dialogLogout.dismiss()
                    }
                ),
                dismissButton = DialogButton(
                    text = R.string.dialog_dismiss.strResDesc(),
                    action = {
                        dialogLogout.dismiss()
                    }
                ),
                dismissableByUser = true
            )
        )
    }
}