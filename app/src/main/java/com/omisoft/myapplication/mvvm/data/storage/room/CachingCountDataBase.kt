package com.omisoft.myapplication.mvvm.data.storage.room

interface CachingCountDataBase {
    fun getCachingCountDao(): CachingCountDao
}