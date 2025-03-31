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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDropDownList(
    onExpandedChange: (Boolean) -> Unit, // {expanded = !expanded}
    onDismissRequest: (Boolean) -> Unit, // { expanded = false }
    onItemClick: (String, Boolean) -> Unit, // { selectedOption = option; expanded = false }
    onValueChange: (String) -> Unit, // {}
    isExpanded: Boolean,
    selectedOptionString: String,
    optionsList: List<String>,
    defaultTextFieldValue: String,
    itemColor: Color,
    itemTextColor: Color,
    selectedItemFieldColor: Color,
    selectedItemFieldTextColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(isExpanded) }
    var selectedOption by remember { mutableStateOf(selectedOptionString) }
    val options by remember { mutableStateOf(optionsList) }

    Column(modifier = Modifier.padding(24.dp)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { onExpandedChange(expanded) },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                shape = RectangleShape,
                value = selectedOption.ifEmpty { defaultTextFieldValue },
                onValueChange = { onValueChange(selectedOption) },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = selectedItemFieldColor,
                    unfocusedContainerColor = selectedItemFieldColor,
                    focusedTextColor = selectedItemFieldTextColor,
                    unfocusedTextColor = selectedItemFieldTextColor
                ),

                modifier = Modifier
                    .border(width = 1.dp, color = borderColor)
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                modifier = Modifier.background(itemColor),
                expanded = expanded,
                onDismissRequest = { onDismissRequest(expanded) }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        modifier = Modifier.background(itemColor),
                        colors = MenuDefaults.itemColors(textColor = itemTextColor),
                        text = { Text(option) },
                        onClick = { onItemClick(selectedOption, expanded) }

                    )
                }
            }
        }
    }
}