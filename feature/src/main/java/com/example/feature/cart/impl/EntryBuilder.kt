package com.example.feature.cart.impl

import androidx.navigation3.runtime.EntryProviderScope
import com.example.core.navigation.Navigator
import com.example.feature.cart.api.CartScreenRoute
import com.example.feature.checkout.api.CheckoutScreenRoute

fun EntryProviderScope<Any>.cartEntries(navigator: Navigator) {
    entry<CartScreenRoute> {
        CartScreen(
            onBackClick = { navigator.goBack() },
            onCheckout = {
                navigator.navigateTo(CheckoutScreenRoute)
            }
        )
    }
}