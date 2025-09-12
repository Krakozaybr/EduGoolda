package ru.itmo.edugoolda.features.profile

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import ru.itmo.edugoolda.core.ComponentFactory
import ru.itmo.edugoolda.data.user.api.UserId
import ru.itmo.edugoolda.features.profile.presentation.viewProfile.ProfileComponent
import ru.itmo.edugoolda.features.profile.presentation.viewProfile.RealProfileComponent

fun ComponentFactory.createProfileComponent(
    componentContext: ComponentContext,
    userId: UserId?,
    communication: ProfileComponent.Communication
): RealProfileComponent {
    return RealProfileComponent(userId, componentContext, communication, get(), get(), get())
}