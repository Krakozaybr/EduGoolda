package ru.itmo.edugoolda.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import ru.itmo.edugoolda.core.theme.custom.CustomTheme
import ru.itmo.edugoolda.core.theme.custom.toMaterialColors
import ru.itmo.edugoolda.core.theme.custom.toMaterialTypography
import ru.itmo.edugoolda.core.theme.values.AppTypography
import ru.itmo.edugoolda.core.theme.values.DarkAppColors
import ru.itmo.edugoolda.core.theme.values.LightAppColors

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkAppColors else LightAppColors
    val typography = AppTypography

    CustomTheme(
        colors = colorScheme,
        typography = typography
    ) {
        MaterialTheme(
            colorScheme = colorScheme.toMaterialColors(),
            typography = typography.toMaterialTypography(),
            content = content
        )
    }
}