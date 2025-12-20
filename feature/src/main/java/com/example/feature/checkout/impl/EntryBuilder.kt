package com.example.feature.checkout.impl

import androidx.navigation3.runtime.EntryProviderScope
import com.example.core.navigation.Navigator
import com.example.feature.checkout.api.CheckoutScreenRoute
import com.example.feature.home.api.HomeScreenRoute

fun EntryProviderScope<Any>.checkoutEntries(
    navigator: Navigator
) {
    entry<CheckoutScreenRoute> {
        CheckoutScreen(
            onNavigateBack = { navigator.goBack() },
            onContinueShopping = { navigator.navigateAndClearBackStack(HomeScreenRoute) }
        )
    }
}