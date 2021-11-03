package com.Lilian.ui.CityActivity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.Lilian.WeatherApp.R
import com.Lilian.WeatherApp.model.Forecast
import com.Lilian.WeatherApp.util.DataState
import com.Lilian.ui.MainActivity.CityStateEvent
import com.Lilian.ui.MainActivity.CityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_city.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CityActivity : AppCompatActivity(), CityWeatherAdapter.CityWeatherItemListener {

    private val viewModel: CityViewModel by viewModels()
    private lateinit var adapter: CityWeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_city)
        setupRecyclerView()
        subscribeObservers()

        viewModel.setStateEvent(CityStateEvent.GetWeatherEvents)
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
        adapter = CityWeatherAdapter(this)
        weather_rv.layoutManager = LinearLayoutManager(this)
        weather_rv.adapter = adapter
    }
}


