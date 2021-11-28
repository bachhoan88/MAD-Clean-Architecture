package com.example.weather.data.model

import com.example.weather.data.base.DataModel
import com.google.gson.annotations.SerializedName

data class Coord(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val long: Double
) : DataModel()