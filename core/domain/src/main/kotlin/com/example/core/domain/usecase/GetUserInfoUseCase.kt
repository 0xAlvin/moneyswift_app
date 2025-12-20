package com.example.core.domain.usecase


import com.example.core.domain.model.User
import com.example.core.domain.repository.AuthRepoInterface
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val authRepository: AuthRepoInterface
) {
    operator fun invoke(): Flow<User?> {
        return authRepository.getCurrentUser()
    }
}