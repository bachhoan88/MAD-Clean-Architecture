package com.example.weather.presentation.base

import com.example.weather.domain.exception.BaseException

open class ViewState(
    open val isLoading: Boolean = false,
    open val exception: BaseException? = null
)

fun Throwable.toBaseException(): BaseException {
    return when (this) {
        is BaseException -> this
        else -> BaseException.AlertException(code = -1, message = this.message ?: "")
    }
}
