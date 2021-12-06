package com.example.weather.data

object Constants {
    object HttpClient {
        const val CONNECT_TIMEOUT = 10L
        const val READ_TIMEOUT = 10L
        const val WRITE_TIMEOUT = 10L
        const val CONNECTION_TIME_OUT_MLS = CONNECT_TIMEOUT * 1000L
    }

    object Authentication {
        const val MAX_RETRY = 1
    }

    object DateFormat {
        const val EEEE_dd_MMMM = "EEEE',' dd MMMM"
        const val DEFAULT_FORMAT = "dd-mm-yyyy"
        const val HH_mm = "HH:mm"
    }

    object OpenWeather {
        const val WEATHER_ICON_URL = "https://openweathermap.org/img/wn/%s@4x.png"
        const val WEATHER_SMALL_ICON_URL = "https://openweathermap.org/img/wn/%s.png"
    }
}
