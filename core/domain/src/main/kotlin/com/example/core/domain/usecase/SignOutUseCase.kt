package com.example.core.domain.usecase


import com.example.core.domain.repository.AuthRepoInterface
import com.example.core.domain.model.Result
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepoInterface: AuthRepoInterface
) : UseCase<Unit, Unit>() {

    override suspend fun execute(parameters: Unit): Result<Unit> {
        return authRepoInterface.signOut()
    }
}