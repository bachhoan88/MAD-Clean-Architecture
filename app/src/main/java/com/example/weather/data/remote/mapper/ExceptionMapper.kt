package com.example.weather.data.remote.mapper

import android.content.Context
import com.example.weather.R
import com.example.weather.data.remote.exception.RetrofitException
import com.example.weather.domain.exception.BaseException
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ExceptionMapper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun mapperToBaseException(throwable: RetrofitException): Throwable {

        return when (throwable.getKind()) {
            RetrofitException.Kind.NETWORK ->
                BaseException.SnackBarException(
                    code = -1,
                    message = context.getString(R.string.internet_connection_error)
                )

            RetrofitException.Kind.HTTP ->
                BaseException.AlertException(
                    code = throwable.getResponse()?.code() ?: -1,
                    title = String.format(context.getString(R.string.error_code_title), throwable.getResponse()?.code()),
                    message = String.format(
                        context.getString(R.string.url_invalid),
                        throwable.getRetrofit()?.baseUrl() ?: ""
                    )
                )

            RetrofitException.Kind.HTTP_422_WITH_DATA -> BaseException.AlertException(
                code = throwable.getErrorData()?.code ?: -1,
                title = String.format(context.getString(R.string.error_code_title), throwable.getErrorData()?.code),
                message = throwable.getErrorData()?.message ?: String.format(
                    context.getString(R.string.url_invalid),
                    throwable.getRetrofit()?.baseUrl() ?: ""
                )
            )

            else -> throwable
        }

    }
}