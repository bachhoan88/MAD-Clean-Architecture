package com.example.weather.presentation.ui.home

import androidx.lifecycle.viewModelScope
import com.example.weather.domain.exception.BaseException
import com.example.weather.domain.usecase.weather.GetCurrentWeatherByCityUseCase
import com.example.weather.domain.usecase.weather.GetHourlyWeatherUseCase
import com.example.weather.domain.usecase.weather.GetLastCityUseCase
import com.example.weather.presentation.base.BaseViewModel
import com.example.weather.presentation.base.ViewState
import com.example.weather.presentation.base.toBaseException
import com.example.weather.presentation.model.CurrentWeatherMapper
import com.example.weather.presentation.model.CurrentWeatherViewDataModel
import com.example.weather.presentation.model.HourlyWeatherMapper
import com.example.weather.presentation.model.HourlyWeatherViewDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentWeatherByCityUseCase: GetCurrentWeatherByCityUseCase,
    private val weatherMapper: CurrentWeatherMapper,
    private val getLastCityUseCase: GetLastCityUseCase,
    private val getHourlyWeatherUseCase: GetHourlyWeatherUseCase,
    private val hourlyWeatherMapper: HourlyWeatherMapper
) : BaseViewModel() {

    private val _state = MutableStateFlow(HomeViewState(isLoading = true))
    override val state: StateFlow<HomeViewState>
        get() = _state

    init {
        viewModelScope.launch {
            getLastCityUseCase.execute()
                .collect { city ->
                    getWeather(city)
                }
        }
    }

    fun getWeather(city: String) {
        viewModelScope.launch {
            getCurrentWeatherByCityUseCase.execute(GetCurrentWeatherByCityUseCase.Params(city))
                .catch { throwable ->
                    _state.update { it.copy(isLoading = false, exception = throwable.toBaseException()) }
                }
                .map { weather ->
                    getHourlyWeathers(weather.coord.lat, weather.coord.long)
                    weatherMapper.mapperToViewDataModel(weather)
                }
                .collect { weather ->
                    _state.update { it.copy(isLoading = false, currentWeather = weather) }
                }
        }
    }

    private fun getHourlyWeathers(lat: Double, long: Double) {
        viewModelScope.launch {
            getHourlyWeatherUseCase.execute(GetHourlyWeatherUseCase.Params(lat, long))
                .catch { throwable ->
                    _state.update { it.copy(isLoading = false, exception = throwable.toBaseException()) }
                }
                .map { it.map { hourlyWeatherMapper.mapperToViewDataModel(it) } }
                .collect { weathers ->
                    _state.update { it.copy(hourlyWeathers = weathers) }
                }
        }
    }
}

data class HomeViewState(
    override val isLoading: Boolean = false,
    override val exception: BaseException? = null,
    val currentWeather: CurrentWeatherViewDataModel? = null,
    val hourlyWeathers: List<HourlyWeatherViewDataModel> = emptyList()
) : ViewState(isLoading, exception)