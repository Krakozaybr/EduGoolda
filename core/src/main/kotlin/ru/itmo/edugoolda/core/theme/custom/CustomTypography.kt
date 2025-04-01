package ru.itmo.edugoolda.core.theme.custom

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

data class CustomTypography(
    val title: TitleTypography,
    val body: BodyTypography,
    val caption: CaptionTypography,
    val button: ButtonTypography,
    val bottomBar : BottomBarTypography
)

data class TitleTypography(
    val regular: TextStyle
)

data class BodyTypography(
    val regular: TextStyle
)

data class CaptionTypography(
    val regular: TextStyle
)

data class ButtonTypography(
    val bold: TextStyle
)
data class BottomBarTypography(
    val bold: TextStyle
)


val LocalCustomTypography = staticCompositionLocalOf<CustomTypography?> { null }
