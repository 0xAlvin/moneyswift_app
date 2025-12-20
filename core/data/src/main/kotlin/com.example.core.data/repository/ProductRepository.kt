package com.example.core.data.repository

import com.example.core.domain.model.Product
import com.example.core.domain.repository.ProductRepositoryInterface
import com.example.core.network.service.ProductApiService
import kotlinx.serialization.InternalSerializationApi
import javax.inject.Inject

class DefaultProductRepository @Inject constructor(
    private val apiService: ProductApiService
) : ProductRepositoryInterface {

    @OptIn(InternalSerializationApi::class)
    override suspend fun getProducts(): Result<List<Product>> {
        return try {
            Result.success(apiService.getProducts())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    @OptIn(InternalSerializationApi::class)
    override suspend fun getProductById(id: Int): Result<Product> {

        return try {
            Result.success(apiService.getProductById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}