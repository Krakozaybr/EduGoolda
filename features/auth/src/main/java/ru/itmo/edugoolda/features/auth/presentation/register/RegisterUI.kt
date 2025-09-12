package ru.itmo.edugoolda.features.auth.presentation.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.itmo.edugoolda.core.theme.custom.CustomTheme
import ru.itmo.edugoolda.core.widget.button.AppButton
import ru.itmo.edugoolda.core.widget.button.ButtonType
import ru.itmo.edugoolda.core.widget.text_field.AppTextField
import ru.itmo.edugoolda.data.user.api.UserRole
import ru.itmo.edugoolda.features.auth.R

@Composable
fun RegisterUi(
    component: RegisterComponent,
    modifier: Modifier = Modifier,
) {
    val selectedRole by component.selectedRole.collectAsState()
    val isRegisterProgress by component.isRegisterProgress.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.systemBarsPadding()
    ) {

        IconButton(
            onClick = { component.onBackButtonClick() },
            enabled = !isRegisterProgress,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 7.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "ArrowBack"
            )
        }
        Spacer(Modifier.weight(0.15f))

        Text(
            text = stringResource(id = R.string.register_title),
            fontWeight = CustomTheme.typography.title.bold.fontWeight,
            fontSize = CustomTheme.typography.title.bold.fontSize,
        )

        Spacer(Modifier.weight(0.2f))

        AppTextField(
            placeholder = stringResource(id = R.string.register_email_header_hint),
            inputControl = component.emailInputControl,
            modifier = Modifier
                .padding(horizontal = 21.dp)
                .padding(bottom = 12.dp),
            onTextChanging = {
                component.emailInputControl.error.value = null
            }
        )

        AppTextField(
            placeholder = stringResource(id = R.string.register_password_hint),
            inputControl = component.passwordInputControl,
            modifier = Modifier
                .padding(horizontal = 21.dp)
                .padding(bottom = 12.dp)
        )

        AppTextField(
            placeholder = stringResource(id = R.string.register_username_header_hint),
            inputControl = component.userNameInputControl,
            modifier = Modifier
                .padding(horizontal = 21.dp)
                .padding(bottom = 19.dp)
        )

        Text(
            text = stringResource(id = R.string.register_title_role),
            fontWeight = CustomTheme.typography.title.bold.fontWeight,
            fontSize = CustomTheme.typography.title.bold.fontSize,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 21.dp)
        )

        Column(
            modifier = Modifier.padding(start = 8.dp),
        ) {
            UserRole.entries.forEach {
                RoleItem(
                    isSelected = it == selectedRole,
                    selectedRole = it,
                    onClick = { component.onUserRoleSelect(it) }
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.65f))

        AppButton(
            text = stringResource(id = R.string.register_button_register),
            buttonType = ButtonType.Primary,
            onClick = { component.onRegisterClick() },
            isLoading = isRegisterProgress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp)
                .padding(bottom = 30.dp)
        )
    }
}

@Composable
fun RoleItem(
    isSelected: Boolean,
    selectedRole: UserRole,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val text = stringResource(
        when (selectedRole) {
            UserRole.Teacher -> R.string.register_enum_string_teacher
            UserRole.Student -> R.string.register_enum_string_student
        }
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
        )
        Text(text = text,
            modifier = Modifier.clickable {
                onClick()
            })
    }
}