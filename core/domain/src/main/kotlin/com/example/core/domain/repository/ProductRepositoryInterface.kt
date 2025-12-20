package com.example.core.domain.repository

import com.example.core.domain.model.Product
import kotlinx.serialization.InternalSerializationApi

interface ProductRepositoryInterface {
    @OptIn(InternalSerializationApi::class)
    suspend fun getProducts(): Result<List<Product>>
    @OptIn(InternalSerializationApi::class)
    suspend fun getProductById(id: Int): Result<Product>
}