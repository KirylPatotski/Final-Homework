package com.homework.app.mvvw.ui.music.fragment

import androidx.lifecycle.*
import com.homework.app.mvp.Artist
import com.homework.app.mvp.ArtistsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicViewModel : ViewModel(), LifecycleEventObserver {

    private var artistsService: ArtistsService? = null

    val artistsLiveData = MutableLiveData<List<Artist>>()

    fun setArtistService(service: ArtistsService) {
        this.artistsService = service
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val artists = artistsService?.getTopArtists()?.artists?.artist ?: listOf()
                    artistsLiveData.postValue(artists)
                }
            }
        }
    }

}