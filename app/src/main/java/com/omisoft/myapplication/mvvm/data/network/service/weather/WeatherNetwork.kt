package com.omisoft.myapplication.mvvm.data.network.service.weather
import com.omisoft.myapplication.mvvm.data.network.service.weather.services.WeatherService

interface WeatherNetwork {
    fun getWeatherService(): WeatherService
}