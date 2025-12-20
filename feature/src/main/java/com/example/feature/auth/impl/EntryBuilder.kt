package com.example.feature.auth.impl

import androidx.navigation3.runtime.EntryProviderScope
import com.example.core.navigation.Navigator
import com.example.feature.auth.api.LogInScreenRoute
import com.example.feature.auth.api.OnBoardingScreenRoute
import com.example.feature.auth.api.ResetScreenRoute
import com.example.feature.auth.api.SignUpScreenRoute
import com.example.feature.auth.impl.login.LoginScreen
import com.example.feature.auth.impl.onboarding.OnboardingScreen
import com.example.feature.auth.impl.reset.ResetScreen
import com.example.feature.auth.impl.signup.SignUpScreen

fun EntryProviderScope<Any>.authEntries(
    navigator: Navigator
) {
    entry<OnBoardingScreenRoute> {
        OnboardingScreen(
            onFinish = {
                navigator.startMainFlow()
            },
            onSignIn = {
                navigator.navigateTo(LogInScreenRoute)
            },
            onAuthenticated = { navigator.startMainFlow() },
        )
    }

    entry<LogInScreenRoute> {
        LoginScreen(
            onSuccess = {
                navigator.startMainFlow()
            },
            onSignUp = {
                navigator.navigateTo(SignUpScreenRoute)
            },
            onForgotPassword = {
                navigator.navigateTo(ResetScreenRoute)
            }
        )
    }

    entry<SignUpScreenRoute> {
        SignUpScreen(
            onSuccess = {
                navigator.startMainFlow()
            },
            onSignIn = {
                navigator.goBack()
            }
        )
    }

    entry<ResetScreenRoute> {
        ResetScreen(
            onBackToSignIn = {
                navigator.goBack()
            }
        )
    }
}