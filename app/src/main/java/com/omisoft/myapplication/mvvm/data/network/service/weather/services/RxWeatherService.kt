package com.omisoft.myapplication.mvvm.data.network.service.weather.services

import com.omisoft.myapplication.mvvm.data.network.model.weather.WeatherResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RxWeatherService {

    companion object {
        private const val BASE_PATH = "data/2.5"
        private const val WEATHER_PATH = "weather"
        private const val KEY_UNITS = "metric"
        private const val KEY_API_KEY = "f8f4c1eb92c75f7adf32de36015abf37"
    }

    @GET("${BASE_PATH}/{weather}")
    fun getWeatherByLocation(
        @Path("weather") weather: String = WEATHER_PATH,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String = KEY_API_KEY,
        @Query("units") units: String = KEY_UNITS,
    ): Single<WeatherResponse>
}