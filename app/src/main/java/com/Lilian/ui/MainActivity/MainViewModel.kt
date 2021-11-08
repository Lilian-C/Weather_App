package com.Lilian.ui.MainActivity

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.Lilian.WeatherApp.model.Forecast
import com.Lilian.WeatherApp.repository.WeatherRepository
import com.Lilian.WeatherApp.util.DataState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel
@ViewModelInject
constructor(
        private val weatherRepository: WeatherRepository,
        @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _dataState: MutableLiveData<DataState<List<Forecast>>> = MutableLiveData()

    val dataState: LiveData<DataState<List<Forecast>>>
        get() = _dataState

    fun setStateEvent(mainStateEvent: MainStateEvent) {
        viewModelScope.launch {
            when (mainStateEvent) {
                is MainStateEvent.GetWeatherEvents -> {
                    weatherRepository.getTodayForecast(listOf("Nantes", "Paris", "Moscou", "Tokyo", "Montreal", "Rezé"))
                        .onEach { dataState ->
                            _dataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }

                is MainStateEvent.None -> {
                    // No action
                }
            }
        }
    }
}


sealed class MainStateEvent {
    object GetWeatherEvents : MainStateEvent()
    object None : MainStateEvent()
}

