package com.example.weather.domain.annotation

import androidx.annotation.IntDef
import com.example.weather.domain.annotation.Action.Companion.CLOSE_SESSION
import com.example.weather.domain.annotation.Action.Companion.RELOAD_PAGE

@IntDef(RELOAD_PAGE, CLOSE_SESSION)
annotation class Action {
    companion object {
        const val RELOAD_PAGE = 1
        const val CLOSE_SESSION = 2
    }
}
