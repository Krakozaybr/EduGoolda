package ru.itmo.edugoolda.features.solution.presentation

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import me.aartikov.replica.paged.PagedLoadingStatus
import ru.itmo.edugoolda.core.theme.custom.CustomTheme
import ru.itmo.edugoolda.core.utils.TriggerLoadNext
import ru.itmo.edugoolda.core.widget.PullRefreshLceWidget
import ru.itmo.edugoolda.core.widget.solutions.SolutionListItem
import ru.itmo.edugoolda.data.solutions.api.SolutionInfoList
import ru.itmo.edugoolda.features.solution.R

@Composable
fun SolutionListUi(
    component: SolutionListComponent,
    modifier: Modifier = Modifier
) {
    val state by component.solutionState.collectAsState()

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
                onClick = { component.onReturnBackClickRequest() },
                Modifier.padding(top = 3.dp + statusBarHeight, bottom = 6.dp, start = 5.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_left_white),
                    contentDescription = "Arrow Left",
                    tint = Color.Unspecified
                )
            }

            Text(
                text = stringResource(R.string.solution_title),
                modifier = Modifier.padding(start = 30.dp, top = 3.dp + statusBarHeight, bottom = 6.dp),
                fontWeight = CustomTheme.typography.title.bold.fontWeight,
                fontSize = CustomTheme.typography.body.regular.fontSize,
                color = CustomTheme.colors.text.invert
            )
        }

        PullRefreshLceWidget(
            state = state,
            onRefresh = component::onRefresh,
            onRetryClick = component::onRetryClick
        ) { data: SolutionInfoList, _: Boolean ->
            val lazyListState = rememberLazyListState()
            lazyListState.TriggerLoadNext(
                pagedState = state,
                hasNextPage = state.data?.hasNextPage == true,
                callback = component::onLoadNext
            )
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp).fillMaxSize()
            ) {
                items(data.solutionInfoList) {
                    SolutionListItem(
                        studentName = it.student.name,
                        sentAt = it.sentAt,
                        lessonName = it.lessonInfo.name,
                        onClick = { component.onSolutionClick(it.id) }
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
