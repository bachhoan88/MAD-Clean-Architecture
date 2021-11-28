package com.example.weather.domain.usecase.weather

import android.content.Context
import com.example.weather.R
import com.example.weather.data.model.CurrentWeather
import com.example.weather.domain.asFlow
import com.example.weather.domain.exception.BaseException
import com.example.weather.domain.repository.WeatherRepository
import com.example.weather.domain.usecase.UseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentWeatherByCityUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
    @ApplicationContext private val context: Context
) : UseCase<GetCurrentWeatherByCityUseCase.Params, CurrentWeather>() {

    override fun execute(params: Params?): Flow<CurrentWeather> {
        if (params?.city?.isNotEmpty() == true) {
            return weatherRepository.getCurrentWeather(params.city)
        }

        return BaseException.AlertException(-1, context.getString(R.string.city_input_invalid)).asFlow()
    }

    data class Params(val city: String)
}
