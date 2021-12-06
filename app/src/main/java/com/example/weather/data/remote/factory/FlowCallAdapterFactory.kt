package com.example.weather.data.remote.factory

import com.example.weather.data.remote.adapter.FlowCallAdapter
import com.example.weather.data.remote.mapper.ExceptionMapper
import kotlinx.coroutines.flow.Flow
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class FlowCallAdapterFactory constructor(
    private val exceptionMapper: ExceptionMapper
) : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // Check api service not use the Flow class
        if (getRawType(returnType) != Flow::class.java) {
            return null
        }

        check(returnType is ParameterizedType) { "Flow return type must be parameterized as Flow<Foo> or Flow<out Foo>" }
        val responseType = getParameterUpperBound(0, returnType)

        return FlowCallAdapter<Any>(retrofit, exceptionMapper, responseType)
    }

    companion object {
        @JvmStatic
        fun create(exceptionMapper: ExceptionMapper) = FlowCallAdapterFactory(exceptionMapper)
    }
}
