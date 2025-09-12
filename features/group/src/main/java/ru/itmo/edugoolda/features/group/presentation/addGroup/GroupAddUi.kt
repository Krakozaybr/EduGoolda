package ru.itmo.edugoolda.features.group.presentation.addGroup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
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
fun GroupAddUi(
    component: GroupAddComponent,
    modifier: Modifier = Modifier,
) {

    val isAddButtonEnabled by component.isAddButtonEnabled.collectAsState()
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
                onClick = { component.onCancelClick() },
                Modifier.padding(top = 3.dp + statusBarHeight, bottom = 6.dp, start = 5.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_left_white),
                    contentDescription = "Arrow Left",
                    tint = Color.Unspecified
                )
            }

            Text(
                text = stringResource(R.string.group_add_title),
                modifier = Modifier.padding(start = 30.dp, top = 3.dp + statusBarHeight, bottom = 6.dp),
                fontWeight = CustomTheme.typography.title.bold.fontWeight,
                fontSize = CustomTheme.typography.body.regular.fontSize,
                color = CustomTheme.colors.text.invert
            )
        }

        Spacer(modifier = Modifier.weight(0.2f))

        AppTextField(
            inputControl = component.codeInputControl,
            placeholder = stringResource(id = R.string.group_textfield_code),
            modifier = Modifier
                .padding(top = 40.dp, bottom = 20.dp)
                .width(200.dp)
                .align(Alignment.CenterHorizontally)
        )

        AppButton(
            onClick = { component.onAddClick() },
            text = stringResource(R.string.group_add_group_button),
            buttonType = ButtonType.Primary,
            modifier = Modifier
                .padding(bottom = 30.dp)
                .width(200.dp)
                .align(Alignment.CenterHorizontally),
            isEnabled = isAddButtonEnabled
        )

        Spacer(modifier = Modifier.weight(0.35f))
    }
}

@Preview(showBackground = true)
@Composable
private fun GroupAddUiPreview() {
    AppTheme {
        GroupAddUi(component = FakeGroupAddComponent())
    }
}