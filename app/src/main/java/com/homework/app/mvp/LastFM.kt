package com.homework.app.mvp.service

import com.homework.app.mvvw.data.artist.ArtistsResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFMNetwork {
    fun getArtistsService(): ArtistsService
}

class LastFMNetworkImpl private constructor() : LastFMNetwork {

    private lateinit var artistsService: ArtistsService

    companion object {
        private const val BASE_URL = "https://ws.audioscrobbler.com/"

        private var instance: LastFMNetworkImpl? = null

        fun getInstance(): LastFMNetworkImpl {
            if (instance == null) {
                instance = LastFMNetworkImpl()
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

        artistsService = retrofit.create(ArtistsService::class.java)
    }

    override fun getArtistsService(): ArtistsService = artistsService
}

interface ArtistsService {

    companion object {
        private const val BASE_PATH = "2.0"
        private const val KEY_METHOD = "chart.gettopartists"
        private const val KEY_API_KEY = "a35699f435445486aec22d7a19e652bf"
        private const val KEY_FORMAT = "json"
    }

    @GET("$BASE_PATH/")
    suspend fun getTopArtists(
        @Query("method") method: String = KEY_METHOD,
        @Query("api_key") apiKey: String = KEY_API_KEY,
        @Query("format") format: String = KEY_FORMAT,
    ): ArtistsResponse
//    https://ws.audioscrobbler.com/2.0/?method=chart.gettopartists&api_key=a35699f435445486aec22d7a19e652bf&format=json
}