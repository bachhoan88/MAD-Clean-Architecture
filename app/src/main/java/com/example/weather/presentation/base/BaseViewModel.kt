package com.example.weather.presentation.base

import androidx.lifecycle.ViewModel
import com.example.weather.domain.usecase.UseCase
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel(
    private vararg val useCases: UseCase<*, *>?
) : ViewModel() {

    abstract val state: StateFlow<ViewState>

    override fun onCleared() {
        useCases.forEach { it?.onCleared() }
        super.onCleared()
    }
}
