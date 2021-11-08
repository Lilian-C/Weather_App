package com.Lilian.WeatherApp;

import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.Lilian.WeatherApp.model.Forecast
import com.Lilian.WeatherApp.model.ForecastReport
import com.Lilian.WeatherApp.repository.WeatherRepository
import com.Lilian.WeatherApp.util.DataState
import com.Lilian.ui.MainActivity.MainStateEvent
import com.Lilian.ui.MainActivity.MainViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import org.mockito.junit.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MainViewModelTest {
    private lateinit var viewModel: MainViewModel

    private lateinit var dataState: LiveData<DataState<List<Forecast>>>

    @Spy
    private val _dataState: MutableLiveData<DataState<List<Forecast>>> = MutableLiveData()

    @Mock
    private lateinit var repo: WeatherRepository

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        `when`(repo.getTodayForecast(listOf("Nantes", "Paris", "Moscou", "Tokyo", "Montreal", "Rezé"))).thenAnswer {_dataState.asFlow()}

        _dataState.value = DataState.Success(listOf<Forecast>())
        viewModel = MainViewModel(repo, SavedStateHandle())
        dataState = viewModel.dataState;
    }

    @Test
    fun `Api should be called with a city name`() = runBlocking {
        viewModel.setStateEvent(MainStateEvent.GetWeatherEvents)
        verify(repo).getTodayForecast(listOf("Nantes", "Paris", "Moscou", "Tokyo", "Montreal", "Rezé"))
        return@runBlocking
    }
}