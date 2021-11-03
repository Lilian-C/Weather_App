package com.Lilian.WeatherApp.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.Lilian.WeatherApp.network.OpenWeatherInterceptor
import com.Lilian.WeatherApp.network.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit.Builder {
        val client = OkHttpClient.Builder()
            .addInterceptor(OpenWeatherInterceptor())
            .build()
        return Retrofit.Builder().client(client)
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Singleton
    @Provides
    fun provideWeatherService(retrofit: Retrofit.Builder): WeatherApi {
        return retrofit
            .build()
            .create(WeatherApi::class.java)
    }
}