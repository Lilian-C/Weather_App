package com.Lilian.WeatherApp.network

import com.Lilian.WeatherApp.model.Forecast
import com.Lilian.WeatherApp.model.ForecastReport
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    fun getTodayForecast(@Query("q") city: String, @Query("lang") lang: String): Forecast

    @GET("forecast")
    fun getWeeklyForecast(@Query("q") city: String, @Query("cnt") cnt : Int, @Query("lang") lang: String): ForecastReport
}