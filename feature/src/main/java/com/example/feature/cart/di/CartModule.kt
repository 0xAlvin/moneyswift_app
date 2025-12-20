package com.example.feature.cart.di

import com.example.core.data.repository.CartRepository
import com.example.core.domain.repository.CartRepositoryInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CartModule {

    @Binds
    @Singleton
    abstract fun bindCartRepository(
        impl: CartRepository
    ): CartRepositoryInterface
}