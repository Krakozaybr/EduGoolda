package ru.itmo.edugoolda.core.widget.join_requests

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
fun JoinRequestTeacherListItem(
    groupName: String,
    studentName: String,
    createAt: String,
    onAcceptClick: () -> Unit,
    onDeclineClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                Modifier
                    .padding(end = 30.dp)
                    .weight(1f)
            ) {

                Text(
                    text = studentName,
                    style = CustomTheme.typography.body.regular,
                    modifier = Modifier
                        .basicMarquee(),
                )
                Text(
                    text = groupName,
                    style = CustomTheme.typography.body.regular15,
                    modifier = Modifier
                        .padding(top = 7.dp, start = 6.dp)
                        .basicMarquee(),
                    maxLines = 1,
                    softWrap = false
                )
                Text(
                    text = createAt,
                    style = CustomTheme.typography.caption.regular,
                    modifier = Modifier
                        .padding(top = 5.dp, start = 6.dp)
                )
            }
            Row {
                IconButton(
                    onClick = onAcceptClick,
                    modifier = Modifier.padding(end = 10.dp).size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Apply",
                        modifier = Modifier.size(28.dp)
                    )
                }
                IconButton(
                    onClick = onDeclineClick,
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Deny",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StudentHeaderPreview() {
    AppTheme {
        JoinRequestTeacherListItem(
            groupName = "hhgghghhhhhhhhhhhhhghghghghghghghgh",
            studentName = "Иванов Василий",
            createAt = "15:30",
            onAcceptClick = { },
            onDeclineClick = { }
        )
    }
}
