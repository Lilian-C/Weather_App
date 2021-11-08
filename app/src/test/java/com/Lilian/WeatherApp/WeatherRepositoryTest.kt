package com.Lilian.WeatherApp;

import android.os.Build
import androidx.lifecycle.*
import com.Lilian.WeatherApp.model.Forecast
import com.Lilian.WeatherApp.model.ForecastReport
import com.Lilian.WeatherApp.network.WeatherApi
import com.Lilian.WeatherApp.repository.WeatherRepository
import com.Lilian.WeatherApp.util.DataState
import com.Lilian.ui.MainActivity.MainStateEvent
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class WeatherRepositoryTest {

    private lateinit var weatherRepository: WeatherRepository

    @Mock
    private lateinit var weatherApi: WeatherApi

    @Mock
    private lateinit var fakeForecastReport: ForecastReport

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        weatherRepository = WeatherRepository(weatherApi);
    }

    @Test
    fun `DataState should be a success`() = runBlocking {
        `when`(weatherApi.getWeeklyForecast("Nantes", 14, "fr")).thenAnswer {fakeForecastReport}

        val res = weatherRepository.getWeeklyForecast("Nantes").toList()
        assertThat(res.last() , instanceOf(DataState.Success::class.java))
    }

    @Test
    fun `DataState should be an error`() = runBlocking {
        `when`(weatherApi.getWeeklyForecast("Nantes", 14, "fr")).thenAnswer { throw Exception() }

        val res = weatherRepository.getWeeklyForecast("Nantes").toList()
        assertThat(res.last() , instanceOf(DataState.Error::class.java))
    }
}
