package com.example.weather.data.remote.adapter

import com.example.weather.data.remote.exception.RetrofitException
import com.example.weather.data.remote.mapper.ExceptionMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.*
import java.io.IOException
import java.lang.reflect.Type
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FlowCallAdapter<T>(
    private val retrofit: Retrofit,
    private val mapper: ExceptionMapper,
    private val responseType: Type
) : CallAdapter<T, Flow<T>> {
    override fun adapt(call: Call<T>): Flow<T> {
        return flow {
            emit(
                suspendCancellableCoroutine { continuation ->
                    call.enqueue(object : Callback<T> {
                        override fun onFailure(call: Call<T>, t: Throwable) {
                            continuation.resumeWithException(mapper.mapperToBaseException(asRetrofitException(t)))
                        }

                        override fun onResponse(call: Call<T>, response: Response<T>) {
                            try {
                                continuation.resume(response.body()!!)
                            } catch (e: Exception) {
                                continuation.resumeWithException(mapper.mapperToBaseException(asRetrofitException(e, response)))
                            }
                        }
                    })
                    continuation.invokeOnCancellation { call.cancel() }
                }
            )
        }
    }

    override fun responseType() = responseType

    private fun asRetrofitException(throwable: Throwable, res: Response<T>? = null): RetrofitException {
        // We had non-200 http error
        if (throwable is HttpException) {
            val response = throwable.response()

            return when (throwable.code()) {
                422 -> // on out api 422's get metadata in the response. Adjust logic here based on your needs
                    RetrofitException.httpErrorWithObject(
                        response?.raw()?.request?.url.toString(),
                        response,
                        retrofit
                    )
                else -> RetrofitException.httpError(
                    response?.raw()?.request?.url.toString(),
                    response,
                    retrofit
                )
            }
        }

        if (res != null) {
            return RetrofitException.httpErrorWithObject(
                res.raw().request.url.toString(),
                res,
                retrofit
            )
        }

        // A network error happened
        if (throwable is IOException) {
            return RetrofitException.networkError(throwable)
        }

        // We don't know what happened. We need to simply convert to an unknown error
        return RetrofitException.unexpectedError(throwable)
    }
}