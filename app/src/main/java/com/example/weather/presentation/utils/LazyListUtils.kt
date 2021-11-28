package com.example.weather.presentation.utils

import androidx.compose.foundation.lazy.LazyListState

val LazyListState.isScrolled: Boolean
    get() = firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0
