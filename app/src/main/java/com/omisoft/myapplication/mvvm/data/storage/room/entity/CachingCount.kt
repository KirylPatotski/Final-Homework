package com.omisoft.myapplication.mvvm.data.storage.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CachingCount(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "count")
    val count: Int,
)