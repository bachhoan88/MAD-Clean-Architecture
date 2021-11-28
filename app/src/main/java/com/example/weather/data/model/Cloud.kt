package com.example.weather.data.model

import com.example.weather.data.base.DataModel
import com.google.gson.annotations.SerializedName

data class Cloud(
    @SerializedName("all") val all: Int
) : DataModel()