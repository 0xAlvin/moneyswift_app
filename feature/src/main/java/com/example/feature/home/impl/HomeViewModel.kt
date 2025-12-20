package com.example.feature.home.impl

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.model.Product
import com.example.core.domain.model.User
import com.example.core.domain.repository.CartRepositoryInterface
import com.example.core.domain.repository.ProductRepositoryInterface
import com.example.core.domain.usecase.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi
import javax.inject.Inject

data class ProductUiItem @OptIn(InternalSerializationApi::class) constructor(
    val product: Product,
    val isInCart: Boolean
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val repository: ProductRepositoryInterface,
    private val cartRepository: CartRepositoryInterface
) : ViewModel() {

    private val _isUserLoading = MutableStateFlow(true)
    val isUserLoading = _isUserLoading.asStateFlow()

    val currentUser: StateFlow<User?> = getUserInfoUseCase()
        .onEach { _isUserLoading.value = false }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    @OptIn(InternalSerializationApi::class)
    private val _rawProducts = MutableStateFlow<List<Product>>(emptyList())

    @OptIn(InternalSerializationApi::class)
    val productList: StateFlow<List<ProductUiItem>> = combine(
        _rawProducts,
        cartRepository.getCartItems()
    ) { products, cartItems ->
        val cartIds = cartItems.map { it.product.id }.toSet()
        products.map { ProductUiItem(it, cartIds.contains(it.id)) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        loadProducts()
    }

    @OptIn(InternalSerializationApi::class)
    fun loadProducts() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.getProducts()
                    .onSuccess { _rawProducts.value = it }
                    .onFailure { Log.e("HOME", "Error loading products", it) }
            } finally {
                _isLoading.value = false
            }
        }
    }

    @OptIn(InternalSerializationApi::class)
    fun quickAddToCart(product: Product) {
        viewModelScope.launch {
            cartRepository.addToCart(product, 1)
        }
    }
}