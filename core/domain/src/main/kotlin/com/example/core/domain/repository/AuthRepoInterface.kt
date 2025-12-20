package com.example.core.domain.repository



import com.example.core.domain.model.User
import com.example.core.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepoInterface {
    suspend fun signInWithEmail(email: String, password: String): Result<User>
    suspend fun signUpWithEmail(email: String, password: String): Result<User>
    suspend fun signOut(): Result<Unit>
    suspend fun resetPassword(email: String): Result<Unit>
    fun getCurrentUser(): Flow<User?>
    fun isUserAuthenticated(): Boolean
}