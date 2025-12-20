package com.example.moneyswift.di

import androidx.navigation3.runtime.EntryProviderScope
import com.example.core.navigation.Navigator
import com.example.feature.auth.api.OnBoardingScreenRoute
import com.example.feature.auth.impl.authEntries
import com.example.feature.cart.api.CartScreenRoute
import com.example.feature.cart.impl.cartEntries
import com.example.feature.checkout.impl.checkoutEntries
import com.example.feature.home.api.HomeScreenRoute
import com.example.feature.home.impl.homeEntries
import com.example.feature.settings.api.SettingsScreenRoute
import com.example.feature.settings.impl.settingsEntries
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.multibindings.IntoSet

typealias EntryProviderInstaller = EntryProviderScope<Any>.() -> Unit

@Module
@InstallIn(ActivityRetainedComponent::class)
object NavigationModule {

    @Provides
    @ActivityRetainedScoped
    fun provideNavigator(): Navigator {
        return Navigator(
            tabRoutes = mapOf(
                0 to OnBoardingScreenRoute,
                1 to HomeScreenRoute,
                2 to CartScreenRoute,
                3 to SettingsScreenRoute
            )
        )
    }

    @Provides
    @IntoSet
    fun provideAuthEntries(navigator: Navigator): EntryProviderInstaller = {
        authEntries(navigator)
    }

    @Provides
    @IntoSet
    fun provideHomeEntries(navigator: Navigator): EntryProviderInstaller = {
        homeEntries(navigator)
    }

    @Provides
    @IntoSet
    fun provideCheckoutEntries(navigator: Navigator): EntryProviderInstaller = {
        checkoutEntries(navigator)
    }

    @Provides
    @IntoSet
    fun provideSettingsEntries(navigator: Navigator): EntryProviderInstaller = {
        settingsEntries(navigator)
    }

    @Provides
    @IntoSet
    fun provideCartEntries(navigator: Navigator): EntryProviderInstaller = {
        cartEntries(navigator)
    }
}