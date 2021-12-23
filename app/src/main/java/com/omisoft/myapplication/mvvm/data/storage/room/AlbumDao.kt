package com.omisoft.myapplication.mvvm.data.storage.room

import androidx.room.*
import com.omisoft.myapplication.mvvm.data.storage.room.entity.Album

@Dao
interface AlbumDao {
    @Insert
    fun insertAlbum(album: Album)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlbums(album: List<Album>)

    @Delete
    fun deleteAlbum(album: Album)

    @Delete
    fun deleteAlbums(vararg album: Album)

    @Query("DELETE FROM album")
    fun deleteAlbums()

    @Update
    fun updateAlbum(album: Album)

    @Query("SELECT * FROM album")
    fun getAlbums(): List<Album>

    @Query("SELECT * FROM album WHERE name LIKE :albumName")
    fun searchAlbums(albumName: String): List<Album>
}