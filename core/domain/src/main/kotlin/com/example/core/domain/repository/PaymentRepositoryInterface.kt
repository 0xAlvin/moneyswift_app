package com.example.core.domain.repository


import com.stripe.android.paymentsheet.PaymentSheet

data class PaymentConfig(
    val clientSecret: String,
    val customerConfig: PaymentSheet.CustomerConfiguration? = null,
    val publishableKey: String? = null
)

interface PaymentRepositoryInterface {
    suspend fun createPaymentIntent(amount: Double): Result<PaymentConfig>
}