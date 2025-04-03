package ru.itmo.edugoolda.core.widget.text_field

import androidx.compose.foundation.border
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import ru.itmo.edugoolda.core.theme.custom.CustomTheme

@Composable
fun AppOutlinedTextField(
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    defaultValue: String? = null,
) {
    OutlinedTextField(
        shape = RectangleShape,
        value = defaultValue ?: "",
        onValueChange = { text -> onTextChange(text) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = CustomTheme.colors.background.screen,
            unfocusedContainerColor = CustomTheme.colors.background.screen,
            focusedTextColor = CustomTheme.colors.text.primary,
            unfocusedTextColor = CustomTheme.colors.text.primary
        ),

        modifier = modifier
            .border(width = 1.dp, color = CustomTheme.colors.border.outlinedTextField)
    )
}