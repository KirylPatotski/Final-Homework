package com.omisoft.myapplication.mvvm.data.storage.room

interface AlbumDatabase {
    fun getAlbumDao(): AlbumDao
}