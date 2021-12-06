package com.example.weather.domain.repository

import com.example.weather.data.model.CurrentWeather
import com.example.weather.data.model.Daily
import com.example.weather.data.model.Hourly
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getCurrentWeather(city: String): Flow<CurrentWeather>

    fun getHourlyWeather(lat: Double, lon: Double): Flow<List<Hourly>>

    fun getDailyWeather(lat: Double, lon: Double): Flow<List<Daily>>
}
