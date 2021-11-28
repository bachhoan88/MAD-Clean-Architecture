package com.example.weather.data.remote.response

import com.example.weather.data.model.Current
import com.example.weather.data.model.Daily
import com.example.weather.data.model.Hourly
import com.google.gson.annotations.SerializedName

data class OneCallResponse(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("timezone_offset") val timezoneOffset: Int,
    @SerializedName("current") val current: Current,
    @SerializedName("hourly") val hourly: List<Hourly>?,
    @SerializedName("daily") val daily: List<Daily>?
)