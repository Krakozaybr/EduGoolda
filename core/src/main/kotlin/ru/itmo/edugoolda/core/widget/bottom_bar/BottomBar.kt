package ru.itmo.edugoolda.core.widget.bottom_bar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text

import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.res.vectorResource
import ru.itmo.edugoolda.core.R

import ru.itmo.edugoolda.core.widget.bottom_bar.BottomBarDefaults

data class BottomNavItem(
    val name: String,
    val icon: ImageVector
)

@Composable
fun BottomNavigationBar(
    onItemClick: (BottomNavItem) -> Unit,
    items: List<BottomNavItem>,
    colors: NavigationBarItemColors = BottomBarDefaults.colors(),
    modifier: Modifier = Modifier

) {
    var selectedItem by remember { mutableIntStateOf(0)  }

    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    onItemClick(item)
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.name
                    )
                },
                label = {
                    Text(
                        text = item.name,
//                        style = BottomBarDefaults.textStyle
                    )
                },
                alwaysShowLabel = true,
                colors = colors

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    val items = listOf(
        BottomNavItem(
            name = "Главная",
            icon = ImageVector.vectorResource(R.drawable.ic_24_heart)
        ),
        BottomNavItem(
            name = "Избранное",
            icon = ImageVector.vectorResource(R.drawable.ic_24_heart)
        ),
        BottomNavItem(
            name = "Профиль",
            icon = ImageVector.vectorResource(R.drawable.ic_24_heart)
        ),
        BottomNavItem(
            name = "Настройки",
            icon = ImageVector.vectorResource(R.drawable.ic_24_heart)
        )
    )

    BottomNavigationBar(
        items = items,
        onItemClick = { item ->

            println("Clicked on ${item.name}")
        }
    )
}