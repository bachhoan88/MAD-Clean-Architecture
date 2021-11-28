package com.example.weather.presentation.ui

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.weather.presentation.ui.day.SevenDaysScreen
import com.example.weather.presentation.ui.day.SevenDaysViewModel
import com.example.weather.presentation.ui.home.HomeScreen
import com.example.weather.presentation.ui.home.HomeViewModel

@Composable
fun WeatherApp(appState: WeatherAppState = rememberWeatherAppState()) {
    NavHost(
        navController = appState.controller,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = hiltViewModel(),
                navigateToNextSevenDay = { lat, long ->
                    appState.navigateToSevenDays(lat, long)
                }
            )
        }

        composable(
            Screen.NextSevenDays.route,
            arguments = listOf(
                navArgument("lat") { type = NavType.FloatType },
                navArgument("long") { type = NavType.FloatType }
            )
        ) { entry ->
            SevenDaysScreen(
                viewModel = hiltViewModel(),
                onBackPressed = appState::navigateBack,
                lat = entry.arguments?.getFloat("lat")?.toDouble() ?: 0.0,
                long = entry.arguments?.getFloat("long")?.toDouble() ?: 0.0
            )
        }
    }
}