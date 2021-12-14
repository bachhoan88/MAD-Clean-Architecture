package com.example.weather.domain.usecase.weather

import android.content.Context
import com.example.weather.R
import com.example.weather.data.model.Hourly
import com.example.weather.domain.asFlow
import com.example.weather.domain.exception.BaseException
import com.example.weather.domain.repository.WeatherRepository
import com.example.weather.domain.usecase.UseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

class GetHourlyWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
    @ApplicationContext private val context: Context
) : UseCase<GetHourlyWeatherUseCase.Params, GetHourlyWeatherUseCase.Response>() {

    override fun execute(params: Params?): Flow<Response> {
        if (params != null) {
            return weatherRepository
                .getHourlyWeather(params.lat, params.long)
                .map { it.drop(1) }
                .map { hourly ->
                    Response(
                        today = hourly.filter { it.dt <= maxToday() },
                        tomorrow = hourly.filter { it.dt > maxToday() && it.dt <= maxTomorrow() }
                    )
                }
        }

        return BaseException.AlertException(-1, context.getString(R.string.lat_lon_invalid)).asFlow()
    }

    private fun maxToday(): Long {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar.add(Calendar.DATE, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 6)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis / 1000L
    }

    private fun maxTomorrow(): Long {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar.add(Calendar.DATE, 2)
        calendar.set(Calendar.HOUR_OF_DAY, 6)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis / 1000L
    }

    data class Params(val lat: Double, val long: Double)

    data class Response(val today: List<Hourly>, val tomorrow: List<Hourly>)

    companion object {
        private const val MAX_WEATHERS_ON_DAY = 20
    }
}
