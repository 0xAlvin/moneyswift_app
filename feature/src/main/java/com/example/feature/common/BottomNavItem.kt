package com.example.feature.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.feature.cart.api.CartScreenRoute
import com.example.feature.home.api.HomeScreenRoute
import com.example.feature.settings.api.SettingsScreenRoute

sealed class BottomNavItem(
    val route: Any,
    val title: String,
    val icon: ImageVector
) {
    data object Home : BottomNavItem(
        route = HomeScreenRoute,
        title = "Home",
        icon = Icons.Default.Home
    )

    data object Cart : BottomNavItem(
        route = CartScreenRoute,
        title = "Cart",
        icon = Icons.Default.ShoppingCart
    )

    data object Settings : BottomNavItem(
        route = SettingsScreenRoute,
        title = "Settings",
        icon = Icons.Default.Settings
    )

    companion object {
        val items = listOf(Home, Cart, Settings)
    }
}
