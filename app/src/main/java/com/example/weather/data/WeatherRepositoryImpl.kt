package com.example.weather.data

import android.content.Context
import com.example.weather.R
import com.example.weather.data.local.pref.PrefsHelper
import com.example.weather.data.model.CurrentWeather
import com.example.weather.data.model.Daily
import com.example.weather.data.model.Hourly
import com.example.weather.data.remote.api.WeatherApi
import com.example.weather.domain.repository.WeatherRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val weatherApi: WeatherApi,
    private val prefsHelper: PrefsHelper
) : WeatherRepository {

    override fun getCurrentWeather(city: String): Flow<CurrentWeather> {
        return weatherApi.getCurrentWeather(city = city, appId = context.getString(R.string.weather_app_id))
            .map { weather ->
                prefsHelper.saveLastCity(city)
                weather
            }
    }

    override fun getHourlyWeather(lat: Double, lon: Double): Flow<List<Hourly>> {
        return weatherApi.getHourlyWeather(lat = lat, long = lon, appId = context.getString(R.string.weather_app_id))
            .map { response ->
                response.hourly ?: emptyList()
            }
    }

    override fun getDailyWeather(lat: Double, lon: Double): Flow<List<Daily>> {
        return weatherApi.getHourlyWeather(lat = lat, long = lon, appId = context.getString(R.string.weather_app_id))
            .map { response ->
                response.daily ?: emptyList()
            }
    }
}
