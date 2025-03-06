package ru.itmo.edugoolda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.retainedComponent
import com.arkivanov.essenty.lifecycle.asEssentyLifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import ru.itmo.edugoolda.features.root.createRootComponent
import ru.itmo.edugoolda.features.root.presentation.RootUi
import ru.itmo.edugoolda.core.ComponentFactory
import ru.itmo.edugoolda.core.activity.ActivityProvider
import ru.itmo.edugoolda.core.koin
import ru.itmo.edugoolda.core.theme.AppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val activityProvider = application.koin.get<ActivityProvider>()
        activityProvider.attachActivity(this)
        lifecycle.asEssentyLifecycle().doOnDestroy {
            activityProvider.detachActivity()
        }

        val rootComponent = retainedComponent { componentContext ->
            val componentFactory = application.koin.get<ComponentFactory>()
            componentFactory.createRootComponent(componentContext)
        }

        setContent {
            AppTheme {
                RootUi(rootComponent)
            }
        }
    }
}
