package com.omisoft.myapplication.mvvm.model.network

import com.omisoft.myapplication.mvvm.model.entity.Album;

interface NetworkMusicService {
    fun getFavouriteMusic(): List<Any>
    fun getAlbums(): List<Album>
    fun updateFavouriteMusic(data: Any)
}