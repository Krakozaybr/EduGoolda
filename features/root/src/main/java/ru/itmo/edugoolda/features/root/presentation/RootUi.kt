package ru.itmo.edugoolda.features.root.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ru.itmo.edugoolda.core.message.presentation.MessageUi
import ru.itmo.edugoolda.core.theme.AppTheme
import ru.itmo.edugoolda.core.theme.custom.CustomTheme
import ru.itmo.edugoolda.core.utils.LocalSystemBarsSettings
import ru.itmo.edugoolda.core.utils.accumulate

@Suppress("ModifierReused")
@Composable
fun RootUi(
    component: RootComponent,
    modifier: Modifier = Modifier
) {
    val childStack by component.childStack.collectAsState()

    SystemBarsColors()

    Children(childStack, modifier) { child ->
        when (val instance = child.instance) {
            else -> TODO()
        }
    }

    MessageUi(
        component = component.messageComponent,
        modifier = modifier,
        bottomPadding = 16.dp
    )
}

@Composable
private fun SystemBarsColors() {
    val systemUiController = rememberSystemUiController()
    val systemBarsSettings = LocalSystemBarsSettings.current.accumulate()

    val statusBarColor = Color.Transparent

    val navigationBarColor = when (systemBarsSettings.transparentNavigationBar) {
        true -> Color.Transparent
        false -> CustomTheme.colors.background.screen
    }

    val darkStatusBarIcons = CustomTheme.colors.isLight && !systemBarsSettings.lightStatusBarIcons

    val darkNavigationBarIcons = CustomTheme.colors.isLight

    LaunchedEffect(statusBarColor, darkStatusBarIcons) {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = darkStatusBarIcons
        )
    }

    LaunchedEffect(navigationBarColor, darkNavigationBarIcons) {
        systemUiController.setNavigationBarColor(
            color = navigationBarColor,
            darkIcons = darkNavigationBarIcons,
            navigationBarContrastEnforced = false
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun RootUiPreview() {
    AppTheme {
        RootUi(FakeRootComponent())
    }
}