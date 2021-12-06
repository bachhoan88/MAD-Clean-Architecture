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
    override val state
        get() = _state.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            _state.value
        )

    init {
        viewModelScope.launch {
            getLastCityUseCase.execute()
                .collect { city ->
                    getWeather(city)
                }
        }
    }

    fun getWeather(city: String) {
        _state.update { HomeViewState(isLoading = true) }

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

    /**
     * Notify that the user when typing the search input
     */
    fun onSearchInputChanged(searchInput: String) {
        _state.update {
            it.copy(searchInput = searchInput)
        }
    }

    /**
     * Enable or disable search view
     */
    fun enableSearchView(enabled: Boolean) {
        _state.update {
            it.copy(searchEnabled = enabled, searchInput = if (!enabled) "" else it.searchInput)
        }
    }

    private fun getHourlyWeathers(lat: Double, long: Double) {
        viewModelScope.launch {
            getHourlyWeatherUseCase.execute(GetHourlyWeatherUseCase.Params(lat, long))
                .catch { throwable ->
                    _state.update { it.copy(isLoading = false, exception = throwable.toBaseException()) }
                }
                .map { it.map { houry -> hourlyWeatherMapper.mapperToViewDataModel(houry) } }
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
    val hourlyWeathers: List<HourlyWeatherViewDataModel> = emptyList(),
    val searchInput: String = "",
    val searchEnabled: Boolean = false
) : ViewState(isLoading, exception)
