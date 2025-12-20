package com.example.core.navigation

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class Navigator(
    tabRoutes: Map<Int, Any>
) {
    private val backStacks: Map<Int, SnapshotStateList<Any>> = tabRoutes.mapValues { (_, route) ->
        mutableStateListOf(route)
    }

    private val onboardingRoute = tabRoutes[0]

    private val _selectedTab = mutableIntStateOf(0)
    val selectedTab: Int get() = _selectedTab.intValue

    private var _isNavigating = mutableStateOf(false)


    val currentDestination by derivedStateOf {
        getCurrentTabBackStack().lastOrNull()
    }

    val showBottomBar by derivedStateOf {
        if (_isNavigating.value) return@derivedStateOf false
        val stack = getCurrentTabBackStack()
        val destination = currentDestination

        val isAuthScreen = destination?.let {
            val name = it.toString()
            name.contains("Login", ignoreCase = true) ||
                    name.contains("SignUp", ignoreCase = true) ||
                    it == onboardingRoute
        } ?: false

        !isAuthScreen
    }

    fun getCurrentTabBackStack(): SnapshotStateList<Any> {
        return backStacks[_selectedTab.intValue] ?: backStacks.values.first()
    }

    fun selectTab(index: Int) {
        if (backStacks.containsKey(index) && index != _selectedTab.intValue) {
            _selectedTab.intValue = index
        }
    }

    fun navigateTo(destination: Any) {
        _isNavigating.value = true
        getCurrentTabBackStack().add(destination)
    }

    fun goBack(): Boolean {
        val currentStack = getCurrentTabBackStack()

        return if (currentStack.size > 1) {
            _isNavigating.value = true
            currentStack.removeLast()
            true
        } else if (_selectedTab.intValue != 1 && _selectedTab.intValue != 0) {
            _selectedTab.intValue = 1
            true
        } else {
            false
        }
    }

    fun canGoBack(): Boolean {
        val stack = getCurrentTabBackStack()
        val isNotAtRoot = stack.size > 1
        val canSwitchToHome = _selectedTab.intValue != 1 && _selectedTab.intValue != 0
        val result = isNotAtRoot || canSwitchToHome

        return result
    }

    fun startMainFlow() {
        selectTab(1)
        backStacks[0]?.clear()
        onboardingRoute?.let { backStacks[0]?.add(it) }
    }

    fun navigateAndClearBackStack(destination: Any) {
        _isNavigating.value = true
        getCurrentTabBackStack().apply {
            clear()
            add(destination)
        }
    }

    fun popUpTo(destination: Any, inclusive: Boolean = false) {
        _isNavigating.value = true
        val currentStack = getCurrentTabBackStack()
        val index = currentStack.indexOfLast { it == destination }
        if (index != -1) {
            val targetSize = if (inclusive) index else index + 1
            while (currentStack.size > targetSize) {
                currentStack.removeLast()
            }
        }
    }

    fun onNavigationComplete() {
        _isNavigating.value = false
    }
}

val LocalNavigator = compositionLocalOf<Navigator> {
    error("No Navigator provided")
}