package com.example.feature.home.impl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.ProductDetails
import kotlinx.serialization.InternalSerializationApi

@OptIn(InternalSerializationApi::class)
@Composable
fun ProductDetailsScreen(
    productId: Int,
    modifier: Modifier = Modifier,
    productDetailsViewModel: ProductDetailsViewModel = hiltViewModel<ProductDetailsViewModel>(),
    onBackClick: () -> Unit,
) {
    val isLoading by productDetailsViewModel.isLoading.collectAsStateWithLifecycle()
    val product by productDetailsViewModel.product.collectAsStateWithLifecycle()

    LaunchedEffect(productId) {
        productDetailsViewModel.loadProductById(productId)
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            product?.let { currentProduct ->
                ProductDetails(
                    product = currentProduct,
                    onBackClick = onBackClick,
                    onAddToCart = { product, quantity ->
                        productDetailsViewModel.addToCart(product, quantity)
                    }
                )
            }
        }
    }
}