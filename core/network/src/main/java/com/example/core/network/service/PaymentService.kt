package com.example.core.network.service


import com.example.core.domain.model.PaymentIntentRequest
import com.example.core.domain.model.PaymentIntentResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentApiService {
    @POST("create-payment-intent")
    suspend fun createPaymentIntent(
        @Body request: PaymentIntentRequest
    ): PaymentIntentResponse
}