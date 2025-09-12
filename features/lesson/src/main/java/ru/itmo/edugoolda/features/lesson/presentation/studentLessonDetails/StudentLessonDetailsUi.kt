package ru.itmo.edugoolda.features.lesson.presentation.studentLessonDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.itmo.edugoolda.core.theme.AppTheme
import ru.itmo.edugoolda.core.theme.custom.CustomTheme
import ru.itmo.edugoolda.core.widget.PullRefreshLceWidget
import ru.itmo.edugoolda.core.widget.text.FadingEdgeScrollableText
import ru.itmo.edugoolda.core.widget.text_field.MessageTextField
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonStatus
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonStudentDetails
import ru.itmo.edugoolda.data.lesson.lesson_details.internal.dto.onlyTimeFormat
import ru.itmo.edugoolda.data.lesson.lesson_details.internal.dto.toCurrentLocalDateTime
import ru.itmo.edugoolda.features.lesson.R

@Composable
fun StudentLessonDetailsUi(
    component: StudentLessonDetailsComponent,
    modifier: Modifier = Modifier,
) {
    val studentLessonDetailsState by component.lessonStudentDetailsState.collectAsState()

    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

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
        ) { data: LessonStudentDetails, _: Boolean ->
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp)
                ) {
                    Column {
                        Text(
                            text = data.name,
                            fontWeight = CustomTheme.typography.title.boldSmallerSize.fontWeight,
                            fontSize = CustomTheme.typography.title.boldSmallerSize.fontSize,
                            modifier = Modifier
                                .padding(end = 40.dp)
                                .basicMarquee(),
                            maxLines = 1,
                            softWrap = false
                        )
                        if (data.status == LessonStatus.Reviewed) {
                            Text(
                                text = "(${stringResource(R.string.lesson_checked_status)})",
                                fontWeight = CustomTheme.typography.body.regular.fontWeight,
                                fontSize = CustomTheme.typography.body.regular.fontSize,
                                modifier = Modifier.width(230.dp)
                            )
                        }
                    }
//                    Text(
//                        text = "${stringResource(R.string.lesson_deadline_title)}:\n${formatInstantToDateTimeString(data.deadline)}",
//                        fontWeight = CustomTheme.typography.body.bold.fontWeight,
//                        fontSize = CustomTheme.typography.body.bold.fontSize,
//                        modifier = Modifier.width(100.dp),
//                    )
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

                FadingEdgeScrollableText(
                    data.description
                        ?: stringResource(R.string.lesson_description_null),
                    horizontalPadding = 20.dp
                )

                if (data.isEstimatable && data.status == LessonStatus.Pending) {
                    MessageTextField(
                        onSendClick = { component.onSendCommentClick() },
                        inputControl = component.replyInputControl,
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .padding(top = 15.dp, bottom = 5.dp),
                        placeholder = "${stringResource(id = R.string.lesson_placeholder_reply)}..."
                    )
                }
            }
        }
        PullRefreshLceWidget(
            state = studentLessonDetailsState,
            onRefresh = component::onRefresh,
            onRetryClick = component::onRetryClick
        ) { data: LessonStudentDetails, _: Boolean ->
            LazyColumn(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
                    .fillMaxSize()

            ) {
                items(data.messages.reversed()) { item ->
                    MessageItem(
                        item.author.name,
                        item.message,
                        item.sentAt.toCurrentLocalDateTime().onlyTimeFormat(),
                        when (item.author.id) {
                            data.teacher.id -> Arrangement.Start
                            else -> Arrangement.End
                        },
                        when (item.author.id) {
                            data.teacher.id -> CustomTheme.colors.message.secondary
                            else -> CustomTheme.colors.message.primary
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MessageItem(
    authorName: String,
    text: String,
    sentAt: String,
    alignType: Arrangement.Horizontal,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp).padding(horizontal = 15.dp),
        horizontalArrangement = alignType
    ) {
        Column(
            modifier = Modifier
                .width(220.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(8.dp),
                    clip = true
                )
                .clip(RoundedCornerShape(8.dp))
                .background(backgroundColor)
        ) {
            Row(modifier = Modifier.padding(start = 8.dp, top = 5.dp)) {
                Text(
                    text = authorName,
                    style = CustomTheme.typography.body.regular13,
                    softWrap = false,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(end = 30.dp)
                        .weight(1f)
                        .basicMarquee()
                )
                Text(
                    text = sentAt,
                    fontWeight = CustomTheme.typography.body.regular13.fontWeight,
                    fontSize = CustomTheme.typography.body.regular13.fontSize,
                    modifier = Modifier.padding(end = 5.dp)
                )
            }
            Text(
                text = text,
                fontWeight = CustomTheme.typography.body.regular15.fontWeight,
                fontSize = CustomTheme.typography.body.regular15.fontSize,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .width(200.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MessageItemPreview() {
    AppTheme {
        MessageItem(
            authorName = "Pavel",
            text = "Привет, меня зовут Паша!",
            sentAt = "15:80",
            Arrangement.Start,
            backgroundColor = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StudentLessonDetailsUiPreview() {
    AppTheme {
        StudentLessonDetailsUi(component = FakeStudentLessonDetailsComponent())
    }
}