package com.example.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import com.example.core.domain.model.Result

abstract class UseCase<in P, out R> {
    protected abstract suspend fun execute(parameters: P): Result<R>
    suspend operator fun invoke(params: P): Result<R> {
        return execute(params)
    }
}

abstract class FlowUseCase<in P, out R> {
    protected abstract suspend fun execute(parameters: P): Flow<Result<R>>
    suspend operator fun invoke(params: P): Flow<Result<R>> {
        return execute(params)
    }
}