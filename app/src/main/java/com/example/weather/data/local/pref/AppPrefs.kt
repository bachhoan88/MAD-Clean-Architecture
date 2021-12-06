package com.example.weather.data.local.pref

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppPrefs @Inject constructor(
    @ApplicationContext private val context: Context
) : PrefsHelper {

    private val sharedPreferences by lazy {
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    override fun isFirstRun(): Boolean {
        return sharedPreferences.getBoolean(FIRST_RUN_TAG, true)
    }

    override fun setFirstRun(enable: Boolean) {
        sharedPreferences.edit { putBoolean(FIRST_RUN_TAG, enable) }
    }

    override fun saveLastCity(cityName: String) {
        sharedPreferences.edit { putString(LAST_CITY_TAG, cityName) }
    }

    override fun getLastCity(): String {
        return sharedPreferences.getString(LAST_CITY_TAG, null) ?: DEFAULT_CITY_NAME
    }

    companion object {
        private const val FIRST_RUN_TAG = "first_run"
        private const val LAST_CITY_TAG = "country"
        private const val DEFAULT_CITY_NAME = "Hanoi"
    }
}
