package ru.itmo.edugoolda.core.widget.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.itmo.edugoolda.core.theme.custom.CustomTheme

@Composable
fun FadingEdgeScrollableText(
    text: String,
    modifier: Modifier = Modifier,
    maxHeight: Dp = 200.dp,
    horizontalPadding: Dp = 16.dp,
) {
    val scrollState = rememberScrollState()

    val showTopFade by remember {
        derivedStateOf { scrollState.value > 0 }
    }
    val showBottomFade by remember {
        derivedStateOf { scrollState.value < scrollState.maxValue }
    }

    Box(
        modifier = modifier
            .heightIn(max = maxHeight)
            .fillMaxWidth()
    ) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = horizontalPadding),
            style = CustomTheme.typography.body.regular
        )

        val containerColor = MaterialTheme.colorScheme.surface

        if (showTopFade) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(containerColor, Color.Transparent)
                        )
                    )
            )
        }

        if (showBottomFade) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, containerColor)
                        )
                    )
            )
        }
    }
}
