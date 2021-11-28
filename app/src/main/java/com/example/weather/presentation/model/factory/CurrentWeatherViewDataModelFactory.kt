package com.example.weather.presentation.model.factory

import com.example.weather.presentation.model.CurrentWeatherViewDataModel

fun createCurrentWeather() = CurrentWeatherViewDataModel(
    currentTime = "Sunday, 28 November",
    city = "HANOI",
    country = "VIETNAM",
    currentTemp = "24",
    humidity = "39%",
    wind = "6 km/h",
    visibility = "10 km",
    realFeel = "23º",
    currentIcon = "https://openweathermap.org/img/wn/04d@4x.png"
)