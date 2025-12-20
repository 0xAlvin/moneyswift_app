package com.example.feature.auth.di

import com.example.core.domain.repository.AuthRepoInterface
import com.example.core.domain.usecase.LoginUseCase
import com.example.core.domain.usecase.ResetPasswordUseCase
import com.example.core.domain.usecase.SignOutUseCase
import com.example.core.domain.usecase.SignUpUseCase
import com.example.core.data.repository.AuthRepository
import com.example.core.domain.usecase.CheckAuthStatusUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepo(firebaseAuth: FirebaseAuth): AuthRepoInterface {
        return AuthRepository(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(authRepoInterface: AuthRepoInterface): LoginUseCase {
        return LoginUseCase(authRepoInterface)
    }

    @Provides
    @Singleton
    fun provideSignUpUseCase(authRepoInterface: AuthRepoInterface): SignUpUseCase {
        return SignUpUseCase(authRepoInterface)
    }

    @Provides
    @Singleton
    fun provideResetPasswordUseCase(authRepoInterface: AuthRepoInterface): ResetPasswordUseCase {
        return ResetPasswordUseCase(authRepoInterface)
    }

    @Provides
    @Singleton
    fun provideSignOutUseCase(authRepoInterface: AuthRepoInterface): SignOutUseCase {
        return SignOutUseCase(authRepoInterface)
    }

    @Provides
    @Singleton
    fun provideCheckAuthStatusUseCase(authRepoInterface: AuthRepoInterface): CheckAuthStatusUseCase {
        return CheckAuthStatusUseCase(authRepoInterface)
    }
}