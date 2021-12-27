package com.omisoft.myapplication.mvvm.ui.music.fragment

import androidx.lifecycle.*
import com.omisoft.myapplication.mvvm.data.network.model.artists.Artist
import com.omisoft.myapplication.mvvm.data.network.service.last_fm.services.ArtistsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicViewModel : ViewModel(), LifecycleEventObserver {

    companion object {
        private const val TAG = "MusicViewHolder"
    }

    private var artistsService: ArtistsService? = null

    val artistsLiveData = MutableLiveData<List<Artist>>()

    fun setArtistService(service: ArtistsService) {
        this.artistsService = service
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                println("ON_CREATE")
                getArtists()
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

    private fun getArtists() {
        viewModelScope.launch(Dispatchers.IO) {
            val artists = artistsService?.getTopArtists()?.artists?.artist ?: listOf()
            artistsLiveData.postValue(artists)
        }
    }
}