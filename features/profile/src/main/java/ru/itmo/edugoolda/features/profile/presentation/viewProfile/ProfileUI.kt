package ru.itmo.edugoolda.features.profile.presentation.viewProfile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.itmo.edugoolda.core.dialog.standard.StandardDialog
import ru.itmo.edugoolda.core.theme.AppTheme
import ru.itmo.edugoolda.core.theme.custom.CustomTheme
import ru.itmo.edugoolda.core.widget.PullRefreshLceWidget
import ru.itmo.edugoolda.data.user.api.Profile
import ru.itmo.edugoolda.data.user.api.UserRole
import ru.itmo.edugoolda.features.profile.R

@Composable
fun ProfileUI(
    component: ProfileComponent,
    modifier: Modifier = Modifier,
) {
    val profileState by component.profileState.collectAsState()
    StandardDialog(component.dialogLogout)

    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .height(50.dp + statusBarHeight)
                .background(CustomTheme.colors.content.contentActive)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.profile_top_bar_text),
                modifier = Modifier.padding(start = 20.dp, top = 3.dp + statusBarHeight, bottom = 6.dp),
                fontWeight = CustomTheme.typography.title.bold.fontWeight,
                fontSize = CustomTheme.typography.body.regular.fontSize,
                color = CustomTheme.colors.text.invert
            )
            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = { component.onDialogLogoutRequest() },
                modifier = Modifier
                    .padding(end = 20.dp, top = 3.dp + statusBarHeight, bottom = 6.dp)
                    .size(30.dp)

            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Logout",
                    modifier = Modifier.size(30.dp),
                    tint = CustomTheme.colors.icon.invert
                )
            }
        }

        PullRefreshLceWidget(
            state = profileState,
            onRefresh = component::onRefresh,
            onRetryClick = {
                component.onRetryClick()
            }
        ) { data: Profile, _: Boolean ->
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp)
            ) {

                Text(
                    text = data.name,
                    fontWeight = CustomTheme.typography.title.boldSmallerSize.fontWeight,
                    fontSize = CustomTheme.typography.title.boldSmallerSize.fontSize,
                    color = CustomTheme.colors.text.primary,
                    modifier = Modifier.width(270.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false
                )
                Row(
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    Text(
                        text = "${stringResource(R.string.profile_email_header)}:",
                        fontWeight = CustomTheme.typography.body.bold.fontWeight,
                        fontSize = CustomTheme.typography.body.bold.fontSize,
                        color = CustomTheme.colors.text.primary,
                    )
                    Text(
                        text = data.email,
                        fontWeight = CustomTheme.typography.body.regular.fontWeight,
                        fontSize = CustomTheme.typography.body.regular.fontSize,
                        color = CustomTheme.colors.text.primary,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Text(
                        text = "${stringResource(R.string.profile_role_header)}:",
                        fontWeight = CustomTheme.typography.body.bold.fontWeight,
                        fontSize = CustomTheme.typography.body.bold.fontSize,
                        color = CustomTheme.colors.text.primary,
                    )
                    Text(
                        text = when (data.role) {
                            UserRole.Teacher -> stringResource(R.string.profile_role_teacher)
                            UserRole.Student -> stringResource(R.string.profile_role_student)
                        },
                        fontWeight = CustomTheme.typography.body.regular.fontWeight,
                        fontSize = CustomTheme.typography.body.regular.fontSize,
                        color = CustomTheme.colors.text.primary,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileUIPreview() {
    AppTheme {
        ProfileUI(component = FakeProfileComponent())
    }
}