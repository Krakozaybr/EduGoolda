package ru.itmo.edugoolda.features.lesson.presentation.teacherLessonDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.itmo.edugoolda.core.dialog.standard.StandardDialog
import ru.itmo.edugoolda.core.theme.AppTheme
import ru.itmo.edugoolda.core.theme.custom.CustomTheme
import ru.itmo.edugoolda.core.widget.PullRefreshLceWidget
import ru.itmo.edugoolda.core.widget.text.FadingEdgeScrollableText
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonFullDetails
import ru.itmo.edugoolda.features.lesson.R

@Composable
fun TeacherLessonDetailsUi(
    component: TeacherLessonDetailsComponent,
    modifier: Modifier = Modifier,
) {
    val studentLessonDetailsState by component.lessonTeacherDetailsState.collectAsState()

    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    StandardDialog(component.dialogDeleteLesson)

    Column(modifier = modifier.navigationBarsPadding()) {
        Row(
            modifier = Modifier
                .height(50.dp + statusBarHeight)
                .background(CustomTheme.colors.content.contentActive)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { component.onReturnBackClick() },
                Modifier.padding(top = 3.dp + statusBarHeight, bottom = 6.dp, start = 5.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_left_white),
                    contentDescription = "Arrow Left",
                    tint = Color.Unspecified
                )
            }

            Text(
                text = stringResource(R.string.student_lesson_details_title),
                modifier = Modifier.padding(start = 30.dp, top = 3.dp + statusBarHeight, bottom = 6.dp),
                fontWeight = CustomTheme.typography.title.bold.fontWeight,
                fontSize = CustomTheme.typography.body.regular.fontSize,
                color = CustomTheme.colors.text.invert
            )
        }

        PullRefreshLceWidget(
            state = studentLessonDetailsState,
            onRefresh = component::onRefresh,
            onRetryClick = component::onRetryClick,
            isShowCircularProgressIndicator = false
        ) { data: LessonFullDetails, _: Boolean ->
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp)
                ) {
                    Text(
                        text = data.name,
                        fontWeight = CustomTheme.typography.title.boldSmallerSize.fontWeight,
                        fontSize = CustomTheme.typography.title.boldSmallerSize.fontSize,
                        modifier = Modifier
                            .padding(end = 30.dp)
                            .weight(1f)
                            .basicMarquee(),
                        maxLines = 1,
                        softWrap = false
                    )

                    IconButton(
                        onClick = { component.onDialogLessonDelete() },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.rubbish_bin_icon),
                            contentDescription = "Rubbish bin",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Text(
                    text = "${stringResource(R.string.lesson_desription_title)}:",
                    fontWeight = CustomTheme.typography.body.bold.fontWeight,
                    fontSize = CustomTheme.typography.body.bold.fontSize,
                    color = CustomTheme.colors.text.primary,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 15.dp, bottom = 4.dp)
                )
                FadingEdgeScrollableText(data.description, horizontalPadding = 20.dp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TeacherLessonDetailsUiPreview() {
    AppTheme {
        TeacherLessonDetailsUi(component = FakeTeacherLessonDetailsComponent())
    }
}