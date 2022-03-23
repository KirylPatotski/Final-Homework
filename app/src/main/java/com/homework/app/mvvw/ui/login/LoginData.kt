package com.homework.app.mvvw.ui.login

import android.util.Log
import androidx.core.util.PatternsCompat
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.homework.app.mvvw.AppPreferences
import java.io.*

class AuthVariables(private val authModel: LoginValid, private val preferences: AppPreferences,) : ViewModel(), LifecycleObserver {

    companion object {
        private const val TAG = "AuthViewModel"
    }

    val isLoginSuccessLiveData = MutableLiveData<Boolean>()
    val isLoginFailedLiveData = MutableLiveData<Boolean>()
    val showProgressLiveData = MutableLiveData<Boolean>()
    val hideProgressLiveData = MutableLiveData<Boolean>()

    val emailLiveData = MutableLiveData<String>()
    val passwordLiveData = MutableLiveData<String>()
    val confirmLiveData = MutableLiveData<String>()
    val saveCredentialsCheckedLiveData = MutableLiveData<Boolean>()

    fun onLoginClicked(email: String, password: String) {
        showProgressLiveData.postValue(true)
        val token = authModel.onLoginClicked(email, password, confirm = password)
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

    fun writeFile(email: String, password: String, file: File) {
        var fileOutputStream: FileOutputStream? = null
        try {
            file.createNewFile()
            fileOutputStream = FileOutputStream(file)
            fileOutputStream.write("$email, $password".toByteArray())
        } catch (error: Exception) {
            println("Something went wrong")
        } finally {
            fileOutputStream?.close()
            readFile(file)
        }
    }

    private fun readFile(file: File) {
        var fileInputStream: FileInputStream? = null

        try {
            fileInputStream = FileInputStream(file)
            val isr = InputStreamReader(fileInputStream)
            val bsr = BufferedReader(isr)
            val stringBuilder = StringBuilder()
            val iterator = bsr.lineSequence().iterator()

            while (iterator.hasNext()) {
                stringBuilder.append(iterator.next())
            }

            val result = stringBuilder.toString()

            if (result.isNotBlank()) {
                val credentials = result.split(",")
            }
        } catch (error: Exception) {
            Log.e(TAG, error.message ?: "")
        } finally {
            fileInputStream?.close()
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
                var password = it.getPassword()
                passwordLiveData.postValue(password)
                confirmLiveData.postValue(password)
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

    fun setUpdatedConfirm(password: String) {
        if (password != passwordLiveData.value) {
            passwordLiveData.value = password
        }
    }

}

fun String?.isEmailValid(): Boolean {
    return if (this != null && this.isNotBlank()) {
        isNotBlank() && PatternsCompat.EMAIL_ADDRESS.matcher(this).matches()
    } else {
        false
    }
}

fun String?.PasswordEqualsConfirmPassword(actualPassword: String): Boolean {
    return this == actualPassword
}

fun String?.isPasswordValid(): Boolean {
    return if (this != null && this.isNotBlank()) {
        isNotBlank() && this.length > 5
    } else {
        false
    }
}

@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory(private val authModel: LoginValid, private val preferences: AppPreferences, ) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) = (AuthVariables(authModel, preferences) as T)
}


class LoginValid {

    fun onLoginClicked(email: String, password: String, confirm: String): String? {
        val isEmailValid = email.isEmailValid()
        val isPasswordValid = password.isPasswordValid()
        val isConfirmValid = confirm.PasswordEqualsConfirmPassword(password)


        return if (isEmailValid && isPasswordValid && isConfirmValid) {
            onLogin(email, password)
        } else {
            null
        }
    }

    private fun onLogin(email: String, password: String): String {
        return "token"
    }

}

