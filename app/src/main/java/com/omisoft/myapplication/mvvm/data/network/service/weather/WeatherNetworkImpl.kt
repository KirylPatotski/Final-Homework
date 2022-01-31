package com.omisoft.myapplication.mvvm.data.network.service.weather

import com.omisoft.myapplication.mvvm.data.network.service.weather.services.CoroutineWeatherService
import com.omisoft.myapplication.mvvm.data.network.service.weather.services.RxWeatherService
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class WeatherNetworkImpl private constructor() : WeatherNetwork {

    private lateinit var coroutineWeatherService: CoroutineWeatherService
    private lateinit var rxWeatherService: RxWeatherService

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/"

        private var instance: WeatherNetwork? = null

        fun getInstance(): WeatherNetwork {
            if (instance == null) {
                instance = WeatherNetworkImpl()
            }

            return instance!!
        }
    }

    init {
        initServices()
    }

    private fun initServices() {
        val bodyInterceptor = HttpLoggingInterceptor()
        val headersInterceptor = HttpLoggingInterceptor()
        bodyInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        headersInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)

        val client = OkHttpClient.Builder()
            .addInterceptor(bodyInterceptor)
            .addInterceptor(headersInterceptor)
            .build()

        initCoroutineWeatherService(client)
        initRxWeatherService(client)
    }

    override fun getCoroutineWeatherService(): CoroutineWeatherService = coroutineWeatherService

    override fun getRxWeatherService(): RxWeatherService = rxWeatherService

    private fun initCoroutineWeatherService(client: OkHttpClient) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        coroutineWeatherService = retrofit.create(CoroutineWeatherService::class.java)
    }

    private fun initRxWeatherService(client: OkHttpClient) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(client)
            .build()

        rxWeatherService = retrofit.create(RxWeatherService::class.java)
    }
}