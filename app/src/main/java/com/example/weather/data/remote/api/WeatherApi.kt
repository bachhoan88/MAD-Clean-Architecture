package com.example.weather.data.remote.api

import com.example.weather.data.model.CurrentWeather
import com.example.weather.data.remote.response.OneCallResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    fun getCurrentWeather(
        @Query("q") city: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en",
        @Query("appid") appId: String
    ): Flow<CurrentWeather>

    @GET("onecall")
    fun getHourlyWeather(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("lang") lang: String = "en",
        @Query("units") units: String = "metric",
        @Query("exclude") exclude: String = "minutely,daily",
        @Query("appid") appId: String
    ): Flow<OneCallResponse>

    @GET("onecall")
    fun getDailyWeather(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("lang") lang: String = "en",
        @Query("units") units: String = "metric",
        @Query("exclude") exclude: String = "minutely,hourly",
        @Query("appid") appId: String
    ): Flow<OneCallResponse>
}
