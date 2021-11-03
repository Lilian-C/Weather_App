package com.Lilian.ui.MainActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.Lilian.WeatherApp.R
import com.Lilian.WeatherApp.model.Forecast
import kotlinx.android.synthetic.main.item_weather.view.*

class WeatherAdapter(private val listener: WeatherItemListener) : RecyclerView.Adapter<WeatherViewHolder>() {

    interface WeatherItemListener {
        fun onClickedWeather(weatherTitle: CharSequence)
    }

    private val items = ArrayList<Forecast>()

    fun setItems(items: ArrayList<Forecast>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_weather, parent, false)
        return WeatherViewHolder(view, listener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weatherReport = items[position]

        holder.textTitle.text = weatherReport.name
        holder.textDescription.text = weatherReport.weather[0]?.description

        if (weatherReport.temperature?.temp != null) {
            holder.textTemperature.text = ("%.0f".format((weatherReport.temperature!!.temp!! - 273.15))) + "°C"
        }
        else {
            holder.textTemperature.text = ""
        }

        //Todo: had a placeholder in case of error
        Glide.with(holder.itemLayout).load("https://openweathermap.org/img/w/${weatherReport.weather[0]?.icon}.png")
            .apply(RequestOptions().centerCrop())
            .transition(withCrossFade())
            .into(holder.weatherImage)
    }
}

class WeatherViewHolder(itemView: View, private val listener: WeatherAdapter.WeatherItemListener) :
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
        listener.onClickedWeather(textTitle.text)
    }
}

