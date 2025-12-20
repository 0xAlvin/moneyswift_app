package com.example.core.data.repository

import com.example.core.database.dao.CartDao
import com.example.core.database.entities.CartItemEntity
import com.example.core.domain.model.CartItem
import com.example.core.domain.model.Product
import com.example.core.domain.repository.CartRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.InternalSerializationApi
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val cartDao: CartDao
) : CartRepositoryInterface {

    @OptIn(InternalSerializationApi::class)
    override fun getCartItems(): Flow<List<CartItem>> {
        return cartDao.getCartItems().map { entities ->
            entities.map { entity ->
                CartItem(
                    product = Product(
                        id = entity.productId,
                        title = entity.title,
                        price = entity.price,
                        image = entity.image,
                        description = "",
                        category = "",
                        rating = null
                    ),
                    quantity = entity.quantity
                )
            }
        }
    }

    @OptIn(InternalSerializationApi::class)
    override suspend fun addToCart(product: Product, quantity: Int) {
        val entity = CartItemEntity(
            productId = product.id,
            title = product.title,
            price = product.price,
            image = product.image,
            quantity = quantity
        )
        cartDao.insertOrUpdate(entity)
    }

    override suspend fun removeFromCart(productId: Int) {
        cartDao.deleteById(productId)
    }

    override suspend fun updateQuantity(productId: Int, newQuantity: Int) {
        if (newQuantity <= 0) {
            cartDao.deleteById(productId)
        } else {
            cartDao.updateQuantity(productId, newQuantity)
        }
    }

    override suspend fun clearCart() {
        cartDao.deleteAll()
    }
}
