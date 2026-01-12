package com.example.moneyswift

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.core.navigation.LocalNavigator
import com.example.core.navigation.Navigator
import com.example.feature.cart.impl.CartViewModel
import com.example.feature.common.BottomNavItem
import com.example.moneyswift.di.EntryProviderInstaller
import android.content.pm.PackageManager
import android.os.Build
import com.example.ui.theme.MoneySwiftTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(
                this,
                "Can't post notifications without notifications permission",
                Toast.LENGTH_LONG,
            ).show()
        }
    }

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var entryProviders: Set<@JvmSuppressWildcards EntryProviderInstaller>

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        askNotificationPermission()

        setContent {
            MoneySwiftTheme {
                CompositionLocalProvider(LocalNavigator provides navigator) {
                    BottomNavigator(entryProviders = entryProviders)
                }
            }
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                return
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@Composable
fun BottomNavigator(
    entryProviders: Set<EntryProviderInstaller>,
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val cartItems by cartViewModel.cartItems.collectAsState(initial = emptyList())
    val cartItemCount = cartItems.size

    val cartItem = BottomNavItem.items.firstOrNull { it.title == "Cart" }
    val cartTabIndex = BottomNavItem.items.indexOfFirst { it.title == "Cart" } + 1

    LaunchedEffect(navigator.currentDestination) {
        navigator.onNavigationComplete()
    }

    BackHandler(enabled = navigator.canGoBack()) {
        navigator.goBack()
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = navigator.showBottomBar,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                Box {
                    NavigationBar {
                        BottomNavItem.items.forEachIndexed { index, item ->
                            val tabIndex = index + 1
                            val isCart = item.title == "Cart"

                            if (!isCart) {
                                NavigationBarItem(
                                    modifier = Modifier.height(50.dp),
                                    icon = {
                                        Icon(item.icon, contentDescription = item.title)
                                    },
                                    label = { Text(item.title) },
                                    selected = navigator.selectedTab == tabIndex,
                                    onClick = {
                                        if (navigator.selectedTab == tabIndex) {
                                            if (navigator.getCurrentTabBackStack().size > 1) {
                                                navigator.popUpTo(item.route, inclusive = false)
                                            }
                                        } else {
                                            navigator.selectTab(tabIndex)
                                        }
                                    }
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    cartItem?.let { item ->
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .offset(y = (-32).dp)
                                .size(64.dp)
                                .shadow(8.dp, CircleShape)
                                .clip(CircleShape)
                                .background(
                                    if (navigator.selectedTab == cartTabIndex)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.primaryContainer
                                )
                                .clickable {
                                    if (navigator.selectedTab == cartTabIndex) {
                                        if (navigator.getCurrentTabBackStack().size > 1) {
                                            navigator.popUpTo(item.route, inclusive = false)
                                        }
                                    } else {
                                        navigator.selectTab(cartTabIndex)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            BadgedBox(
                                badge = {
                                    if (cartItemCount > 0) {
                                        Badge(
                                            containerColor = MaterialTheme.colorScheme.error,
                                            contentColor = MaterialTheme.colorScheme.onError
                                        ) {
                                            Text(
                                                text = cartItemCount.toString(),
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    modifier = Modifier.size(28.dp),
                                    tint = if (navigator.selectedTab == cartTabIndex)
                                        MaterialTheme.colorScheme.onPrimary
                                    else
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            NavDisplay(
                backStack = navigator.getCurrentTabBackStack(),
                onBack = { navigator.goBack() },
                entryProvider = entryProvider {
                    entryProviders.forEach { installer ->
                        installer()
                    }
                },
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator()
                )
            )
        }
    }
}