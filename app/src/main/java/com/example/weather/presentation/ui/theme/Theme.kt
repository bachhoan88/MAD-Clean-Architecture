package com.example.weather.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightThemeColors = lightColors(
    primary = LightColor,
    primaryVariant = Purple700,
    onPrimary = LightColor,
    secondary = Teal200,
    error = Red800,
    onSecondary = Color.Black
)

private val DarkThemeColors = darkColors(
    primary = DarkColor,
    primaryVariant = Purple700,
    onPrimary = DarkColor,
    secondary = Red300,
    error = Red200,
    onSecondary = Color.White
)

@Composable
fun WeatherTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    MaterialTheme(
        colors = if (darkTheme) DarkThemeColors else LightThemeColors,
        typography = WeatherTypography,
        shapes = WeatherShapes,
        content = content
    )
}
