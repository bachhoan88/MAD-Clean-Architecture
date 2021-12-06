package com.example.weather.domain.repository

import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    fun getLastCityName(): Flow<String>
}
