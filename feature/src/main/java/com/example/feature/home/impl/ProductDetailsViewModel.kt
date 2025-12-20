package com.example.feature.home.impl

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.repository.CartRepository
import com.example.core.data.repository.DefaultProductRepository
import com.example.core.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productRepository: DefaultProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    @OptIn(InternalSerializationApi::class)
    private val _product = MutableStateFlow<Product?>(null)
    @OptIn(InternalSerializationApi::class)
    val product: StateFlow<Product?> = _product.asStateFlow()

    @OptIn(InternalSerializationApi::class)
    fun loadProductById(productId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("PRODUCT DETAILS", "Loading product with ID: $productId")
                val result = productRepository.getProductById(productId)
                if (result.isSuccess) {
                    _product.value = result.getOrNull()
                    Log.d("PRODUCT DETAILS", "Product loaded: ${_product.value?.title}")
                } else {
                    _product.value = null
                    Log.e("PRODUCT DETAILS", "Failed to load product: ${result.exceptionOrNull()}")
                }
            } catch (e: Exception) {
                _product.value = null
                Log.e("PRODUCT DETAILS", "Exception loading product", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    @OptIn(InternalSerializationApi::class)
    fun addToCart(product: Product, quantity: Int) {
        viewModelScope.launch {
            try {
                Log.d("PRODUCT DETAILS", "Adding to cart: ${product.title}, quantity: $quantity")
                cartRepository.addToCart(product, quantity)
                Log.d("PRODUCT DETAILS", "Successfully added to cart")
            } catch (e: Exception) {
                Log.e("PRODUCT DETAILS", "Failed to add to cart", e)
            }
        }
    }
}