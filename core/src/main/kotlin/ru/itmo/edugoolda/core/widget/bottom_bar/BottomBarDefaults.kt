package ru.itmo.edugoolda.core.widget.bottom_bar


import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.text.TextStyle
import ru.itmo.edugoolda.core.theme.custom.CustomTheme


@Immutable
object BottomBarDefaults {


    @Stable
    val textStyle: TextStyle
        @Composable
        get() = CustomTheme.typography.bottomBar.bold

    @Stable
    @Composable
    fun colors(): NavigationBarItemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = CustomTheme.colors.bottomBar.selectedIconColor,
        selectedTextColor = CustomTheme.colors.bottomBar.selectedTextColor,
        unselectedIconColor = CustomTheme.colors.bottomBar.unselectedIconColor,
        unselectedTextColor = CustomTheme.colors.bottomBar.unselectedTextColor,
        indicatorColor = CustomTheme.colors.bottomBar.indicatorColor,

        )


}
