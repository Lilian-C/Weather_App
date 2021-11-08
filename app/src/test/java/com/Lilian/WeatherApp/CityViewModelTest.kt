package com.Lilian.WeatherApp;

import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.Lilian.WeatherApp.model.Forecast
import com.Lilian.WeatherApp.model.ForecastReport
import com.Lilian.WeatherApp.repository.WeatherRepository
import com.Lilian.WeatherApp.util.DataState
import com.Lilian.ui.MainActivity.CityStateEvent
import com.Lilian.ui.MainActivity.CityViewModel
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
class CityViewModelTest {
    private lateinit var viewModel: CityViewModel

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

        `when`(repo.getWeeklyForecast("Nantes")).thenAnswer {_dataState.asFlow()}

        //var savedStateHandle = SavedStateHandle()
        //savedStateHandle.set("city_name", "Nantes")

        _dataState.value = DataState.Success(listOf<Forecast>())
        viewModel = CityViewModel(repo, SavedStateHandle())
        dataState = viewModel.dataState;
    }

    @Test
    fun `Api should be called with a city name`() = runBlocking {
        viewModel.cityName = "Nantes"
        viewModel.setStateEvent(CityStateEvent.GetWeatherEvents)
        verify(repo).getWeeklyForecast("Nantes")
        return@runBlocking
    }

    @Test
    fun `Api shouldn't be called with a null city name`() = runBlocking {
        viewModel.cityName = null
        viewModel.setStateEvent(CityStateEvent.GetWeatherEvents)
        verifyNoInteractions(repo)
        return@runBlocking
    }
}