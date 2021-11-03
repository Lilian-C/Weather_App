package com.Lilian.ui.MainActivity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.Lilian.WeatherApp.R
import com.Lilian.WeatherApp.model.Forecast
import com.Lilian.WeatherApp.util.DataState
import com.Lilian.ui.CityActivity.CityActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), WeatherAdapter.WeatherItemListener {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: WeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setupRecyclerView()
        subscribeObservers()

        viewModel.setStateEvent(MainStateEvent.GetWeatherEvents)
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(this, Observer { dataState ->
            when (dataState) {
                is DataState.Success<List<Forecast>> -> {
                    displayLoading(false)
                    populateRecyclerView(dataState.data)
                }
                is DataState.Loading -> {
                    displayLoading(true)
                }
                is DataState.Error -> {
                    displayLoading(false)
                    displayError(dataState.exception.message)
                }
            }
        })
    }


    private fun displayError(message: String?) {
        if (message.isNullOrEmpty()) {
            Toast.makeText(this, "Unknown error", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun displayLoading(isLoading: Boolean) {
        // Display circular loading
    }

    private fun populateRecyclerView(cities: List<Forecast>) {
        if (cities.isNotEmpty()) adapter.setItems(ArrayList(cities))
    }

    private fun setupRecyclerView() {
        adapter = WeatherAdapter(this)
        weather_rv.layoutManager = LinearLayoutManager(this)
        weather_rv.adapter = adapter
    }

    override fun onClickedWeather(weatherTitle: CharSequence) {
        val intent = Intent(this, CityActivity::class.java).apply {
            putExtra("city_name", weatherTitle)
        }
        startActivity(intent)
    }

}