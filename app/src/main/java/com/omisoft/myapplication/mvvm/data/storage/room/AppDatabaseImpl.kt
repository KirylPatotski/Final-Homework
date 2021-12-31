package com.omisoft.myapplication.mvvm.data.storage.room

import android.content.Context

class AppDatabaseImpl private constructor() : AlbumDatabase, CachingCountDataBase {

    companion object {
        private var instance: AppDatabaseImpl? = null
        private var albumDao: AlbumDao? = null
        private var cachingCountDao: CachingCountDao? = null

        fun getInstance(context: Context): AppDatabaseImpl {
            if (instance == null) {
                instance = AppDatabaseImpl()
                albumDao = AppDatabase.buildDatabase(context).getAlbumDao()
                cachingCountDao = AppDatabase.buildDatabase(context).getCachingCountDao()
            }

            return instance!!
        }
    }

    override fun getAlbumDao(): AlbumDao = albumDao!!

    override fun getCachingCountDao(): CachingCountDao = cachingCountDao!!
}