package com.omisoft.myapplication.mvvm.data.storage.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.omisoft.myapplication.mvvm.data.storage.room.entity.Album
import com.omisoft.myapplication.mvvm.data.storage.room.entity.CachingCount

@Database(entities = [Album::class, CachingCount::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "AppDatabase"
        ).build()
    }

    abstract fun getAlbumDao(): AlbumDao

    abstract fun getCachingCountDao(): CachingCountDao
}