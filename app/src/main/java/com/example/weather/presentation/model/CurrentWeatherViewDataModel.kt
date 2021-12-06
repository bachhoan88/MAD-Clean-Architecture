package com.example.weather.presentation.model

import android.content.Context
import androidx.compose.runtime.Immutable
import com.example.weather.R
import com.example.weather.data.Constants
import com.example.weather.data.model.CurrentWeather
import com.example.weather.domain.toCountryName
import com.example.weather.domain.toDateTimeString
import com.example.weather.presentation.base.ModelMapper
import com.example.weather.presentation.base.ViewDataModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.math.round

@Immutable
data class CurrentWeatherViewDataModel(
    val currentTime: String,
    val city: String,
    val country: String,
    val currentTemp: String,
    val humidity: String,
    val wind: String,
    val visibility: String,
    val realFeel: String,
    val currentIcon: String
) : ViewDataModel()

class CurrentWeatherMapper @Inject constructor(
    @ApplicationContext
    private val context: Context
) : ModelMapper<CurrentWeather, CurrentWeatherViewDataModel> {

    override fun mapperToViewDataModel(dataModel: CurrentWeather): CurrentWeatherViewDataModel {
        return CurrentWeatherViewDataModel(
            currentTime = (dataModel.dt * 1000L).toDateTimeString(Constants.DateFormat.EEEE_dd_MMMM, dataModel.timezone),
            city = dataModel.name.uppercase(),
            country = dataModel.sys.country.toCountryName().uppercase(),
            currentTemp = "${dataModel.main.temp.toInt()}",
            humidity = "${dataModel.main.humidity}${context.getString(R.string.percent)}",
            wind = "${round(dataModel.wind.speed).toInt()} ${context.getString(R.string.speed)}",
            visibility = "${dataModel.visibility / 1000} ${context.getString(R.string.km)}",
            realFeel = "${round(dataModel.main.feelsLike).toInt()}${context.getString(R.string.temp)}",
            currentIcon = String.format(Constants.OpenWeather.WEATHER_ICON_URL, dataModel.weatherItems.first().icon)
        )
    }
}
