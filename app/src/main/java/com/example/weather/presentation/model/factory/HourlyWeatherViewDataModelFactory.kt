package com.example.weather.presentation.model.factory

import com.example.weather.presentation.model.HourlyWeatherViewDataModel

fun createHourlyWeather() = HourlyWeatherViewDataModel(
    dt = 1L,
    hour = "09:00",
    temp = "16",
    humidity = "43%",
    wind = "2 km/h",
    visibility = "10 Km",
    realFeel = "15º",
    icon = "https://openweathermap.org/img/wn/04d@4x.png"
)

fun createHourlyWeathers() = (10..30).map { hourly ->
    val currentHour = if (hourly > 23) hourly - 23 else hourly
    createHourlyWeather().copy(hour = String.format("%02d:00", currentHour))
}
