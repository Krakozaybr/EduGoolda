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
import ru.itmo.edugoolda.features.auth.presentation.auth.AuthUi
import ru.itmo.edugoolda.features.group.GroupUi
import ru.itmo.edugoolda.features.join_requests.presentation.student.JoinRequestsStudentUi
import ru.itmo.edugoolda.features.join_requests.presentation.teacher.JoinRequestsTeacherUi
import ru.itmo.edugoolda.features.lesson.LessonsUi
import ru.itmo.edugoolda.features.main.presentation.student.MainStudentUi
import ru.itmo.edugoolda.features.main.presentation.teacher.MainTeacherUi
import ru.itmo.edugoolda.features.root.presentation.start.StartUi

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
            is RootComponent.Child.Auth -> AuthUi(instance.instance)
            is RootComponent.Child.StudentMain -> MainStudentUi(instance.instance)
            is RootComponent.Child.TeacherMain -> MainTeacherUi(instance.instance)
            is RootComponent.Child.Group -> GroupUi(instance.instance)
            is RootComponent.Child.JoinRequestsTeacher -> JoinRequestsTeacherUi(instance.instance)
            is RootComponent.Child.JoinRequestsStudent -> JoinRequestsStudentUi(instance.instance)
            is RootComponent.Child.Lessons -> LessonsUi(instance.instance)
            is RootComponent.Child.Start -> StartUi(instance.instance)
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