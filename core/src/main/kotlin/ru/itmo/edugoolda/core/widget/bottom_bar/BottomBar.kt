package ru.itmo.edugoolda.core.widget.bottom_bar

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itmo.edugoolda.core.R
import ru.itmo.edugoolda.core.theme.AppTheme
import ru.itmo.edugoolda.core.theme.custom.CustomTheme

data class BottomBarItem(
    val title: String,
    @DrawableRes val iconResId: Int
)

@Composable
fun CustomBottomBar(
    items: List<BottomBarItem>,
    selectedItemIndex: Int,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(CustomTheme.colors.background.backgroundPrimary)
                .selectableGroup(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items.forEachIndexed { index, item ->
                BottomBarItem(
                    item = item,
                    isSelected = index == selectedItemIndex,
                    onClick = { onItemClick(index) },
                    modifier = Modifier.weight(.1f)
                )
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    item: BottomBarItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Icon(
            painter = painterResource(id = item.iconResId),
            contentDescription = item.title,
            tint = if (isSelected) CustomTheme.colors.content.contentActive else CustomTheme.colors.content.contentTertiary,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = item.title,
            color = if (isSelected) CustomTheme.colors.content.contentPrimary else CustomTheme.colors.content.contentTertiary,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            style = CustomTheme.typography.caption.regular
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
private fun CustomBottomBarPreview() {
    AppTheme {
        val items = listOf(
            BottomBarItem("Home", R.drawable.ic_24_heart),
            BottomBarItem("Search", R.drawable.ic_24_heart),
            BottomBarItem("Profile", R.drawable.ic_24_heart),
            BottomBarItem("Settings", R.drawable.ic_24_heart)
        )
        var selectedItem by remember { mutableIntStateOf(0) }
        CustomBottomBar(
            items = items,
            selectedItemIndex = selectedItem,
            onItemClick = { index ->
                selectedItem = index
            },
            modifier = Modifier,
        )
    }
}
