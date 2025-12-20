package com.example.core.domain.model
import kotlinx.serialization.InternalSerializationApi

data class CartItem @OptIn(InternalSerializationApi::class) constructor(
    val product: Product,
    val quantity: Int
) {
    @OptIn(InternalSerializationApi::class)
    val totalPrice: Double get() = product.price * quantity
}