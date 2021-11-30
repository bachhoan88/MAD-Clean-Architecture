package com.example.weather.domain

import com.example.weather.data.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.text.SimpleDateFormat
import java.util.*

/**
 * Creates a _cold_ flow that produces values from the given Throwable.
 */
fun <T> Throwable.asFlow(): Flow<T> {
    return flow {
        emit(suspendCancellableCoroutine { continuation ->
            continuation.cancel(this@asFlow)
        })
    }
}

/**
 * Creates a _cold_ flow that produces values from the given T type.
 */
fun <T> T.asFlow(): Flow<T> = flow {
    emit(this@asFlow)
}

fun Long.toDateTimeString(format: String, zone: TimeZone? = null): String {
    val date = Date().apply { time = this@toDateTimeString }
    return SimpleDateFormat(format, Locale.ENGLISH)
        .apply { zone?.let { timeZone = it } }
        .format(date)
}

fun Long.toDateTimeString(format: String, zoneId: Int? = null): String {
    val date = Date().apply { time = this@toDateTimeString }
    return try {
        SimpleDateFormat(format, Locale.ENGLISH)
            .apply { timeZone = TimeZone.getDefault().apply { zoneId?.let { rawOffset = it } } }
            .format(date)
    } catch (e: Exception) {
        SimpleDateFormat(Constants.DateFormat.DEFAULT_FORMAT, Locale.ENGLISH)
            .apply { timeZone = TimeZone.getDefault().apply { zoneId?.let { rawOffset = it } } }
            .format(date)
    }
}

fun String.toCountryName(): String {
    return Locale("", this).displayName
}