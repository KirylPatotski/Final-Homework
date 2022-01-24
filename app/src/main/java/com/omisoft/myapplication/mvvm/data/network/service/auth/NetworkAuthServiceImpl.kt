package com.omisoft.myapplication.mvvm.data.network.service.auth

import com.omisoft.myapplication.mvvm.utils.extensions.isEmailValid
import com.omisoft.myapplication.mvvm.utils.extensions.isPasswordValid

class NetworkAuthServiceImpl : NetworkAuthService {
    override fun onLoginClicked(email: String, password: String): String? {
        val isEmailValid = email.isEmailValid()
        val isPasswordValid = password.isPasswordValid()

        return if (isEmailValid && isPasswordValid) {
            onLogin(email, password)
        } else {
            null
        }
    }

    override fun updateUserData(data: Any) {
        TODO("Not yet implemented")
    }

    fun removeUser() {
        TODO("Not yet implemented")
    }

    private fun onLogin(email: String, password: String): String {
        return "token"
    }
}