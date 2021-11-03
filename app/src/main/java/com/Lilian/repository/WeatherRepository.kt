package com.Lilian.WeatherApp.repository

import android.util.Log
import com.Lilian.WeatherApp.BuildConfig
import com.Lilian.WeatherApp.model.Forecast
import com.Lilian.WeatherApp.model.ForecastReport
import com.Lilian.WeatherApp.model.Weather
import com.Lilian.WeatherApp.network.WeatherApi
import com.Lilian.WeatherApp.util.DataState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class WeatherRepository
constructor(
        private val weatherApi: WeatherApi
) {
    suspend fun getTodayForecast(cities: List<String>): Flow<DataState<List<Forecast>>> = flow {
        emit(DataState.Loading)
        var forecasts: MutableList<Forecast> = mutableListOf()

        try {
            coroutineScope {
                cities.forEach { cityName ->
                    launch { // this will allow us to run multiple tasks in parallel
                        val localWeather: Forecast? = weatherApi.getTodayForecast(
                                cityName,
                                "fr"
                        )
                        if (localWeather != null) {
                            forecasts.add(localWeather);
                        }
                    }
                }
            }
            emit(DataState.Success(forecasts))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    suspend fun getWeeklyForecast(city: String): Flow<DataState<List<Forecast>>> = flow {
        emit(DataState.Loading)
        try {
            val forecasts =
                    weatherApi.getWeeklyForecast(city, 14, "fr")
            emit(DataState.Success(forecasts.report))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}