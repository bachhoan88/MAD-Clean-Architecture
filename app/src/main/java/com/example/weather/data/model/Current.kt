package com.example.weather.data.model

import com.example.weather.data.base.DataModel
import com.google.gson.annotations.SerializedName

data class Current(
    @SerializedName("dt") val dt: Long,
    @SerializedName("sunrise") val sunrise: Long,
    @SerializedName("sunset") val sunset: Long,
    @SerializedName("temp") val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("dew_point") val dewPoint: Double,
    @SerializedName("wind_speed") val windSpeed: Double,
    @SerializedName("wind_deg") val windDeg: Double,
    @SerializedName("wind_gust") val windGust: Double,
    @SerializedName("weather") val weather: List<WeatherItem>,
    @SerializedName("clouds") val clouds: Int,
    @SerializedName("pop") val pop: Int,
    @SerializedName("visibility") val visibility: Int,
    @SerializedName("uvi") val uvi: Double,
) : DataModel()