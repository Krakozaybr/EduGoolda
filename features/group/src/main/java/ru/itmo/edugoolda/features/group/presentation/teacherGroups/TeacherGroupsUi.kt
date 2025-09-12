package ru.itmo.edugoolda.features.group.presentation.teacherGroups

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
import ru.itmo.edugoolda.core.theme.AppTheme
import ru.itmo.edugoolda.core.theme.custom.CustomTheme
import ru.itmo.edugoolda.core.utils.TriggerLoadNext
import ru.itmo.edugoolda.core.widget.PullRefreshLceWidget
import ru.itmo.edugoolda.core.widget.text_field.AppTextField
import ru.itmo.edugoolda.data.group.group_list.api.GroupInfoList
import ru.itmo.edugoolda.features.group.R

@Composable
fun TeacherGroupsUi(
    component: TeacherGroupsComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.teacherGroupState.collectAsState()
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
            onRetryClick = component::onRetryClick
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
                        TeacherGroupItem(
                            { component.onGroupDetailsRequestClick(item.id) },
                            {
                                component.onGroupChangeFavouriteStatusRequestClick(
                                    item.id,
                                    !item.isFavourite
                                )
                            },
                            item.name,
                            item.subjectName,
                            item.isFavourite
                        )
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
                            text = stringResource(R.string.group_list_empty_teacher),
                            style = CustomTheme.typography.body.regular18,
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
}

@Composable
fun TeacherGroupItem(
    onGroupItemClick: () -> Unit,
    onChangeFavourite: () -> Unit,
    name: String,
    subjectName: String,
    isFavourite: Boolean,
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
                    .basicMarquee(),
                maxLines = 1,
                softWrap = false,
            )

            IconButton(
                onClick = { onChangeFavourite() },
                modifier = Modifier.size(35.dp)
            ) {
                Icon(
                    painter = when (isFavourite) {
                        true -> painterResource(R.drawable.favourite_pressed)
                        false -> painterResource(R.drawable.favourite_unpressed)
                    },
                    contentDescription = "Group Icon Favorite",
                    modifier = Modifier.size(width = 20.dp, height = 23.dp)
                )
            }

            IconButton(
                onClick = { onGroupItemClick() },
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .size(35.dp)
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
        TeacherGroupItem(
            {}, {}, "Группа 1 34234324 ", "Math 134324324234324", true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TeacherGroupsUIPreview() {
    AppTheme {
        TeacherGroupsUi(component = FakeTeacherGroupsComponent())
    }
}