package com.example.feature.home.impl

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.ProductCard
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi

@OptIn(ExperimentalMaterial3Api::class, InternalSerializationApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    onProductClick: (Int) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val isLoading by homeViewModel.isLoading.collectAsStateWithLifecycle()
    val isUserLoading by homeViewModel.isUserLoading.collectAsStateWithLifecycle()
    val products by homeViewModel.productList.collectAsStateWithLifecycle()
    val currentUser by homeViewModel.currentUser.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val pullRefreshState = rememberPullToRefreshState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    if (!isUserLoading && !isLoading) {
                        Column {
                            Text(
                                text = if (currentUser != null) "Welcome back," else "Welcome to",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = currentUser?.email ?: "MoneySwift",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                actions = {
                    if (!isUserLoading) {
                        if (currentUser == null) {
                            TextButton(
                                onClick = onNavigateToLogin,
                                contentPadding = PaddingValues(horizontal = 12.dp),
                            ) {
                                Icon(Icons.Default.Person, null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Sign In")
                            }
                        } else {
                            IconButton(onClick = onNavigateToCart) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                            }
                            IconButton(onClick = onNavigateToSettings) {
                                Icon(Icons.Default.Settings, contentDescription = "Settings")
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    ) { paddingValues ->
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            isRefreshing = isLoading,
            onRefresh = { homeViewModel.loadProducts() },
            state = pullRefreshState
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Adaptive(160.dp),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (!isUserLoading) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        HomeBanner(isGuest = currentUser == null)
                    }
                }

                items(products, key = { it.product.id }) { item ->
                    ProductCard(
                        title = item.product.title,
                        price = item.product.price,
                        imageUrl = item.product.image,
                        rating = item.product.rating?.rate ?: 0.0,
                        ratingCount = item.product.rating?.count ?: 0,
                        isInCart = item.isInCart,
                        onProductClick = { onProductClick(item.product.id) },
                        onAddToCart = {
                            homeViewModel.quickAddToCart(item.product)
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                val result = snackbarHostState.showSnackbar(
                                    message = "Added ${item.product.title} to cart",
                                    actionLabel = "View Cart",
                                    duration = SnackbarDuration.Short
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    onNavigateToCart()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HomeBanner(isGuest: Boolean) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (isGuest) MaterialTheme.colorScheme.tertiaryContainer
            else MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isGuest) "New here?" else "Special Offer",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isGuest) MaterialTheme.colorScheme.onTertiaryContainer
                    else MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = if (isGuest) "Join us today for exclusive deals" else "50% Off Electronics",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isGuest) MaterialTheme.colorScheme.onTertiaryContainer
                    else MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Icon(
                imageVector = if (isGuest) Icons.Default.CardGiftcard else Icons.Default.LocalOffer,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = if (isGuest) MaterialTheme.colorScheme.onTertiaryContainer.copy(0.6f)
                else MaterialTheme.colorScheme.onPrimaryContainer.copy(0.6f)
            )
        }
    }
}