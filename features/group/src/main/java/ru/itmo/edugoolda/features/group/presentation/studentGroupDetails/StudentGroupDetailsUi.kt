package ru.itmo.edugoolda.features.group.presentation.studentGroupDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
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
import ru.itmo.edugoolda.data.group.group_info.api.GroupFullInfo
import ru.itmo.edugoolda.features.group.R

@Composable
fun StudentGroupDetailsUi(
    component: StudentGroupDetailsComponent,
    modifier: Modifier = Modifier,
) {
    val groupInfoState by component.groupInfoState.collectAsState()
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    StandardDialog(component.dialogQuit)

    Column(modifier = modifier.navigationBarsPadding()) {
        Row(
            modifier = Modifier
                .height(50.dp + statusBarHeight)
                .background(CustomTheme.colors.content.contentActive)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { component.onReturnBackRequestClick() },
                Modifier.padding(top = 3.dp + statusBarHeight, bottom = 6.dp, start = 5.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_left_white),
                    contentDescription = "Arrow Left",
                    tint = Color.Unspecified
                )
            }

            Text(
                text = stringResource(R.string.group_info_title),
                modifier = Modifier.padding(start = 30.dp, top = 3.dp + statusBarHeight, bottom = 6.dp),
                fontWeight = CustomTheme.typography.title.bold.fontWeight,
                fontSize = CustomTheme.typography.body.regular.fontSize,
                color = CustomTheme.colors.text.invert
            )
        }
        PullRefreshLceWidget(
            state = groupInfoState,
            onRefresh = component::onRefresh,
            onRetryClick = {
                component.onRetryClick()
            },
            isShowCircularProgressIndicator = false
        ) { data: GroupFullInfo, _: Boolean ->
            Column(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = data.name,
                        fontWeight = CustomTheme.typography.title.boldSmallerSize.fontWeight,
                        fontSize = CustomTheme.typography.title.boldSmallerSize.fontSize,
                        color = CustomTheme.colors.text.primary,
                        modifier = Modifier
                            .padding(top = 5.dp, end = 30.dp)
                            .weight(1f)
                            .basicMarquee(),
                        maxLines = 1,
                        softWrap = false
                    )

                    IconButton(
                        onClick = { component.onDialogQuitRequest() },
                        modifier = Modifier.size(37.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Rubbish bin",
                            modifier = Modifier.size(27.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                ) {
                    Text(
                        text = "${stringResource(R.string.group_subject_title)}: ",
                        fontWeight = CustomTheme.typography.body.bold.fontWeight,
                        fontSize = CustomTheme.typography.body.bold.fontSize,
                        color = CustomTheme.colors.text.primary,
                    )
                    Text(
                        text = data.subject.name,
                        fontWeight = CustomTheme.typography.body.regular.fontWeight,
                        fontSize = CustomTheme.typography.body.regular.fontSize,
                        color = CustomTheme.colors.text.primary,
                        modifier = Modifier
                            .padding(end = 30.dp)
                            .weight(1f)
                            .basicMarquee(),
                        maxLines = 1,
                        softWrap = false
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(top = 15.dp)
                ) {
                    Text(
                        text = "${stringResource(R.string.group_owner_title)}: ",
                        fontWeight = CustomTheme.typography.body.bold.fontWeight,
                        fontSize = CustomTheme.typography.body.bold.fontSize,
                        color = CustomTheme.colors.text.primary,
                    )
                    Text(
                        text = data.owner.name,
                        fontWeight = CustomTheme.typography.body.regular.fontWeight,
                        fontSize = CustomTheme.typography.body.regular.fontSize,
                        color = CustomTheme.colors.text.primary,
                        modifier = Modifier
                            .padding(end = 30.dp)
                            .weight(1f)
                            .basicMarquee(),
                        maxLines = 1,
                        softWrap = false
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(top = 15.dp)
                ) {
                    Text(
                        text = "${stringResource(R.string.group_count_members_title)}: ",
                        fontWeight = CustomTheme.typography.body.bold.fontWeight,
                        fontSize = CustomTheme.typography.body.bold.fontSize,
                        color = CustomTheme.colors.text.primary,
                    )
                    Text(
                        text = data.studentsCount.toString(),
                        fontWeight = CustomTheme.typography.body.regular.fontWeight,
                        fontSize = CustomTheme.typography.body.regular.fontSize,
                        color = CustomTheme.colors.text.primary,
                    )
                }

                Text(
                        text = "${stringResource(R.string.group_description_title)}: ",
                        fontWeight = CustomTheme.typography.body.bold.fontWeight,
                        fontSize = CustomTheme.typography.body.bold.fontSize,
                        color = CustomTheme.colors.text.primary,
                        modifier = Modifier.padding(top = 20.dp, bottom = 3.dp)
                    )
                FadingEdgeScrollableText(
                    data.description ?: stringResource(R.string.group_description_null),
                    horizontalPadding = 0.dp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TeacherGroupDetailsUiPreview() {
    AppTheme {
        StudentGroupDetailsUi(component = FakeStudentGroupDetailsComponent())
    }
}