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
import dev.icerock.moko.resources.desc.strResDesc
import ru.itmo.edugoolda.core.R
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
            shape = RoundedCornerShape(8.dp),

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
                    onClick = data.confirmButton.action,
                    onDismiss = dialogControl::dismiss,
                    text = data.confirmButton.text.localized()
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

@Composable
fun DialogButton(
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
) {
    Text(
        text = text,
        style = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            lineHeight = 18.sp
        ),
        color = when (isEnabled) {
            true -> CustomTheme.colors.button.primary
            false -> CustomTheme.colors.button.primaryDisabled
        },
        textAlign = TextAlign.Center,
        modifier = modifier
            .clickable(enabled = isEnabled) {
                onClick()
                onDismiss()
            }
            .padding(4.dp)
            .widthIn(min = 60.dp)
    )
}

@Preview(showSystemUi = false)
@Composable
private fun StandardDialogPreview() {
    val dialogControl = remember {
        fakeStandardDialogControl(
            StandardDialogData(
                title = R.string.common_retry.strResDesc(),
                message = R.string.error_no_internet_connection.strResDesc(),
                confirmButton = DialogButton(
                    text = R.string.common_retry.strResDesc(),
                    action = { TODO() }
                ),
                /*
                dismissButton = DialogButton(   // необязательный элемент, но там где нужно поставить - нужно поставить.
                    text = "".desc(),
                    action = {}
                )
                 */
            )
        )
    }

    AppTheme {
        StandardDialog(dialogControl = dialogControl)
    }
}