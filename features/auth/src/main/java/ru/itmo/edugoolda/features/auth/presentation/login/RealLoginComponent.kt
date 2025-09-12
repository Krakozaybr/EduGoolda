package ru.itmo.edugoolda.features.auth.presentation.login

import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.strResDesc
import kotlinx.coroutines.flow.MutableStateFlow
import ru.itmo.edugoolda.core.error_handling.ErrorHandler
import ru.itmo.edugoolda.core.error_handling.UnauthorizedException
import ru.itmo.edugoolda.core.error_handling.errorMessage
import ru.itmo.edugoolda.core.error_handling.safeLaunch
import ru.itmo.edugoolda.core.message.data.MessageService
import ru.itmo.edugoolda.core.message.domain.Message
import ru.itmo.edugoolda.core.utils.componentScope
import ru.itmo.edugoolda.core.utils.withProgress
import ru.itmo.edugoolda.data.auth.api.AuthRepository
import ru.itmo.edugoolda.data.auth.api.domain.Email
import ru.itmo.edugoolda.data.auth.api.domain.Password
import ru.itmo.edugoolda.features.auth.R
import ru.mobileup.kmm_form_validation.control.InputControl

class RealLoginComponent(
    componentContext: ComponentContext,
    private val communication: LoginComponent.Communication,
    private val errorHandler: ErrorHandler,
    private val authRepository: AuthRepository,
    private val messageService: MessageService,
) : LoginComponent, ComponentContext by componentContext {
    override val emailInputControl = InputControl(componentScope)
    override val passwordInputControl = InputControl(componentScope)
    override val isLoginProgress = MutableStateFlow(false)

    override fun onLoginClick() {
        if (isLoginProgress.value) return

        componentScope.safeLaunch(
            errorHandler, onErrorHandled = { error ->
                when (error) {
                    is UnauthorizedException -> showMessageInvalidEmailOrPassword()
                    else -> showMessageOtherError(error)
                }
            },
            showError = false
        ) {
            withProgress(isLoginProgress) {
                authRepository.login(
                    email = Email(emailInputControl.text.value),
                    password = Password(passwordInputControl.text.value)
                )
            }
            communication.onLoggedIn()
        }
    }

    private fun showMessageInvalidEmailOrPassword() {
        messageService.showMessage(
            Message(text = R.string.message_login_error.strResDesc())
        )
    }

    private fun showMessageOtherError(error: Exception) {
        messageService.showMessage(Message(text = error.errorMessage))
    }

    override fun onRegisterRequestClick() {
        communication.onRegisterRequest()
    }
}