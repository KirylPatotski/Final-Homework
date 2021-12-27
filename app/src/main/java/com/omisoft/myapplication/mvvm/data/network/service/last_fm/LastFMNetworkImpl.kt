package com.omisoft.myapplication.mvvm.data.network.service.last_fm

import com.omisoft.myapplication.mvvm.data.network.service.last_fm.services.ArtistsService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LastFMNetworkImpl private constructor() : LastFMNetwork {

    private lateinit var artistsService: ArtistsService

    companion object {
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
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        artistsService = retrofit.create(ArtistsService::class.java)
    }

    override fun getArtistsService(): ArtistsService = artistsService
}