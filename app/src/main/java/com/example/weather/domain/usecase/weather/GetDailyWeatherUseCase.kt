package com.example.weather.domain.usecase.weather

import android.content.Context
import com.example.weather.R
import com.example.weather.data.model.Daily
import com.example.weather.data.model.Hourly
import com.example.weather.domain.asFlow
import com.example.weather.domain.exception.BaseException
import com.example.weather.domain.repository.WeatherRepository
import com.example.weather.domain.usecase.UseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDailyWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
    @ApplicationContext private val context: Context
) : UseCase<GetDailyWeatherUseCase.Params, List<Daily>>() {

    override fun execute(params: Params?): Flow<List<Daily>> {
        if (params != null) {
            return weatherRepository.getDailyWeather(params.lat, params.long)
        }

        return BaseException.AlertException(-1, context.getString(R.string.lat_lon_invalid)).asFlow()
    }

    data class Params(val lat: Double, val long: Double)
}
