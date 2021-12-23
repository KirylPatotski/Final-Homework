package com.omisoft.myapplication.mvvm.data.network

import com.omisoft.myapplication.mvvm.data.storage.room.entity.Album;

interface NetworkMusicService {
    fun getFavouriteMusic(): List<Any>
    fun getAlbums(): List<Album>
    fun updateFavouriteMusic(data: Any)
}