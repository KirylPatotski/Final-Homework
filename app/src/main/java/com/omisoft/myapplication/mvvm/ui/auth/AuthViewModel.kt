package com.omisoft.myapplication.mvvm.ui.auth

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omisoft.myapplication.mvvm.data.network.service.auth.NetworkAuthService
import com.omisoft.myapplication.mvvm.data.storage.UserStorage
import com.omisoft.myapplication.mvvm.data.storage.preferences.AppPreferences
import java.io.*

class AuthViewModel(
    private val authModel: NetworkAuthService,
    private val storageModel: UserStorage,
    private val preferences: AppPreferences,
) : ViewModel(), LifecycleObserver {

    companion object {
        private const val TAG = "AuthViewModel"
    }

    val isLoginSuccessLiveData = MutableLiveData<Boolean>()
    val isLoginFailedLiveData = MutableLiveData<Boolean>()
    val showProgressLiveData = MutableLiveData<Boolean>()
    val hideProgressLiveData = MutableLiveData<Boolean>()
    val titleLiveData = MutableLiveData<String>()

    val emailLiveData = MutableLiveData<String>()
    val passwordLiveData = MutableLiveData<String>()
    val saveCredentialsCheckedLiveData = MutableLiveData<Boolean>()

    fun onLoginClicked(email: String, password: String) {
//      Сообщаем нашему view, в нашем случае MvvmActivity, что нужно показать прогресс
        showProgressLiveData.postValue(true)
        val token = authModel.onLoginClicked(email, password)

//      Сообщаем нашему view, в нашем случае MvvmActivity, что нужно спрятать прогресс
        hideProgressLiveData.postValue(true)
        if (token != null) {
            saveToken(token)
            saveCredentials(email, password)
            isLoginSuccessLiveData.postValue(true)
        } else {
            isLoginFailedLiveData.postValue(true)
        }
    }

    fun setSaveCredentialsSelected(isSelected: Boolean) {
        preferences.setSaveCredentialsSelected(isSelected)
    }

    fun saveCredentialsToFile(email: String, password: String, file: File) {
        var fos: FileOutputStream? = null

        try {
            file.createNewFile()

            fos = FileOutputStream(file)
            fos.write("$email, $password".toByteArray())
        } catch (error: Exception) {
            Log.e(TAG, error.message ?: "")
        } finally {
            fos?.close()
            getCredentialsFromFile(file)
        }
    }

    private fun getCredentialsFromFile(file: File) {
        var fis: FileInputStream? = null

        try {
            fis = FileInputStream(file)
            val isr = InputStreamReader(fis)
            val bsr = BufferedReader(isr)
            val stringBuilder = StringBuilder()
            val iterator = bsr.lineSequence().iterator()

            while (iterator.hasNext()) {
                stringBuilder.append(iterator.next())
            }

            val result = stringBuilder.toString()

            if (result.isNotBlank()) {
                val credentials = result.split(",")

                val email = credentials[0]
                val password = credentials[1]

                println("CREDENTIALS: email = $email, password = $password")
            }
        } catch (error: Exception) {
            Log.e(TAG, error.message ?: "")
        } finally {
            fis?.close()
        }
    }

    private fun saveCredentials(email: String, password: String) {
        preferences.let {
            if (it.isSaveCredentialsSelected()) {
                it.saveLogin(email)
                it.savePassword(password)
            }
        }
    }

    private fun saveToken(token: String) {
        preferences.saveToken(token)
    }

    fun fetchStoredData() {
        preferences.let {
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