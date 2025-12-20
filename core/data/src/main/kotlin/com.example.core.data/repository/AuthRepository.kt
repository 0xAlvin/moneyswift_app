package com.example.core.data.repository

import com.example.core.domain.repository.AuthRepoInterface
import com.google.firebase.auth.FirebaseAuth
import com.example.core.domain.model.Result
import com.example.core.domain.model.User
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepoInterface {
    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): Result<User> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user

            if (user != null) {
                Result.Success(
                    User(
                        uid = user.uid,
                        email = user.email,
                        displayName = user.displayName
                    )
                )
            } else {
                Result.Error("Authentication failed")
            }
        } catch (e: FirebaseAuthException) {
            Result.Error(e.message ?: "Unknown Error Occurred")
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown Error Occurred")
        }
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String
    ): Result<User> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user

            if (firebaseUser != null) {
                Result.Success(
                    User(
                        uid = firebaseUser.uid,
                        email = firebaseUser.email,
                        displayName = firebaseUser.displayName
                    )
                )
            } else {
                Result.Error("Sign up failed")
            }
        } catch (e: FirebaseAuthException) {
            Result.Error(e.message ?: "Unknown error occurred")
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to sign out")
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.Success(Unit)
        } catch (e: FirebaseAuthException) {
            Result.Error(e.message ?: "Failed to send reset email")
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error occurred")
        }
    }

    override fun getCurrentUser(): Flow<User?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val firebaseUser = auth.currentUser
            trySend(
                firebaseUser?.let {
                    User(
                        uid = it.uid,
                        email = it.email,
                        displayName = it.displayName
                    )
                }
            )
        }

        firebaseAuth.addAuthStateListener(authStateListener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }


    override fun isUserAuthenticated(): Boolean {
        return firebaseAuth.currentUser != null
    }

}