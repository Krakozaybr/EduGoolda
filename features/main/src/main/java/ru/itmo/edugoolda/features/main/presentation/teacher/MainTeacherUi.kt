package ru.itmo.edugoolda.features.main.presentation.teacher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import dev.icerock.moko.resources.desc.strResDesc
import ru.itmo.edugoolda.core.dialog.BottomSheet
import ru.itmo.edugoolda.core.theme.AppTheme
import ru.itmo.edugoolda.core.theme.custom.CustomTheme
import ru.itmo.edugoolda.core.widget.bottom_bar.CustomBottomBar
import ru.itmo.edugoolda.features.group.presentation.teacherGroups.TeacherGroupsUi
import ru.itmo.edugoolda.features.home.presentation.teacher.HomeTeacherUi
import ru.itmo.edugoolda.features.lesson.presentation.teacherLessonList.LessonInfoListTeacherUi
import ru.itmo.edugoolda.features.main.R
import ru.itmo.edugoolda.features.profile.presentation.viewProfile.ProfileUI
import ru.itmo.edugoolda.core.R as CoreR

@Composable
fun MainTeacherUi(
    component: MainTeacherComponent,
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
                    is MainTeacherComponent.Child.Groups -> TeacherGroupsUi(instance.component)
                    is MainTeacherComponent.Child.Home -> HomeTeacherUi(instance.component)
                    is MainTeacherComponent.Child.Profile -> ProfileUI(instance.component)
                    is MainTeacherComponent.Child.Lessons -> LessonInfoListTeacherUi(instance.component)
                }
            }
        },
        bottomBar = {
            CustomBottomBar(
                items = MainTeacherComponent.Tab.entries,
                getString = { it.titleResId.strResDesc() },
                getIcon = { it.iconResId },
                onItemClick = component::onTabClick,
                selectedItem = selectedTab,
                modifier = Modifier.navigationBarsPadding()
            )
        }
    )

    BottomSheet(component.simpleDialogControl) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row {
                Text(
                    text = stringResource(R.string.main_tab_create),
                    style = CustomTheme.typography.title.bold,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = component.simpleDialogControl::dismiss
                ) {
                    Icon(
                        painter = painterResource(CoreR.drawable.ic_24_close),
                        contentDescription = null
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clickable {
                            component.onCreateLessonClick()
                        }
                        .padding(4.dp)
                ) {
                    Icon(
                        painter = painterResource(CoreR.drawable.ic_24_lessons),
                        contentDescription = null
                    )

                    Text(
                        text = stringResource(R.string.main_tab_create_lesson),
                        style = CustomTheme.typography.body.regular
                    )
                }
                Row(
                    modifier = Modifier
                        .clickable {
                            component.onCreateGroupClick()
                        }
                        .padding(4.dp)
                ) {
                    Icon(
                        painter = painterResource(CoreR.drawable.ic_24_groups),
                        contentDescription = null
                    )

                    Text(
                        text = stringResource(R.string.main_tab_create_group),
                        style = CustomTheme.typography.body.regular
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MainTeacherUiPreview() {
    AppTheme {
        MainTeacherUi(FakeMainTeacherComponent())
    }
}
