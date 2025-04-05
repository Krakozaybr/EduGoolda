package ru.itmo.edugoolda.core.dialog.standard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import dev.icerock.moko.resources.compose.localized
import dev.icerock.moko.resources.desc.desc
import ru.itmo.edugoolda.core.theme.AppTheme
import ru.itmo.edugoolda.core.theme.custom.CustomTheme

@Composable
fun StandardDialog(dialogControl: StandardDialogControl) {
    val dismissableByUser by dialogControl.dismissableByUser.collectAsState()
    val slot by dialogControl.dialogSlot.collectAsState()

    slot.child?.let {
        val data = it.instance

        AlertDialog(
            onDismissRequest = { dialogControl.dismiss() },
            properties = DialogProperties(
                dismissOnBackPress = dismissableByUser,
                dismissOnClickOutside = dismissableByUser
            ),
            shape = RoundedCornerShape(4.dp),

            title = {
                Text(
                    text = data.title.localized(),
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp,
                        lineHeight = 20.sp
                    ),
                    color = CustomTheme.colors.text.primary
                )
            },

            text = if (data.message != null) {
                {
                    Text(
                        text = data.message.localized(),
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Normal,
                            fontSize = 15.sp,
                            lineHeight = 21.sp
                        ),
                        color = CustomTheme.colors.text.secondary
                    )
                }
            } else {
                null
            },

            confirmButton = {
                DialogButton(
                    text = data.confirmButton.text.localized(),
                    onClick = data.confirmButton.action,
                    onDismiss = dialogControl::dismiss
                )
            },

            dismissButton = data.dismissButton?.let { dismissButton ->
                {
                    DialogButton(
                        text = dismissButton.text.localized(),
                        onClick = dismissButton.action,
                        onDismiss = dialogControl::dismiss
                    )
                }
            }
        )
    }
}

@Composable
private fun DialogButton(
    text: String,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            lineHeight = 18.sp
        ),
        color = CustomTheme.colors.button.primary,
        textAlign = TextAlign.Center,
        modifier = modifier
            .clickable {
                onClick()
                onDismiss()
            }
            .padding(4.dp)
            .widthIn(min = 60.dp)
    )
}

@Preview(showSystemUi = false)
@Composable
private fun DialogWindow() {
    val dialogControl = remember {
        fakeStandardDialogControl(
            StandardDialogData(
                title = "Текст заголовка".desc(),
                message = "Вы уверены, что хотите удалить что-то там? Это действие приведет к необратимым последствиям и искревлению пространства времени. Этот маленький маневр обойдется нам в 52 года.".desc(),
                dismissButton = DialogButton(
                    text = "Нет".desc(),
                    action = {}
                ),
                confirmButton = DialogButton(
                    text = "Да".desc(),
                    action = {}
                ),
            )
        )
    }
    AppTheme {
        StandardDialog(dialogControl = dialogControl)
    }
}