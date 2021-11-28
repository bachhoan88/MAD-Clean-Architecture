package com.example.weather.data.model

import com.example.weather.data.base.DataModel
import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("speed") val speed: Double,
    @SerializedName("deg") val deg: Double,
    @SerializedName("gust") val gust: Double
) : DataModel()
