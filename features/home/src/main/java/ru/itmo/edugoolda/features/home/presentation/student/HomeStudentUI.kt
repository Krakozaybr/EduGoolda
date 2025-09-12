package ru.itmo.edugoolda.features.home.presentation.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.itmo.edugoolda.core.R
import ru.itmo.edugoolda.core.theme.custom.CustomTheme
import ru.itmo.edugoolda.core.widget.PullRefreshLceWidget
import ru.itmo.edugoolda.core.widget.button.AppButton
import ru.itmo.edugoolda.core.widget.button.ButtonType
import ru.itmo.edugoolda.core.widget.join_requests.JoinRequestStudentListItem
import ru.itmo.edugoolda.core.widget.lesson.LessonInfoStudentListItem
import ru.itmo.edugoolda.data.home.api.HomeStudentViewData
import ru.itmo.edugoolda.features.home.R as homeR

@Composable
fun HomeStudentUi(
    component: HomeStudentComponent,
    modifier: Modifier = Modifier,
) {
    val mainState by component.mainState.collectAsState()

    PullRefreshLceWidget(
        state = mainState,
        onRefresh = component::onRefresh,
        onRetryClick = component::onRetryClick,
        modifier = modifier.statusBarsPadding(),
        isShowCircularProgressIndicator = false
    ) { data: HomeStudentViewData, _: Boolean ->

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 25.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = homeR.string.logo),
                    style = CustomTheme.typography.title.regular,
                    fontWeight = FontWeight.Bold,

                    )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 10.dp),
                    painter = painterResource(R.drawable.ic_40_solutions),
                    contentDescription = "View"
                )
                Text(
                    text = stringResource(id = homeR.string.lessons),
                    style = CustomTheme.typography.body.regular15,
                    fontWeight = FontWeight.Bold,
                )
            }

            // Solutions List
            LazyColumn(
                modifier = Modifier.heightIn(max = 300.dp)
            ) {
                items(data.lessonInfoList.take(2)) {
                    LessonInfoStudentListItem(
                        name = it.name,
                        createdAt = it.createdAt,
                        onLessonItemClick = { component.onLessonClick(it.id) }
                    )
                }
            }

            // All Solutions button
            AppButton(
                modifier = Modifier
                    .fillMaxWidth(),

                buttonType = ButtonType.Secondary,
                onClick = { component.onAllSolutionsClick() },
                contentPadding = PaddingValues(12.dp)
            ) {
                Text(
                    text = stringResource(id = homeR.string.go_to_schedule),
                    style = CustomTheme.typography.button.bold,
                )
                Icon(
                    modifier = Modifier.padding(start = 12.dp),
                    painter = painterResource(R.drawable.ic_24_app_button_path),
                    contentDescription = null
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 1.dp))

            // Requests section header
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 10.dp),
                    painter = painterResource(R.drawable.ic_40_group_join_request),
                    contentDescription = "View"
                )
                Text(
                    text = stringResource(id = homeR.string.join_requests),
                    style = CustomTheme.typography.body.regular15,
                    fontWeight = FontWeight.Bold
                )
            }

            // Request list
            LazyColumn(
                modifier = Modifier.heightIn(max = 300.dp)
            ) {
                items(data.joinRequests.take(2)) {
                    JoinRequestStudentListItem(
                        groupName = it.groupInfo.name,
                        date = it.createAt,
                        { component.onCancelJoinRequestClick(it.id) }
                    )
                }
            }

            // All requests button
            AppButton(
                modifier = Modifier
                    .fillMaxWidth().padding(bottom = 10.dp),
                buttonType = ButtonType.Secondary,
                onClick = { component.onAllJoinRequestsClick() },
                contentPadding = PaddingValues(12.dp)
            ) {
                Text(
                    text = stringResource(id = homeR.string.all_join_requests),
                    style = CustomTheme.typography.button.bold,
                )
                Icon(
                    modifier = Modifier.padding(start = 12.dp),
                    painter = painterResource(R.drawable.ic_24_app_button_path),
                    contentDescription = null
                )
            }
        }
    }
}
