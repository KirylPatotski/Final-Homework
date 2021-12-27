package com.omisoft.myapplication.mvvm.data.network.service.last_fm

import com.omisoft.myapplication.mvvm.data.network.service.last_fm.services.ArtistsService

interface LastFMNetwork {
    fun getArtistsService(): ArtistsService
}