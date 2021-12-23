package com.omisoft.myapplication.mvvm.data.storage.room

import android.content.Context

class AppDatabaseImpl private constructor() : AlbumDatabase {

    companion object {
        private var instance: AppDatabaseImpl? = null
        private var albumDao: AlbumDao? = null

        fun getInstance(context: Context): AppDatabaseImpl {
            if (instance == null) {
                instance = AppDatabaseImpl()
                albumDao = AppDatabase.buildDatabase(context).getAlbumDao()
            }

            return instance!!
        }
    }

    override fun getAlbumDao(): AlbumDao = albumDao!!
}