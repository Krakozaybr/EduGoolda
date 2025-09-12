package ru.itmo.edugoolda.features.root.presentation.start

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.delay
import ru.itmo.edugoolda.core.error_handling.ErrorHandler
import ru.itmo.edugoolda.core.error_handling.safeLaunch
import ru.itmo.edugoolda.core.utils.componentScope
import ru.itmo.edugoolda.data.auth.api.AuthStatusProvider
import ru.itmo.edugoolda.data.user.api.UserRepository
import ru.itmo.edugoolda.data.user.api.UserRole

class RealStartComponent(
    componentContext: ComponentContext,
    communication: StartComponent.Communication,
    errorHandler: ErrorHandler,
    authStatusProvider: AuthStatusProvider,
    profileRepository: UserRepository
) : ComponentContext by componentContext, StartComponent {
    init {
        componentScope.safeLaunch(
            errorHandler = errorHandler,
            onErrorHandled = {
                communication.authRequired()
            }
        ) {
            delay(1000)
            if (!authStatusProvider.isAuthorized.value) {
                communication.authRequired()
                return@safeLaunch
            }

            val profile = profileRepository.currentProfileReplica.getData()

            when (profile.role) {
                UserRole.Teacher -> communication.teacherDetailsRequired()
                UserRole.Student -> communication.studentDetailsRequired()
            }
        }
    }
}