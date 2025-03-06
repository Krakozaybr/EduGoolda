package ru.itmo.edugoolda.core.widget.checkbox

import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import ru.itmo.edugoolda.core.theme.custom.CustomTheme

@Immutable
object AppCheckboxDefaults {

    @Stable
    val colors: CheckboxColors
        @Composable
        get() = CheckboxDefaults.colors(
            checkedColor = CustomTheme.colors.button.primary,
            uncheckedColor = CustomTheme.colors.border.primary,
            disabledCheckedColor = CustomTheme.colors.button.primaryDisabled,
            disabledUncheckedColor = Color.Transparent,
            checkmarkColor = Color.White,
        )
}
