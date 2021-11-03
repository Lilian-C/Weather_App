package com.Lilian.WeatherApp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ForecastReport(
    @SerializedName("list")
    @Expose
    var report : List<Forecast>
)