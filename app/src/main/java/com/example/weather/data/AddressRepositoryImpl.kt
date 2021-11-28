package com.example.weather.data

import com.example.weather.data.local.pref.PrefsHelper
import com.example.weather.domain.asFlow
import com.example.weather.domain.repository.AddressRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val prefsHelper: PrefsHelper
) : AddressRepository {

    override fun getLastCityName(): Flow<String> {
        return prefsHelper.getLastCity().asFlow()
    }
}