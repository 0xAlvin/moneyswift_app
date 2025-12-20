package com.example.core.domain.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@InternalSerializationApi
@Serializable
data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating? = null
)

@InternalSerializationApi
@Serializable
data class Rating(
    val rate: Double,
    val count: Int
)
