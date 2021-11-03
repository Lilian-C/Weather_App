package com.Lilian.WeatherApp.di

import com.Lilian.WeatherApp.network.WeatherApi
import com.Lilian.WeatherApp.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMainRepository(
        weatherApi: WeatherApi
    ): WeatherRepository {
        return WeatherRepository(weatherApi)
    }
}