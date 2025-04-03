package ru.itmo.edugoolda.core.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import ru.itmo.edugoolda.core.theme.custom.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDropDownList(
    onItemClick: (String) -> Unit, // { selectedOption = option; expanded = false }
    options: List<String>,
    modifier: Modifier = Modifier,
    selectedOption: String? = null,
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.padding(24.dp)) {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                shape = RectangleShape,
                value = selectedOption ?: "",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = CustomTheme.colors.background.screen,
                    unfocusedContainerColor = CustomTheme.colors.background.screen,
                    focusedTextColor = CustomTheme.colors.text.primary,
                    unfocusedTextColor = CustomTheme.colors.text.primary
                ),

                modifier = Modifier
                    .border(width = 1.dp, color = CustomTheme.colors.border.outlinedTextField)
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                modifier = Modifier.background(CustomTheme.colors.background.screen),
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        modifier = Modifier.background(CustomTheme.colors.background.screen),
                        colors = MenuDefaults.itemColors(textColor = CustomTheme.colors.text.primary),
                        text = {
                            Text(
                                text = option,
                                fontStyle = CustomTheme.typography.body.regular.fontStyle,
                                color = CustomTheme.colors.text.primary
                            )
                        },
                        onClick = { onItemClick(option) }

                    )
                }
            }
        }
    }
}