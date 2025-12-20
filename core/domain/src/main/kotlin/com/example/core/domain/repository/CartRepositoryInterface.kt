package com.example.core.domain.repository

import com.example.core.domain.model.CartItem
import com.example.core.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.InternalSerializationApi


interface CartRepositoryInterface {
    fun getCartItems(): Flow<List<CartItem>>
    @OptIn(InternalSerializationApi::class)
    suspend fun addToCart(product: Product, quantity: Int)
    suspend fun removeFromCart(productId: Int)
    suspend fun updateQuantity(productId: Int, newQuantity: Int)
    suspend fun clearCart()
}