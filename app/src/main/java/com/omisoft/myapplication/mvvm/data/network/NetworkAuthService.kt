package com.omisoft.myapplication.mvvm.data.network

interface NetworkAuthService {
    fun onLoginClicked(email: String, password: String): String?
    fun updateUserData(data: Any)
}