package com.omisoft.myapplication.mvvm.data.network.service.weather

import com.omisoft.myapplication.mvvm.data.network.service.weather.services.CoroutineWeatherService
import com.omisoft.myapplication.mvvm.data.network.service.weather.services.RxWeatherService

interface WeatherNetwork {
    fun getCoroutineWeatherService(): CoroutineWeatherService
    fun getRxWeatherService(): RxWeatherService
}