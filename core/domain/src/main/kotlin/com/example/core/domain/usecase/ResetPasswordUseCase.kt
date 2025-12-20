package com.example.core.domain.usecase

import android.util.Patterns
import com.example.core.domain.repository.AuthRepoInterface
import javax.inject.Inject
import com.example.core.domain.model.Result

class ResetPasswordUseCase @Inject constructor(
    private val authRepoInterface: AuthRepoInterface
) : UseCase<String, Unit>() {

    public override suspend fun execute(parameters: String): Result<Unit> {
        if (parameters.isEmpty()) {
            return Result.Error(
                message = "Email cannot be empty"
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(parameters).matches()) {
            return Result.Error(
                message = "Please enter a valid email address"
            )
        }

        return authRepoInterface.resetPassword(email = parameters)
    }
}