package com.example.weather.domain.usecase

import kotlinx.coroutines.flow.Flow

abstract class UseCase<in Params, out T> where T : Any {

    abstract fun execute(params: Params? = null): Flow<T>

    // need call cancel any coroutine jobs
    fun onCleared() { }
}
