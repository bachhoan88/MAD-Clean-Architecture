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

enum class WeatherIndex(private val index: Int) {
    Today(0), Tomorrow(1);

    fun value(): Int = index

    companion object {
        fun from(index: Int): WeatherIndex {
            return if (index == 0) Today else Tomorrow
        }
    }
}

sealed interface WeatherState {
    val weatherIndex: WeatherIndex
    val weathers: List<HourlyWeatherViewDataModel>

    data class Today(
        override val weatherIndex: WeatherIndex = WeatherIndex.Today,
        override val weathers: List<HourlyWeatherViewDataModel> = emptyList(),
    ) : WeatherState

    data class Tomorrow(
        override val weatherIndex: WeatherIndex = WeatherIndex.Tomorrow,
        override val weathers: List<HourlyWeatherViewDataModel> = emptyList(),
    ) : WeatherState
}

sealed interface SearchState {
    val enabled: Boolean
    val query: String

    data class Changing(
        override val enabled: Boolean = true,
        override val query: String = ""
    ) : SearchState

    data class Closed(
        override val enabled: Boolean = false,
        override val query: String = ""
    ) : SearchState
}

data class HomeViewState(
    override val isLoading: Boolean = false,
    override val exception: BaseException? = null,
    val currentWeather: CurrentWeatherViewDataModel? = null,
    val weatherState: WeatherState = WeatherState.Today(),
    val searchState: SearchState = SearchState.Closed()
) : ViewState(isLoading, exception)

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

    val coordinate = MutableStateFlow(Pair(0.0, 0.0))

    private val todayState = MutableStateFlow(WeatherState.Today())
    private val tomorrowState = MutableStateFlow(WeatherState.Tomorrow())

    init {
        viewModelScope.launch {
            getLastCityUseCase.invoke()
                .collect { city ->
                    getWeather(city)
                }
        }
    }

    fun getWeather(city: String) {
        _state.update { HomeViewState(isLoading = true) }

        viewModelScope.launch {
            getCurrentWeatherByCityUseCase.invoke(GetCurrentWeatherByCityUseCase.Params(city))
                .catch { throwable ->
                    _state.update { it.copy(isLoading = false, exception = throwable.toBaseException()) }
                }
                .map { weather ->
                    coordinate.update { it.copy(first = weather.coord.lat, second = weather.coord.long) }
                    getHourlyWeathers(weather.coord.lat, weather.coord.long)
                    weatherMapper.mapperToViewDataModel(weather)
                }
                .collect { weather ->
                    _state.update {
                        it.copy(isLoading = false, currentWeather = weather)
                    }
                }
        }
    }

    /**
     * Notify that the user when typing the search input
     */
    fun onSearchInputChanged(searchInput: String) {
        _state.update {
            it.copy(searchState = SearchState.Changing(query = searchInput))
        }
    }

    /**
     * Enable or disable search view
     */
    fun enableSearchView(enabled: Boolean) {
        _state.update { state ->
            state.copy(searchState = if (enabled) SearchState.Changing() else SearchState.Closed(query = state.searchState.query))
        }
    }

    /**
     * Change hourly weather today or tomorrow
     */
    fun weatherIndexChanged(index: WeatherIndex) {
        _state.update { it.copy(weatherState = if (index == WeatherIndex.Today) todayState.value else tomorrowState.value) }
    }

    private fun getHourlyWeathers(lat: Double, long: Double) {
        viewModelScope.launch {
            getHourlyWeatherUseCase.invoke(GetHourlyWeatherUseCase.Params(lat, long))
                .catch { throwable ->
                    _state.update { it.copy(isLoading = false, exception = throwable.toBaseException()) }
                }
                .map { response ->
                    Pair(
                        response.today.map { hourlyWeatherMapper.mapperToViewDataModel(it) },
                        response.tomorrow.map { hourlyWeatherMapper.mapperToViewDataModel(it) }
                    )
                }
                .collect { pair ->
                    todayState.update { WeatherState.Today(weathers = pair.first) }
                    tomorrowState.update { WeatherState.Tomorrow(weathers = pair.second) }
                    _state.update { it.copy(weatherState = todayState.value) }
                }
        }
    }
}
