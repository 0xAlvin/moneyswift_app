package com.example.feature.auth.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object OnBoardingScreenRoute : NavKey

@Serializable
data object LogInScreenRoute : NavKey

@Serializable
data object SignUpScreenRoute : NavKey

@Serializable
data object ResetScreenRoute : NavKey
