package ru.itmo.edugoolda.features.auth

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import ru.itmo.edugoolda.core.ComponentFactory
import ru.itmo.edugoolda.features.auth.presentation.login.LoginComponent
import ru.itmo.edugoolda.features.auth.presentation.login.RealLoginComponent
import ru.itmo.edugoolda.features.auth.presentation.register.RealRegisterComponent
import ru.itmo.edugoolda.features.auth.presentation.register.RegisterComponent

fun ComponentFactory.createLoginComponent(
    componentContext: ComponentContext,
    communication: LoginComponent.Communication,
): RealLoginComponent {
    return RealLoginComponent(componentContext, communication, get(), get(), get())
}

fun ComponentFactory.createRegisterComponent(
    componentContext: ComponentContext,
    communication: RegisterComponent.Communication,
): RealRegisterComponent {
    return RealRegisterComponent(componentContext, communication, get(), get())
}
