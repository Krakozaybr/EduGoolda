package ru.itmo.edugoolda.features.group.presentation.createGroup

import androidx.compose.foundation.background
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
import ru.itmo.edugoolda.core.theme.AppTheme
import ru.itmo.edugoolda.core.theme.custom.CustomTheme
import ru.itmo.edugoolda.core.widget.button.AppButton
import ru.itmo.edugoolda.core.widget.button.ButtonType
import ru.itmo.edugoolda.core.widget.text_field.AppTextField
import ru.itmo.edugoolda.features.group.R

@Composable
fun GroupCreateUi(
    component: GroupCreateComponent,
    modifier: Modifier = Modifier,
) {

    val isCreationButtonEnabled by component.isCreationButtonEnabled.collectAsState()
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Column(modifier = modifier.navigationBarsPadding().fillMaxSize()) {
        Row(
            modifier = Modifier
                .height(50.dp + statusBarHeight)
                .background(CustomTheme.colors.content.contentActive)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { component.onCancelClick() },
                Modifier.padding(top = statusBarHeight + 3.dp, bottom = 6.dp, start = 5.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_left_white),
                    contentDescription = "Arrow Left",
                    tint = Color.Unspecified
                )
            }

            Text(
                text = stringResource(R.string.group_creation_title),
                modifier = Modifier.padding(start = 30.dp, top = statusBarHeight + 3.dp, bottom = 6.dp),
                fontWeight = CustomTheme.typography.title.bold.fontWeight,
                fontSize = CustomTheme.typography.body.regular.fontSize,
                color = CustomTheme.colors.text.invert
            )
        }

        AppTextField(
            inputControl = component.nameInputControl,
            placeholder = stringResource(id = R.string.group_textfield_name),
            modifier = Modifier
                .padding(horizontal = 22.dp)
                .padding(top = 40.dp, bottom = 30.dp)
        )

        AppTextField(
            inputControl = component.descriptionInputControl,
            placeholder = stringResource(id = R.string.group_textfield_description),
            modifier = Modifier
                .padding(horizontal = 22.dp)
                .padding(bottom = 30.dp),
            minLines = 5,
            maxLines = 5
        )

        AppTextField(
            inputControl = component.subjectInputControl,
            placeholder = stringResource(id = R.string.group_textfield_subject),
            modifier = Modifier
                .padding(horizontal = 22.dp)
                .padding(bottom = 30.dp),
        )

        Spacer(modifier = Modifier.weight(1f))

        AppButton(
                onClick = { component.onCreateClick() },
                text = stringResource(R.string.group_create_button),
                buttonType = ButtonType.Primary,
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .padding(horizontal = 100.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                isEnabled = isCreationButtonEnabled,
            contentPadding = PaddingValues(vertical = 14.dp, horizontal = 18.dp)

        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GroupCreateUiPreview() {
    AppTheme {
        GroupCreateUi(component = FakeGroupCreateComponent())
    }
}