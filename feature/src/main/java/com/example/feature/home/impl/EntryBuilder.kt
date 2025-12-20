package com.example.feature.home.impl

import androidx.navigation3.runtime.EntryProviderScope
import com.example.core.navigation.Navigator
import com.example.feature.auth.api.LogInScreenRoute
import com.example.feature.cart.api.CartScreenRoute
import com.example.feature.home.api.HomeScreenRoute
import com.example.feature.home.api.ProductDetailsRoute
import com.example.feature.settings.api.SettingsScreenRoute
import kotlinx.serialization.InternalSerializationApi

@OptIn(InternalSerializationApi::class)
fun EntryProviderScope<Any>.homeEntries(
    navigator: Navigator
) {
    entry<HomeScreenRoute> {
        HomeScreen(
            onNavigateToLogin = {
                navigator.navigateAndClearBackStack(LogInScreenRoute)
            },
            onProductClick = {
                navigator.navigateTo(ProductDetailsRoute(productId = it))
            },
            onNavigateToCart = {
                navigator.navigateTo(CartScreenRoute)
            },
            onNavigateToSettings = {
                navigator.navigateTo(SettingsScreenRoute)
            },
        )
    }
    entry<ProductDetailsRoute> { route ->
        ProductDetailsScreen(
            productId = route.productId,
            onBackClick = {
                navigator.goBack()
            },
        )
    }
}