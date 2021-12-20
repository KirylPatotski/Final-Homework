package com.omisoft.myapplication.mvvm.ui.draft.countries

import androidx.lifecycle.*
import com.omisoft.myapplication.mvvm.model.entity.Album
import com.omisoft.myapplication.mvvm.model.network.NetworkMusicService
import com.omisoft.myapplication.mvvm.model.network.NetworkMusicServiceImpl
import com.omisoft.myapplication.mvvm.model.storage.preferences.AppPreferences

class ListViewModel : ViewModel(), LifecycleEventObserver {

    val countriesLiveData = MutableLiveData<List<String>>()
    val albumsLiveData = MutableLiveData<List<Album>>()
    val logoutLiveData = MutableLiveData<Unit>()

    val model: CountriesModel = CountriesModelImpl()

    private var preferences: AppPreferences? = null
    private val musicModel: NetworkMusicService = NetworkMusicServiceImpl()

    fun setSharedPreferences(preferences: AppPreferences) {
        this.preferences = preferences
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
        val albums = musicModel.getAlbums()
        albumsLiveData.value = albums
    }
}