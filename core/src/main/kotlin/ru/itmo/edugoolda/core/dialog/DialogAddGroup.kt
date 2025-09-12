package ru.itmo.edugoolda.core.dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import dev.icerock.moko.resources.compose.localized
import ru.itmo.edugoolda.core.dialog.standard.DialogButton
import ru.itmo.edugoolda.core.dialog.standard.StandardDialogControl
import ru.itmo.edugoolda.core.theme.custom.CustomTheme
import ru.itmo.edugoolda.core.widget.text_field.AppTextField
import ru.mobileup.kmm_form_validation.control.InputControl

@Composable
fun DialogAddGroup(
    dialogControl: StandardDialogControl,
    inputControl: InputControl,
) {
    val dismissableByUser by dialogControl.dismissableByUser.collectAsState()
    val slot by dialogControl.dialogSlot.collectAsState()
    val codeText by inputControl.text.collectAsState()

    slot.child?.let {
        val data = it.instance

        AlertDialog(
            onDismissRequest = { dialogControl.dismiss() },
            properties = DialogProperties(
                dismissOnBackPress = dismissableByUser,
                dismissOnClickOutside = dismissableByUser
            ),
            shape = RoundedCornerShape(8.dp),

            title = {
                Text(
                    text = data.title.localized(),
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Medium,
                        fontSize = 17.sp,
                        lineHeight = 20.sp
                    ),
                    color = CustomTheme.colors.text.primary
                )
            },

            text = if (data.message != null) {
                {
                    AppTextField(
                        inputControl,
                        placeholder = data.message.localized(),
                        modifier = Modifier.padding(vertical = 2.dp),
                        onTextChanging = {
                            if (inputControl.error.value != null) {
                                inputControl.error.value = null
                            }
                        }
                    )
                }
            } else {
                null
            },

            confirmButton = {
                DialogButton(
                    onClick = data.confirmButton.action,
                    onDismiss = {},
                    text = data.confirmButton.text.localized(),
                    isEnabled = codeText.isNotBlank(),
                )
            },

            dismissButton = data.dismissButton?.let { dismissButton ->
                {
                    DialogButton(
                        onClick = dismissButton.action,
                        onDismiss = dialogControl::dismiss,
                        text = dismissButton.text.localized()
                    )
                }
            }
        )
    }
}