package com.omisoft.myapplication.mvvm.ui.music.activity

import androidx.lifecycle.ViewModel
import com.omisoft.myapplication.mvvm.data.network.service.music.NetworkMusicService
import com.omisoft.myapplication.mvvm.data.network.service.music.NetworkMusicServiceImpl
import com.omisoft.myapplication.mvvm.data.storage.LocalStorageModel

class MusicViewModel : ViewModel() {
    private val authModel: NetworkMusicService = NetworkMusicServiceImpl()
    private val storageModel = LocalStorageModel()
}