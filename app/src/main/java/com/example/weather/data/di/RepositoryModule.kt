package com.example.weather.data.di

import com.example.weather.data.AddressRepositoryImpl
import com.example.weather.data.WeatherRepositoryImpl
import com.example.weather.data.local.pref.AppPrefs
import com.example.weather.data.local.pref.PrefsHelper
import com.example.weather.domain.repository.AddressRepository
import com.example.weather.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun providePrefHelper(appPrefs: AppPrefs): PrefsHelper {
        return appPrefs
    }

    @Provides
    @Singleton
    fun providerWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository = weatherRepositoryImpl

    @Provides
    @Singleton
    fun providerAddressRepository(addressRepositoryImpl: AddressRepositoryImpl): AddressRepository = addressRepositoryImpl
}
