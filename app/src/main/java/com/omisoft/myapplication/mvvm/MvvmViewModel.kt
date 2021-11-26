package com.omisoft.myapplication.mvvm

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MvvmViewModel : ViewModel() {
    val isLoginSuccessLiveData = MutableLiveData<Unit>()
    val isLoginFailedLiveData = MutableLiveData<Unit>()
    val showProgressLiveData = MutableLiveData<Unit>()
    val hideProgressLiveData = MutableLiveData<Unit>()
    val titleLiveData = MutableLiveData<String>()

    private val authModel = MvvmAuthModel()

    fun onLoginClicked(email: String, password: String) {
//      Сообщаем нашему view, в нашем случае MvvmActivity, что нужно показать прогресс
        showProgressLiveData.postValue(Unit)

//      Здесь мы делаем задержку, чтобы показать прогресс на протяжении 3000 миллисекунд (3 секунды).
//      Это мы делаем штучно, на самом деле штучную задержку делать не надо)
        Handler(Looper.getMainLooper()).postDelayed({
//          titleLiveData.postValue("Main Page")
            val isSuccess = authModel.onLoginClicked(email, password)

//      Сообщаем нашему view, в нашем случае MvvmActivity, что нужно спрятать прогресс
            hideProgressLiveData.postValue(Unit)
            if (isSuccess) {
                isLoginSuccessLiveData.postValue(Unit)
            } else {
                isLoginFailedLiveData.postValue(Unit)
            }
        }, 3000)
    }
}