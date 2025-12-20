package com.example.core.domain.usecase

import javax.inject.Inject
import android.util.Patterns
import com.example.core.domain.model.User
import com.example.core.domain.repository.AuthRepoInterface
import com.example.core.domain.model.Result

class LoginUseCase @Inject constructor(
    private val authRepoInterface: AuthRepoInterface
) : UseCase<LoginUseCase.Params, User>() {

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

        val response = authRepoInterface.signInWithEmail(
            email = parameters.email,
            password = parameters.password
        )

        return if (response is Result.Error) {
            Result.Error(message = response.message)
        } else {
            response
        }
    }

    data class Params(
        val email: String,
        val password: String
    )
}
