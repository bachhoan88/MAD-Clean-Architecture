package com.example.weather.presentation.model

import android.content.Context
import com.example.weather.R
import com.example.weather.data.Constants
import com.example.weather.data.model.Hourly
import com.example.weather.domain.toDateTimeString
import com.example.weather.presentation.base.ModelMapper
import com.example.weather.presentation.base.ViewDataModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.math.round

data class HourlyWeatherViewDataModel(
    val dt: Long,
    val hour: String,
    val temp: String,
    val humidity: String,
    val wind: String,
    val visibility: String,
    val realFeel: String,
    val icon: String
) : ViewDataModel()

class HourlyWeatherMapper @Inject constructor(
    @ApplicationContext private val context: Context
) : ModelMapper<Hourly, HourlyWeatherViewDataModel> {

    override fun mapperToViewDataModel(dataModel: Hourly): HourlyWeatherViewDataModel {
        return HourlyWeatherViewDataModel(
            dt = dataModel.dt,
            hour = (dataModel.dt * 1000L).toDateTimeString(Constants.DateFormat.HH_mm, zone = null),
            temp = "${round(dataModel.temp).toInt()}",
            humidity = "${dataModel.humidity}${context.getString(R.string.percent)}",
            wind = "${round(dataModel.windSpeed).toInt()} ${context.getString(R.string.speed)}",
            visibility = "${dataModel.visibility / 1000} ${context.getString(R.string.km)}",
            realFeel = "${round(dataModel.feelsLike).toInt()}${context.getString(R.string.temp)}",
            icon = String.format(Constants.OpenWeather.WEATHER_SMALL_ICON_URL, dataModel.weather.first().icon)
        )
    }
}