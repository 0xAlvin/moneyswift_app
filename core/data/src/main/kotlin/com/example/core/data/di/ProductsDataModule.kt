package com.example.core.data.di

import com.example.core.data.repository.DefaultProductRepository
import com.example.core.domain.repository.ProductRepositoryInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindProductRepository(
        impl: DefaultProductRepository
    ): ProductRepositoryInterface
}