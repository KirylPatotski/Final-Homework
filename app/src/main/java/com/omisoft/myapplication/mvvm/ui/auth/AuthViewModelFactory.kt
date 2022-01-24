package com.omisoft.myapplication.mvvm.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.omisoft.myapplication.mvvm.data.network.service.auth.NetworkAuthService
import com.omisoft.myapplication.mvvm.data.storage.UserStorage
import com.omisoft.myapplication.mvvm.data.storage.preferences.AppPreferences

class AuthViewModelFactory(
    private val authModel: NetworkAuthService,
    private val storageModel: UserStorage,
    private val preferences: AppPreferences,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (AuthViewModel(authModel, storageModel, preferences) as T)
}