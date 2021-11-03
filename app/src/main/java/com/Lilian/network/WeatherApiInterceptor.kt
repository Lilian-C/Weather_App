package com.Lilian.WeatherApp.network

import android.util.Log
import com.Lilian.WeatherApp.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

class OpenWeatherInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url: HttpUrl = chain.request().url().newBuilder().addQueryParameter("appid", BuildConfig.API_KEY).build()

        return chain.proceed(chain.request().newBuilder().url(url).build())
    }
}