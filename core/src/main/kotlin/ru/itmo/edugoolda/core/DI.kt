package ru.itmo.edugoolda.core

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.Dispatchers
import me.aartikov.replica.client.ReplicaClient
import me.aartikov.replica.network.AndroidNetworkConnectivityProvider
import me.aartikov.replica.network.NetworkConnectivityProvider
import org.koin.core.component.get
import org.koin.dsl.module
import ru.itmo.edugoolda.core.activity.ActivityProvider
import ru.itmo.edugoolda.core.debug_tools.DebugTools
import ru.itmo.edugoolda.core.debug_tools.RealDebugTools
import ru.itmo.edugoolda.core.error_handling.ErrorHandler
import ru.itmo.edugoolda.core.message.data.MessageService
import ru.itmo.edugoolda.core.message.data.MessageServiceImpl
import ru.itmo.edugoolda.core.message.presentation.MessageComponent
import ru.itmo.edugoolda.core.message.presentation.RealMessageComponent
import ru.itmo.edugoolda.core.network.NetworkApiFactory
import ru.itmo.edugoolda.core.network.createOkHttpEngine
import ru.itmo.edugoolda.core.permissions.PermissionService
import ru.itmo.edugoolda.core.settings.AndroidSettingsFactory
import ru.itmo.edugoolda.core.settings.SettingsFactory

fun coreModule(backendUrl: String) = module {
    single { ActivityProvider() }
    single<NetworkConnectivityProvider> { AndroidNetworkConnectivityProvider(get()) }
    single { ReplicaClient(get()) }
    single<MessageService> { MessageServiceImpl() }
    single { ErrorHandler(get()) }
    single<DebugTools> { RealDebugTools(get(), get()) }
    single { createOkHttpEngine(get()) }
    single {
        NetworkApiFactory(
            loggingEnabled = BuildConfig.DEBUG,
            backendUrl = backendUrl,
            httpClientEngine = get()
        )
    }
    single(createdAtStart = true) { PermissionService(get(), get()) }
    single<SettingsFactory> { AndroidSettingsFactory(get(), Dispatchers.IO) }
}

fun ComponentFactory.createMessageComponent(
    componentContext: ComponentContext
): MessageComponent {
    return RealMessageComponent(componentContext, get())
}
