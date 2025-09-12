package ru.itmo.edugoolda.core.widget.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.itmo.edugoolda.core.theme.AppTheme
import ru.itmo.edugoolda.core.theme.custom.CustomTheme

@Composable
fun LessonInfoTeacherListItem(
    name: String,
    createdAt: String,
    onLessonDetailsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier
        .padding(vertical = 6.dp, horizontal = 4.dp)
        .shadow(
            elevation = 4.dp,
            shape = RoundedCornerShape(8.dp),
            clip = true
        )
        .clip(RoundedCornerShape(8.dp))
        .background(CustomTheme.colors.background.backgroundPrimary)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 14.dp).padding(end = 16.dp, start = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.padding(end = 20.dp).weight(1f)) {
                Text(
                    text = name,
                    style = CustomTheme.typography.body.regular,
                    modifier = Modifier.basicMarquee()
                )
                Text(
                    text = createdAt,
                    style = CustomTheme.typography.caption.regular,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            IconButton(
                onClick = { onLessonDetailsClick() },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Apply"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StudentHeaderPreview() {
    AppTheme {
        LessonInfoTeacherListItem(
            name = "Математика 7Б",
            createdAt = "19:47 15.04.24",
            onLessonDetailsClick = {}
        )
    }
}
