package com.example.weather.presentation.ui

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.weather.presentation.ui.day.SevenDaysScreen
import com.example.weather.presentation.ui.home.HomeScreen

@Composable
fun WeatherApp(appState: WeatherAppState = rememberWeatherAppState()) {
    val todayLazyListState = rememberLazyListState()
    val tomorrowLazyListState = rememberLazyListState()

    NavHost(
        navController = appState.controller,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = hiltViewModel(),
                navigateToNextSevenDay = { lat, long ->
                    appState.navigateToSevenDays(lat.toFloat(), long.toFloat())
                },
                todayLazyListState = todayLazyListState,
                tomorrowLazyListState = tomorrowLazyListState
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
