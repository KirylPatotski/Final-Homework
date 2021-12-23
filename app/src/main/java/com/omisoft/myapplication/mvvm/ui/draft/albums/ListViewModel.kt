package com.omisoft.myapplication.mvvm.ui.draft.albums

import android.util.Log
import androidx.lifecycle.*
import com.omisoft.myapplication.mvvm.data.network.NetworkMusicService
import com.omisoft.myapplication.mvvm.data.network.NetworkMusicServiceImpl
import com.omisoft.myapplication.mvvm.data.storage.preferences.AppPreferences
import com.omisoft.myapplication.mvvm.data.storage.room.AlbumDao
import com.omisoft.myapplication.mvvm.data.storage.room.entity.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListViewModel : ViewModel(), LifecycleEventObserver {

    companion object {
        private const val TAG = "ListViewModel"
    }

    private val model: CountriesModel = CountriesModelImpl()
    private var albumDao: AlbumDao? = null

    val countriesLiveData = MutableLiveData<List<String>>()
    val albumsLiveData = MutableLiveData<List<Album>>()
    val logoutLiveData = MutableLiveData<Unit>()

    private var preferences: AppPreferences? = null
    private val musicModel: NetworkMusicService = NetworkMusicServiceImpl()

    fun setSharedPreferences(preferences: AppPreferences) {
        this.preferences = preferences
    }

    fun setAlbumDao(albumDao: AlbumDao) {
        this.albumDao = albumDao
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                println("ON_CREATE")
                getCountries()
                getAlbums()
            }
            Lifecycle.Event.ON_START -> {
                println("ON_START")
            }
            Lifecycle.Event.ON_RESUME -> {
                println("ON_RESUME")
            }
            Lifecycle.Event.ON_PAUSE -> {
                println("ON_PAUSE")
            }
            Lifecycle.Event.ON_STOP -> {
                println("ON_STOP")
            }
            Lifecycle.Event.ON_DESTROY -> {
                println("ON_DESTROY")
            }
            Lifecycle.Event.ON_ANY -> {
                println("ON_ANY")
            }
        }
    }

    fun logout() {
        preferences?.saveToken("")
        logoutLiveData.value = Unit
    }

    private fun getCountries() {
        val countries = model.getCountries()
        countriesLiveData.value = countries
    }

    private fun getAlbums() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val albums = musicModel.getAlbums()
                albumDao?.insertAlbums(albums)
                albumsLiveData.postValue(albums)
            } catch (exception: Exception) {
                Log.e(TAG, exception.message ?: "")
            }
        }
    }
}