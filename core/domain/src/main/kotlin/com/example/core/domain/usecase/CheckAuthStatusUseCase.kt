package com.example.core.domain.usecase

import com.example.core.domain.repository.AuthRepoInterface
import javax.inject.Inject

class CheckAuthStatusUseCase @Inject constructor(
    private val authRepoInterface: AuthRepoInterface
) {
    operator fun invoke(): Boolean {
        return authRepoInterface.isUserAuthenticated()
    }
}