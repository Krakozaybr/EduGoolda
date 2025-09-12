package ru.itmo.edugoolda.features.main.presentation.student

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.stack.Children
import dev.icerock.moko.resources.desc.strResDesc
import ru.itmo.edugoolda.core.theme.AppTheme
import ru.itmo.edugoolda.core.widget.bottom_bar.CustomBottomBar
import ru.itmo.edugoolda.features.group.presentation.studentGroups.StudentGroupsUi
import ru.itmo.edugoolda.features.home.presentation.student.HomeStudentUi
import ru.itmo.edugoolda.features.lesson.presentation.studentLessonList.LessonInfoListStudentUi
import ru.itmo.edugoolda.features.profile.presentation.viewProfile.ProfileUI

@Composable
fun MainStudentUi(
    component: MainStudentComponent,
    modifier: Modifier = Modifier
) {
    val stack by component.stack.collectAsState()
    val selectedTab by component.selectedTab.collectAsState()

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.navigationBars,
        content = {
            Children(stack, Modifier.padding(it)) {
                when (val instance = it.instance) {
                    is MainStudentComponent.Child.Groups -> StudentGroupsUi(instance.component)
                    is MainStudentComponent.Child.Home -> HomeStudentUi(instance.component)
                    is MainStudentComponent.Child.Lessons -> LessonInfoListStudentUi(instance.component)
                    is MainStudentComponent.Child.Profile -> ProfileUI(instance.component)
                }
            }
        },
        bottomBar = {
            CustomBottomBar(
                items = MainStudentComponent.Tab.entries,
                getString = { it.titleResId.strResDesc() },
                getIcon = { it.iconResId },
                onItemClick = component::onTabClick,
                selectedItem = selectedTab,
                modifier = Modifier.navigationBarsPadding()
            )
        }
    )
}

@Preview
@Composable
private fun MainStudentUiPreview() {
    AppTheme {
        MainStudentUi(FakeMainStudentComponent())
    }
}