package com.Lilian.ui.CityActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.Lilian.WeatherApp.R
import com.Lilian.WeatherApp.model.Forecast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_weather.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CityWeatherAdapter(private val listener: CityWeatherItemListener) : RecyclerView.Adapter<CityWeatherViewHolder>() {

    interface CityWeatherItemListener {}

    private val items = ArrayList<Forecast>()

    fun setItems(items: ArrayList<Forecast>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityWeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_weather, parent, false)
        return CityWeatherViewHolder(view, listener)
    }

    override fun getItemCount(): Int = items.size


    override fun onBindViewHolder(holder: CityWeatherViewHolder, position: Int) {
        val forecast = items[position]

        val forecastDate = ""

        if (forecast.date != null) {
            val milliDate = Date(forecast.date!! * 1000)
            val frenchDateFormatter = SimpleDateFormat("dd MMMM", Locale.FRANCE)
            holder.textTitle.text = frenchDateFormatter.format(milliDate)
        }

        holder.textDescription.text = forecast.weather[0]?.description

        if (forecast.temperature?.temp != null) {
            holder.textTemperature.text = ("%.0f".format((forecast.temperature!!.temp!! - 273.15))) + "°C"
        }
        else {
            holder.textTemperature.text = ""
        }

        //Todo: add a placeholder in case of error
        Glide.with(holder.itemLayout).load("https://openweathermap.org/img/w/${forecast.weather[0]?.icon}.png")
            .apply(RequestOptions().centerCrop())
            .transition(withCrossFade())
            .into(holder.weatherImage)
    }
}

class CityWeatherViewHolder(itemView: View, private val listener: CityWeatherAdapter.CityWeatherItemListener) :
    RecyclerView.ViewHolder(itemView),
    View.OnClickListener {

    val itemLayout: ConstraintLayout = itemView.weather_layout
    val textTitle: TextView = itemView.title_tv
    val textDescription: TextView = itemView.description_tv
    val textTemperature: TextView = itemView.temperature_tv
    val weatherImage: AppCompatImageView = itemView.weather_img

    init {
        itemLayout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        //listener.onClickedWeather(textTitle.text)
    }
}

