package com.example.core.data.repository

import com.example.core.domain.repository.PaymentConfig
import com.example.core.domain.model.PaymentIntentRequest
import com.example.core.domain.repository.PaymentRepositoryInterface
import com.example.core.network.service.PaymentApiService
import kotlinx.serialization.Serializable
import javax.inject.Inject


class PaymentRepository @Inject constructor(
    private val paymentApiService: PaymentApiService
) : PaymentRepositoryInterface {

    override suspend fun createPaymentIntent(amount: Double): Result<PaymentConfig> {
        return try {
            val amountInCents = (amount * 100).toInt()

            val response = paymentApiService.createPaymentIntent(
                PaymentIntentRequest(amountInCents)
            )

            val customerConfig = if (response.customerId != null && response.customerEphemeralKeySecret != null) {
                com.stripe.android.paymentsheet.PaymentSheet.CustomerConfiguration(
                    id = response.customerId!!,
                    ephemeralKeySecret = response.customerEphemeralKeySecret!!
                )
            } else null

            Result.success(
                PaymentConfig(
                    clientSecret = response.clientSecret,
                    customerConfig = customerConfig,
                    publishableKey = response.publishableKey
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}