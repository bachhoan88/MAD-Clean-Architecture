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
import javax.inject.Inject

class GetHourlyWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
    @ApplicationContext private val context: Context
) : UseCase<GetHourlyWeatherUseCase.Params, List<Hourly>>() {

    override fun execute(params: Params?): Flow<List<Hourly>> {
        if (params != null) {
            return weatherRepository
                .getHourlyWeather(params.lat, params.long)
                .map { it.drop(1) }
                .map { it.take(MAX_WEATHERS_ON_DAY) }
        }

        return BaseException.AlertException(-1, context.getString(R.string.lat_lon_invalid)).asFlow()
    }

    data class Params(val lat: Double, val long: Double)

    companion object {
        private const val MAX_WEATHERS_ON_DAY = 20
    }
}
