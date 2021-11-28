package com.example.weather.data.di

import com.example.weather.data.remote.api.WeatherApi
import com.example.weather.data.remote.builder.RetrofitBuilder
import com.example.weather.data.remote.interceptor.HeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(retrofitBuilder: RetrofitBuilder, headerInterceptor: HeaderInterceptor): Retrofit = retrofitBuilder
        .addInterceptors(headerInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi = retrofit.create(WeatherApi::class.java)
}