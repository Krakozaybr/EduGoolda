package ru.itmo.edugoolda.core.theme.values

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.itmo.edugoolda.core.theme.custom.BodyTypography
import ru.itmo.edugoolda.core.theme.custom.ButtonTypography
import ru.itmo.edugoolda.core.theme.custom.CaptionTypography
import ru.itmo.edugoolda.core.theme.custom.CustomTypography
import ru.itmo.edugoolda.core.theme.custom.TitleTypography

val AppTypography = CustomTypography(
    title = TitleTypography(
        regular = TextStyle(
            fontSize = 24.sp
        ),
        bold = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        ),
        boldSmallerSize = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        ),
    ),
    body = BodyTypography(
        regular = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        ),
        regular13 = TextStyle(
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal
        ),
        regular15 = TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal
        ),
        regular18 = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal
        ),
        bold = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        ),
    ),
    caption = CaptionTypography(
        regular = TextStyle(
            fontSize = 12.sp
        )
    ),
    button = ButtonTypography(
        bold = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    )
)