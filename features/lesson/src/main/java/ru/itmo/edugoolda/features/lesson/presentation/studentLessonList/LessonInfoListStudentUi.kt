package ru.itmo.edugoolda.features.lesson.presentation.studentLessonList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.aartikov.replica.paged.PagedLoadingStatus
import ru.itmo.edugoolda.core.theme.custom.CustomTheme
import ru.itmo.edugoolda.core.utils.TriggerLoadNext
import ru.itmo.edugoolda.core.widget.PullRefreshLceWidget
import ru.itmo.edugoolda.core.widget.lesson.LessonInfoStudentListItem
import ru.itmo.edugoolda.data.lesson.lesson_info.api.LessonInfoList
import ru.itmo.edugoolda.features.lesson.R

@Composable
fun LessonInfoListStudentUi(
    component: LessonInfoListStudentComponent,
    modifier: Modifier = Modifier,
) {

    val state by component.lessonInfoState.collectAsState()

    PullRefreshLceWidget(
        state = state,
        onRefresh = component::onRefresh,
        onRetryClick = component::onRetryClick,
        modifier = modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) { data: LessonInfoList, _: Boolean ->
        val lazyListState = rememberLazyListState()
        lazyListState.TriggerLoadNext(
            pagedState = state,
            hasNextPage = state.data?.hasNextPage == true,
            callback = component::onLoadNext
        )
        if (data.lessonInfoList.isNotEmpty()) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .fillMaxSize()
            ) {
                items(data.lessonInfoList) {
                    LessonInfoStudentListItem(
                        name = it.name,
                        createdAt = it.createdAt,
                        onLessonItemClick = { component.onLessonClick(it.id) }
                    )
                }
                if (state.loadingStatus == PagedLoadingStatus.LoadingNextPage) {
                    item {
                        CircularProgressIndicator()
                    }
                }
            }
        } else {

            LazyColumn(
                state = lazyListState,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Text(
                        text = stringResource(R.string.lessons_list_empty_student),
                        fontSize = CustomTheme.typography.body.regular18.fontSize,
                        fontWeight = CustomTheme.typography.body.regular18.fontWeight,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                    )
                }
                if (state.loadingStatus == PagedLoadingStatus.LoadingNextPage) {
                    item {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
