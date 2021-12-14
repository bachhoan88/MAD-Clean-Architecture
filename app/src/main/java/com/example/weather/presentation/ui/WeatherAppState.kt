package com.example.weather.presentation.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object NextSevenDays : Screen("sevenDays/{lat}-{long}") {
        fun createRoute(lat: Float, long: Float) = "sevenDays/$lat-$long"
    }
}

@Composable
fun rememberWeatherAppState(
    controller: NavHostController = rememberNavController()
) = remember(controller) {
    WeatherAppState(controller)
}

@Composable
fun rememberLazyListState(
    vararg inputs: Any?,
    initialFirstVisibleItemIndex: Int = 0,
    initialFirstVisibleItemScrollOffset: Int = 0
): LazyListState {
    return rememberSaveable(inputs, saver = LazyListState.Saver) {
        LazyListState(
            initialFirstVisibleItemIndex,
            initialFirstVisibleItemScrollOffset
        )
    }
}

class WeatherAppState(val controller: NavHostController) {
    fun navigateToSevenDays(lat: Float, long: Float) {
        controller.navigate(Screen.NextSevenDays.createRoute(lat, long))
    }

    fun navigateBack() {
        controller.popBackStack()
    }
}
