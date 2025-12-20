package com.example.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PaymentIntentResponse(
    val clientSecret: String,
    val customerId: String? = null,
    val customerEphemeralKeySecret: String? = null,
    val publishableKey: String? = null
)