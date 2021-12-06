package com.example.weather.data.remote.interceptor

import android.content.Context
import android.os.Build
import com.example.weather.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    @ApplicationContext private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        request = request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .addHeader("OS", "Android-${Build.VERSION.SDK_INT}")
            .addHeader("Version", BuildConfig.VERSION_NAME)
            .build()
        return chain.proceed(request)
    }
}
