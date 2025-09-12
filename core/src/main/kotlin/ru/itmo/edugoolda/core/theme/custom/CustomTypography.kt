package ru.itmo.edugoolda.core.theme.custom

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

data class CustomTypography(
    val title: TitleTypography,
    val body: BodyTypography,
    val caption: CaptionTypography,
    val button: ButtonTypography
)

data class TitleTypography(
    val regular: TextStyle,
    val bold: TextStyle,
    val boldSmallerSize: TextStyle
)

data class BodyTypography(
    val regular: TextStyle,
    val regular13: TextStyle,
    val regular15: TextStyle,
    val regular18: TextStyle,
    val bold: TextStyle
)

data class CaptionTypography(
    val regular: TextStyle
)

data class ButtonTypography(
    val bold: TextStyle
)

val LocalCustomTypography = staticCompositionLocalOf<CustomTypography?> { null }
