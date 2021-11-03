package com.Lilian.WeatherApp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class Forecast(

    @SerializedName("id")
    @Expose
    var id: Int?,

    @SerializedName("main")
    @Expose
    var temperature: Temperature?,

    @SerializedName("weather")
    @Expose
    var weather: List<Weather?>,

    @SerializedName("name")
    @Expose
    var name: String?,

    @SerializedName("dt")
    @Expose
    var date: Long?
)