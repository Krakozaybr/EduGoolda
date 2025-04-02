package ru.itmo.edugoolda.core.theme.custom

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class CustomColors(
    val isLight: Boolean,
    val background: BackgroundColors,
    val text: TextColors,
    val icon: IconColors,
    val button: ButtonColors,
    val bottomBar: BottomBarColors,
    val border: BorderColors,
    val textField: TextFieldColors,
)

data class BackgroundColors(
    val screen: Color,
    val toast: Color,
)

data class TextColors(
    val primary: Color,
    val primaryDisabled: Color,
    val secondary: Color,
    val secondaryDisabled: Color,
    val invert: Color,
    val invertDisabled: Color,
    val error: Color
)

data class IconColors(
    val primary: Color,
    val primaryDisabled: Color,
    val secondary: Color,
    val invert: Color,
    val error: Color,
)

data class ButtonColors(
    val primary: Color,
    val primaryDisabled: Color,
    val secondary: Color,
    val secondaryDisabled: Color,
)

data class BottomBarColors(
    val selectedIconColor: Color,
    val selectedTextColor: Color,
    val unselectedIconColor: Color,
    val unselectedTextColor: Color,
    val indicatorColor: Color,
)

data class TextFieldColors(
    val background: Color,
    val backgroundDisabled: Color,
)

data class BorderColors(
    val primary: Color,
    val error: Color,
)

val LocalCustomColors = staticCompositionLocalOf<CustomColors?> { null }
