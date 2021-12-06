package com.example.weather.domain.model
import com.example.weather.domain.annotation.Redirect

data class Redirect(@Redirect val redirect: Int, val redirectObject: Any? = null)
