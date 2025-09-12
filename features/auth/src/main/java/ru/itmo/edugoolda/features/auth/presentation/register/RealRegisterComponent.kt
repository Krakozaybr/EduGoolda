package ru.itmo.edugoolda.features.auth.presentation.register

import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.strResDesc
import kotlinx.coroutines.flow.MutableStateFlow
import ru.itmo.edugoolda.core.error_handling.ErrorHandler
import ru.itmo.edugoolda.core.error_handling.safeLaunch
import ru.itmo.edugoolda.core.utils.componentScope
import ru.itmo.edugoolda.core.utils.withProgress
import ru.itmo.edugoolda.data.auth.api.AuthRepository
import ru.itmo.edugoolda.data.auth.api.domain.Email
import ru.itmo.edugoolda.data.auth.api.domain.Password
import ru.itmo.edugoolda.data.user.api.UserRole
import ru.itmo.edugoolda.features.auth.R
import ru.mobileup.kmm_form_validation.control.InputControl

class RealRegisterComponent(
    componentContext: ComponentContext,
    private val communication: RegisterComponent.Communication,
    private val errorHandler: ErrorHandler,
    private val authRepository: AuthRepository,
) : RegisterComponent, ComponentContext by componentContext {
    override val emailInputControl = InputControl(componentScope)
    override val passwordInputControl = InputControl(componentScope)
    override val userNameInputControl = InputControl(componentScope)
    override val selectedRole = MutableStateFlow(UserRole.Teacher)
    override val isRegisterProgress = MutableStateFlow(false)

    override fun onRegisterClick() {
        if (isRegisterProgress.value) return

        if (isValidEmail(emailInputControl.text.value) && isValidPassword(passwordInputControl.text.value)) {
            emailInputControl.error.value = null
            componentScope.safeLaunch(errorHandler) {
                withProgress(isRegisterProgress) {
                    authRepository.register(
                        email = Email(emailInputControl.text.value),
                        password = Password(passwordInputControl.text.value),
                        name = userNameInputControl.text.value,
                        role = selectedRole.value
                    )
                    communication.onRegistered()
                }
            }
        } else if (!isValidEmail(emailInputControl.text.value)) {
            emailInputControl.error.value = R.string.register_incorrect_email_error.strResDesc()
        } else if (!isValidPassword(passwordInputControl.text.value)) {
            passwordInputControl.error.value = R.string.register_incorrect_password_error.strResDesc()
        }
    }

    override fun onBackButtonClick() {
        communication.onLoginRequest()
    }

    override fun onUserRoleSelect(newRole: UserRole) {
        selectedRole.value = newRole
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
        )
        return email.matches(emailRegex)
    }

    private fun isValidPassword(password: String): Boolean {
        val regex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{6,32}$")
        return regex.matches(password)
    }
}