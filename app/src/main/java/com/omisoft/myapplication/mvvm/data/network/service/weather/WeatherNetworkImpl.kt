package com.omisoft.myapplication.mvvm.data.network.service.weather

import com.omisoft.myapplication.mvvm.data.network.service.weather.services.WeatherService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherNetworkImpl private constructor() : WeatherNetwork {

    private lateinit var weatherService: WeatherService

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/"

        private var instance: WeatherNetworkImpl? = null

        fun getInstance(): WeatherNetworkImpl {
            if (instance == null) {
                instance = WeatherNetworkImpl()
            }

            return instance!!
        }
    }

    init {
        initService()
    }

    private fun initService() {
        val bodyInterceptor = HttpLoggingInterceptor()
        val headersInterceptor = HttpLoggingInterceptor()
        bodyInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        headersInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)

        val client = OkHttpClient.Builder()
            .addInterceptor(bodyInterceptor)
            .addInterceptor(headersInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        weatherService = retrofit.create(WeatherService::class.java)
    }

    override fun getWeatherService(): WeatherService = weatherService
}