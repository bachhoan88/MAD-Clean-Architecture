package com.example.weather.domain.exception

import com.example.weather.domain.annotation.ExceptionType
import com.example.weather.domain.model.Dialog
import com.example.weather.domain.model.Redirect
import com.example.weather.domain.model.Tag

sealed class BaseException(
    open val code: Int,
    @ExceptionType val type: Int,
    override val message: String?,
    val hashCode: String? = "${System.nanoTime()}"
) : Throwable(message) {

    data class AlertException(
        override val code: Int,
        override val message: String,
        val title: String? = null
    ) : BaseException(code, ExceptionType.ALERT, message)

    data class InlineException(
        override val code: Int,
        val tags: List<Tag>
    ) : BaseException(code, ExceptionType.INLINE, null)

    data class RedirectException(
        override val code: Int,
        val redirect: Redirect
    ) : BaseException(code, ExceptionType.REDIRECT, null)

    data class SnackBarException(
        override val code: Int,
        override val message: String
    ) : BaseException(code, ExceptionType.SNACK, message)

    data class ToastException(
        override val code: Int,
        override val message: String
    ) : BaseException(code, ExceptionType.TOAST, message)

    data class DialogException(
        override val code: Int,
        val dialog: Dialog
    ) : BaseException(code, ExceptionType.DIALOG, null)

    data class OnPageException(
        override val code: Int,
        override val message: String
    ) : BaseException(code, ExceptionType.ON_PAGE, message)
}
