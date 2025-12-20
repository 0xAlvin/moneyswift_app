package com.example.core.domain.model

sealed class Result<out T> {

    data class Success<out T>(val data: T): Result<T>()

    data class Error(
        val message: String,
        val exception: Throwable? = null
    ): Result<Nothing>()

    data object Loading: Result<Nothing>()

    val isSuccess: Boolean
        get() = (this is Success)

    val isError: Boolean
        get() = (this is Error)

    fun getDataOrNull(): T? {
        return when (this) {
            is Success -> data
            else ->  null
        }
    }

    fun getErrorMessageOrNull(): String? {
        return when (this) {
            is Error -> message
            else -> null
        }
    }
}

inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) {
        action(data)
    }
    return this
}

inline fun <T> Result<T>.onError(action: (String) -> Unit): Result<T> {
    if (this is Result.Error) {
        action(message)
    }
    return this
}