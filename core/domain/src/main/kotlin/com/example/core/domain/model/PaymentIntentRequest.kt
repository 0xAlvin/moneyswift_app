package com.example.core.domain.model


import kotlinx.serialization.Serializable

@Serializable
data class PaymentIntentRequest(
    val amount: Int
)
