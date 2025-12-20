package com.example.core.network.service

import com.example.core.domain.model.Product
import kotlinx.serialization.InternalSerializationApi
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApiService {
    @OptIn(InternalSerializationApi::class)
    @GET("products")
    suspend fun getProducts(): List<Product>

    @OptIn(InternalSerializationApi::class)
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") postId: Int): Product
}