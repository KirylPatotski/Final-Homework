package com.omisoft.myapplication.mvvm.ui.auth

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omisoft.myapplication.mvvm.model.network.NetworkAuthService
import com.omisoft.myapplication.mvvm.model.network.NetworkAuthServiceImpl
import com.omisoft.myapplication.mvvm.model.storage.LocalStorageModel
import com.omisoft.myapplication.mvvm.model.storage.UserStorage
import com.omisoft.myapplication.mvvm.model.storage.preferences.AppPreferences

class AuthViewModel : ViewModel(), LifecycleObserver {
    val isLoginSuccessLiveData = MutableLiveData<Unit>()
    val isLoginFailedLiveData = MutableLiveData<Unit>()
    val showProgressLiveData = MutableLiveData<Unit>()
    val hideProgressLiveData = MutableLiveData<Unit>()
    val titleLiveData = MutableLiveData<String>()

    val emailLiveData = MutableLiveData<String>()
    val passwordLiveData = MutableLiveData<String>()
    val saveCredentialsCheckedLiveData = MutableLiveData<Boolean>()

    private val authModel: NetworkAuthService = NetworkAuthServiceImpl()
    private val storageModel: UserStorage = LocalStorageModel()
    private var preferences: AppPreferences? = null

    fun setSharedPreferences(preferences: AppPreferences) {
        this.preferences = preferences
    }

    fun onLoginClicked(email: String, password: String) {
//      Сообщаем нашему view, в нашем случае MvvmActivity, что нужно показать прогресс
        showProgressLiveData.postValue(Unit)

//      Здесь мы делаем задержку, чтобы показать прогресс на протяжении 3000 миллисекунд (3 секунды).
//      Это мы делаем штучно, на самом деле штучную задержку делать не надо)
        Handler(Looper.getMainLooper()).postDelayed({
//          titleLiveData.postValue("Main Page")
            val token = authModel.onLoginClicked(email, password)

//      Сообщаем нашему view, в нашем случае MvvmActivity, что нужно спрятать прогресс
            hideProgressLiveData.postValue(Unit)
            if (token != null) {
                saveToken(token)
                saveCredentials(email, password)
                isLoginSuccessLiveData.postValue(Unit)
            } else {
                isLoginFailedLiveData.postValue(Unit)
            }
        }, 3000)
    }

    fun setSaveCredentialsSelected(isSelected: Boolean) {
        preferences?.setSaveCredentialsSelected(isSelected)
    }

    private fun saveCredentials(email: String, password: String) {
        preferences?.let {
            if (it.isSaveCredentialsSelected()) {
                it.saveLogin(email)
                it.savePassword(password)
            }
        }
    }

    private fun saveToken(token: String) {
        preferences?.saveToken(token)
    }

    fun fetchStoredData() {
        preferences?.let {
            if (it.isSaveCredentialsSelected()) {
                emailLiveData.postValue(it.getLogin())
                passwordLiveData.postValue(it.getPassword())
                saveCredentialsCheckedLiveData.postValue(true)
            }
        }
    }

    fun setUpdatedEmail(email: String) {
        if (email != emailLiveData.value) {
            emailLiveData.value = email
        }
    }

    fun setUpdatedPassword(password: String) {
        if (password != passwordLiveData.value) {
            passwordLiveData.value = password
        }
    }
}