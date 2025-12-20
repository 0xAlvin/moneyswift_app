package com.example.feature.settings.impl


import android.util.Log
import androidx.navigation3.runtime.EntryProviderScope
import com.example.core.navigation.Navigator
import com.example.feature.auth.api.LogInScreenRoute
import com.example.feature.settings.api.SettingsScreenRoute

fun EntryProviderScope<Any>.settingsEntries(
    navigator: Navigator
) {
    entry<SettingsScreenRoute> {
        SettingsScreen(
            onSignOutSuccess = {
                navigator.selectTab(0)
                navigator.navigateAndClearBackStack(LogInScreenRoute)
            },
            onNavigateBack = {
                navigator.goBack()
                Log.e("BACKBUTTON","Clicked")
            }
        )
    }
}