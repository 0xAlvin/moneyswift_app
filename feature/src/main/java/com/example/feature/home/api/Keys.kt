package com.example.feature.home.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object HomeScreenRoute : NavKey

@Serializable
data class ProductDetailsRoute(val productId: Int)


