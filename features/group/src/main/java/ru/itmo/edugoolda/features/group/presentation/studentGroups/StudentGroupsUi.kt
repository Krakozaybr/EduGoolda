package ru.itmo.edugoolda.features.group.presentation.studentGroups

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.aartikov.replica.paged.PagedLoadingStatus
import ru.itmo.edugoolda.core.dialog.DialogAddGroup
import ru.itmo.edugoolda.core.theme.AppTheme
import ru.itmo.edugoolda.core.theme.custom.CustomTheme
import ru.itmo.edugoolda.core.utils.TriggerLoadNext
import ru.itmo.edugoolda.core.widget.PullRefreshLceWidget
import ru.itmo.edugoolda.core.widget.text_field.AppTextField
import ru.itmo.edugoolda.data.group.group_list.api.GroupInfoList
import ru.itmo.edugoolda.features.group.R

@Composable
fun StudentGroupsUi(
    component: StudentGroupsComponent,
    modifier: Modifier = Modifier,
) {

    DialogAddGroup(component.dialogAddGroup, component.groupEnteringCodeInputControl)

    val state by component.studentGroupState.collectAsState()

    Column(modifier = modifier.statusBarsPadding()) {
        AppTextField(
            inputControl = component.groupSearchInputControl,
            placeholder = stringResource(R.string.search_students_group),
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp, bottom = 5.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    tint = Color.Gray,
                    contentDescription = "Group Icon",
                    modifier = Modifier.size(25.dp)
                )
            }
        )

        PullRefreshLceWidget(
            state = state,
            onRefresh = component::onRefresh,
            onRetryClick = component::onRetryClick,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) { data: GroupInfoList, _: Boolean ->
            val lazyListState = rememberLazyListState()
            lazyListState.TriggerLoadNext(
                pagedState = state,
                hasNextPage = state.data?.hasNextPage == true,
                callback = component::onLoadNext
            )
            if (data.groups.isNotEmpty()) {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                        .fillMaxSize()
                ) {
                    items(data.groups) { item ->
                        StudentGroupItem(
                            { component.onGroupDetailsRequestClick(item.id) },
                            item.name,
                            item.subjectName
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
                            text = stringResource(R.string.group_list_empty_student),
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

        IconButton(
            onClick = { component.onDialogAddGroup() },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 5.dp)
                .size(60.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.add_group_button),
                contentDescription = "Add Group Icon",
                modifier = Modifier.size(70.dp),
                tint = Color.Unspecified

            )
        }
    }
}

@Composable
fun StudentGroupItem(
    onGroupItemClick: () -> Unit,
    name: String,
    subjectName: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(vertical = 6.dp, horizontal = 4.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp),
                clip = true
            )
            .clip(RoundedCornerShape(8.dp))
            .background(CustomTheme.colors.background.backgroundPrimary)
    ) {
        Row(
            modifier = Modifier.height(60.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Group Icon",
                modifier = Modifier
                    .padding(start = 15.dp, end = 10.dp)
                    .size(27.dp)
            )

            Text(
                text = "$name, $subjectName",
                fontWeight = CustomTheme.typography.body.regular.fontWeight,
                fontSize = CustomTheme.typography.body.regular.fontSize,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 20.dp)
                    .weight(1f)
                    .basicMarquee()
            )

            IconButton(
                onClick = { onGroupItemClick() }
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_forward),
                    contentDescription = "Group Icon"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewGroupItem() {
    AppTheme {
        StudentGroupItem(
            {}, "Группа 1", "Math"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StudentGroupsUiPreview() {
    AppTheme {
        StudentGroupsUi(component = FakeStudentGroupsComponent())
    }
}