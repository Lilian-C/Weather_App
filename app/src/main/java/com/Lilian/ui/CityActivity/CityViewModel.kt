package com.Lilian.ui.MainActivity

import android.os.Bundle
import android.util.Log
import androidx.annotation.MainThread
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.Lilian.WeatherApp.model.Forecast
import com.Lilian.WeatherApp.model.Weather
import com.Lilian.WeatherApp.repository.WeatherRepository
import com.Lilian.WeatherApp.util.DataState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CityViewModel
@ViewModelInject
constructor(
        private val weatherRepository: WeatherRepository,
        @Assisted protected val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _dataState: MutableLiveData<DataState<List<Forecast>>> = MutableLiveData()

    val dataState: LiveData<DataState<List<Forecast>>>
        get() = _dataState

    var cityName: String? = savedStateHandle.get<String>("city_name")

    fun setStateEvent(mainStateEvent: CityStateEvent) {
        viewModelScope.launch {
            when (mainStateEvent) {
                is CityStateEvent.GetWeatherEvents -> {
                    if (cityName != null) {
                        weatherRepository.getWeeklyForecast(cityName ?: "")
                                .onEach { dataState ->
                                    _dataState.value = dataState
                                }
                                .launchIn(viewModelScope)
                    } else {
                        setStateEvent(CityStateEvent.Error)
                    }
                }

                is CityStateEvent.Error -> {
                    //Do something...
                }

                is CityStateEvent.None -> {
                    // No action
                }
            }
        }
    }
}


sealed class CityStateEvent {
    object GetWeatherEvents : CityStateEvent()
    object Error : CityStateEvent()
    object None : CityStateEvent()
}
