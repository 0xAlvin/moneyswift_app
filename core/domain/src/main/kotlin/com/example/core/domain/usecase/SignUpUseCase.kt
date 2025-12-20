package com.example.core.domain.usecase

import android.util.Patterns
import com.example.core.domain.model.User
import com.example.core.domain.model.Result
import com.example.core.domain.repository.AuthRepoInterface
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepoInterface: AuthRepoInterface
) : UseCase<SignUpUseCase.Params, User>() {

    public override suspend fun execute(parameters: Params): Result<User> {
        if (parameters.email.isEmpty()) {
            return Result.Error(
                message = "Email cannot be empty"
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(parameters.email).matches()) {
            return Result.Error(
                message = "Please enter a valid email address"
            )
        }
        if (parameters.password.length < 6) {
            return Result.Error(
                message = "Password must be at least 6 characters"
            )
        }

        return authRepoInterface.signUpWithEmail(
            email = parameters.email,
            password = parameters.password
        )
    }

    data class Params(
        val email: String,
        val password: String
    )
}
