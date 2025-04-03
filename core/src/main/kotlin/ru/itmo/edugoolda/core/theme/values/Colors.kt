package ru.itmo.edugoolda.core.theme.values

import androidx.compose.ui.graphics.Color
import ru.itmo.edugoolda.core.theme.custom.BackgroundColors
import ru.itmo.edugoolda.core.theme.custom.BorderColors

import ru.itmo.edugoolda.core.theme.custom.ButtonColors
import ru.itmo.edugoolda.core.theme.custom.ContentColors
import ru.itmo.edugoolda.core.theme.custom.CustomColors
import ru.itmo.edugoolda.core.theme.custom.IconColors
import ru.itmo.edugoolda.core.theme.custom.TextColors
import ru.itmo.edugoolda.core.theme.custom.TextFieldColors

val LightAppColors = CustomColors(
    isLight = true,
    background = BackgroundColors(
        backgroundPrimary = Color(0xFFFFFFFF),
        screen = Color(0xFFFFFFFF),
        toast = Color(0xFF000000),
    ),
    content = ContentColors(
        contentActive = Color(0xFF007DBC),
        contentPrimary = Color(0xFF001018),
        contentTertiary = Color(0xFF687787),
    ),
    text = TextColors(
        primary = Color(0xFF000000),
        primaryDisabled = Color(0xFF000000).copy(alpha = 0.4f),
        secondary = Color(0xFF797979),
        secondaryDisabled = Color(0xFF797979).copy(alpha = 0.4f),
        invert = Color(0xFFFFFFFF),
        invertDisabled = Color(0xFFFFFFFF).copy(alpha = 0.4f),
        error = Color(0xFFB00020)
    ),
    icon = IconColors(
        primary = Color(0xFF000000),
        primaryDisabled = Color(0xFF000000).copy(alpha = 0.4f),
        secondary = Color(0xFF797979),
        invert = Color(0xFFFFFFFF),
        error = Color(0xFFB00020)
    ),
    button = ButtonColors(
        primary = Color(0xFF6750A4),
        primaryDisabled = Color(0xFF6750A4).copy(alpha = 0.4f),
        secondary = Color(0xFFFFFFFF),
        secondaryDisabled = Color(0xFFFFFFFF).copy(alpha = 0.4f)
    ),

    border = BorderColors(
        primary = Color(0xFF000000),
        error = Color(0xFFB00020)
    ),
    textField = TextFieldColors(
        background = Color(0xFFF2EBE3),
        backgroundDisabled = Color(0xFFF2EBE3).copy(alpha = 0.4f)
    )
)

val DarkAppColors = LightAppColors
