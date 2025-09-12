package ru.itmo.edugoolda.features.group.presentation.teacherGroupDetails

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.aartikov.replica.paged.PagedLoadingStatus
import ru.itmo.edugoolda.core.dialog.standard.StandardDialog
import ru.itmo.edugoolda.core.theme.AppTheme
import ru.itmo.edugoolda.core.theme.custom.CustomTheme
import ru.itmo.edugoolda.core.utils.TriggerLoadNext
import ru.itmo.edugoolda.core.widget.PullRefreshLceWidget
import ru.itmo.edugoolda.core.widget.button.AppButton
import ru.itmo.edugoolda.core.widget.button.ButtonType
import ru.itmo.edugoolda.core.widget.text.FadingEdgeScrollableText
import ru.itmo.edugoolda.data.group.group_info.api.GroupFullInfo
import ru.itmo.edugoolda.data.group.group_students_list.api.GroupStudentsList
import ru.itmo.edugoolda.data.group.group_students_list.api.KickType
import ru.itmo.edugoolda.features.group.R

@Composable
fun TeacherGroupDetailsUi(
    component: TeacherGroupDetailsComponent,
    modifier: Modifier = Modifier,
) {
    val groupOfStudentsState by component.groupOfStudentsState.collectAsState()
    val groupInvitationDataState by component.groupInvitationDataState.collectAsState()
    val groupInfoState by component.groupInfoState.collectAsState()

    val context = LocalContext.current

    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    fun copyText(text: String) {
        val clipboard =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(
            ClipData.newPlainText(
                "Group Code", text
            )
        )
    }

    StandardDialog(component.dialogDeleteGroup)
    StandardDialog(component.dialogKickMember)

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
                modifier = Modifier.padding(
                    start = 30.dp,
                    top = 3.dp + statusBarHeight,
                    bottom = 6.dp
                ),
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
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(
                    modifier = Modifier
                        .padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = data.name,
                        style = CustomTheme.typography.title.boldSmallerSize,
                        color = CustomTheme.colors.text.primary,
                        modifier = Modifier
                            .padding(top = 5.dp, end = 30.dp)
                            .weight(1f)
                            .basicMarquee(),
                        maxLines = 1,
                        softWrap = false
                    )

                    IconButton(
                        onClick = { component.onDialogDeleteGroup() },
                        modifier = Modifier.size(35.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.rubbish_bin_icon),
                            contentDescription = "Rubbish bin",
                            modifier = Modifier.size(30.dp)
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

                Text(
                    text = "${stringResource(R.string.group_description_title)}: ",
                    style = CustomTheme.typography.body.bold,
                    color = CustomTheme.colors.text.primary,
                    modifier = Modifier.padding(top = 10.dp, bottom = 3.dp)
                )
                FadingEdgeScrollableText(
                    data.description ?: stringResource(R.string.group_description_null),
                    horizontalPadding = 0.dp
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(top = 20.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${stringResource(R.string.group_invitation_code)}:",
                modifier = Modifier
                    .padding(end = 10.dp),
                style = CustomTheme.typography.body.bold,
                color = CustomTheme.colors.text.primary,
            )

            AppButton(
                onClick = {
                    if (groupInvitationDataState != null) {
                        copyText(groupInvitationDataState!!.code.value)
                        component.onShowMessageCodeCopied(groupInvitationDataState!!.code.value)
                    } else {
                        component.onGroupCodeGenerateRequestClick() { code ->
                            copyText(code)
                            component.onShowMessageCodeCopied(code)
                        }
                    }
                },
                text = when (groupInvitationDataState) {
                    null -> "∗".repeat(8)
                    else -> groupInvitationDataState!!.code.value
                },
                buttonType = ButtonType.Secondary,
                modifier = Modifier.size(height = 30.dp, width = 94.dp),
                contentPadding = PaddingValues(3.dp),

                )
        }

        Text(
            text = "${stringResource(R.string.group_members_title)}:",
            style = CustomTheme.typography.body.bold,
            color = CustomTheme.colors.text.primary,
            modifier = Modifier.padding(top = 10.dp, bottom = 5.dp, start = 20.dp)
        )

        PullRefreshLceWidget(
            state = groupOfStudentsState,
            onRefresh = component::onRefresh,
            onRetryClick = component::onRetryClick,
        ) { data: GroupStudentsList, _: Boolean ->
            val lazyListState = rememberLazyListState()
            lazyListState.TriggerLoadNext(
                pagedState = groupOfStudentsState,
                hasNextPage = groupOfStudentsState.data?.hasNextPage == true,
                callback = component::onLoadNext
            )
            if (data.users.isNotEmpty()) {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .fillMaxSize()
                ) {
                    items(data.users) { item ->
                        GroupItem(
                            { component.onDialogKickMember(KickType.Kick, item.id) },
                            item.name,
                        )
                    }
                    if (groupOfStudentsState.loadingStatus == PagedLoadingStatus.LoadingNextPage) {
                        item {
                            CircularProgressIndicator()
                        }
                    }
                }
            } else {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxSize()
                ) {
                    item {
                        Text(
                            text = stringResource(R.string.group_students_list_empty_teacher),
                            style = CustomTheme.typography.body.regular15,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GroupItem(
    onGroupItemClick: () -> Unit,
    name: String,
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
            modifier = Modifier
                .height(60.dp)
                .padding(end = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Group Icon",
                modifier = Modifier
                    .padding(start = 15.dp, end = 10.dp)
                    .size(27.dp)
            )

            Text(
                text = name,
                fontWeight = CustomTheme.typography.body.regular.fontWeight,
                fontSize = CustomTheme.typography.body.regular.fontSize,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .width(260.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                softWrap = false
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = { onGroupItemClick() },
                modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.rubbish_bin_icon),
                    contentDescription = "Member Delete",
                    modifier = Modifier.size(25.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewGroupItem() {
    AppTheme {
        GroupItem(
            {}, "Павел"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TeacherGroupDetailsUiPreview() {
    AppTheme {
        TeacherGroupDetailsUi(component = FakeTeacherGroupDetailsComponent())
    }
}