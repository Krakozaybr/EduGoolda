package ru.itmo.edugoolda.features.auth.presentation.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.itmo.edugoolda.core.theme.custom.CustomTheme
import ru.itmo.edugoolda.core.widget.button.AppButton
import ru.itmo.edugoolda.core.widget.button.ButtonType
import ru.itmo.edugoolda.core.widget.text_field.AppTextField
import ru.itmo.edugoolda.features.auth.R

@Composable
fun LoginUi(
    component: LoginComponent,
    modifier: Modifier = Modifier,
) {
    val isLoginProgress by component.isLoginProgress.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.systemBarsPadding()
    ) {

        Spacer(Modifier.weight(0.55f))

        Icon(
            painter = painterResource(R.drawable.app_icon),
            modifier = Modifier
                .padding(bottom = 25.dp)
                .size(110.dp)
                .clip(RoundedCornerShape(5.dp)),
            contentDescription = "App Icon",
            tint = CustomTheme.colors.content.contentActive
        )

        Text(
            text = stringResource(id = R.string.login_title),
            fontWeight = CustomTheme.typography.title.bold.fontWeight,
            fontSize = CustomTheme.typography.title.bold.fontSize,
            modifier = Modifier.padding(bottom = 60.dp)
        )

        AppTextField(
            inputControl = component.emailInputControl,
            placeholder = stringResource(id = R.string.login_email_hint),
            modifier = Modifier
                .padding(horizontal = 22.dp)
                .padding(bottom = 12.dp)
        )

        AppTextField(
            inputControl = component.passwordInputControl,
            placeholder = stringResource(id = R.string.login_password_hint),
            modifier = Modifier.padding(horizontal = 22.dp)
        )

        Spacer(modifier = Modifier.weight(0.45f))

        AppButton(
            text = stringResource(id = R.string.login_button_enter),
            buttonType = ButtonType.Primary,
            onClick = { component.onLoginClick() },
            isLoading = isLoginProgress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp)
                .padding(bottom = 12.dp)
        )

        AppButton(
            text = stringResource(id = R.string.login_button_register),
            buttonType = ButtonType.Secondary,
            onClick = { component.onRegisterRequestClick() },
            isEnabled = !isLoginProgress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp)
                .padding(bottom = 30.dp)
        )
    }
}
