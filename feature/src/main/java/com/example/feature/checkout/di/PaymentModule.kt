package com.example.feature.checkout.di

import android.content.Context
import com.example.core.data.repository.PaymentRepository
import com.example.core.domain.repository.PaymentRepositoryInterface
import com.stripe.android.PaymentConfiguration
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PaymentModule {

    @Binds
    @Singleton
    abstract fun bindPaymentRepository(
        impl: PaymentRepository
    ): PaymentRepositoryInterface

    companion object {
        @Provides
        @Singleton
        fun providePaymentConfiguration(
            @ApplicationContext context: Context
        ): PaymentConfiguration {
            val publishableKey = "pk_test_51Sbt3V9QaYTwcxUIaOZitN0YWzmi5UHimWOIr8bVQlbsqMfgKRiDfYwSLasw0Q4y7At3GqpOhzp2o8Ti3LwzJmlL00a6nyNoK1pk_test_51Sbt3V9QaYTwcxUIaOZitN0YWzmi5UHimWOIr8bVQlbsqMfgKRiDfYwSLasw0Q4y7At3GqpOhzp2o8Ti3LwzJmlL00a6nyNoK1"
            PaymentConfiguration.init(context, publishableKey)
            return PaymentConfiguration.getInstance(context)
        }

        @Provides
        @Singleton
        fun provideStripePublishableKey(
            paymentConfiguration: PaymentConfiguration
        ): String {
            return paymentConfiguration.publishableKey
        }
    }
}
