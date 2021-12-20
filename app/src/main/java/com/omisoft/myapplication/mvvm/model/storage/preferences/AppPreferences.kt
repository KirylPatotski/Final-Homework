package com.omisoft.myapplication.mvvm.model.storage.preferences

interface AppPreferences {
    fun isSaveCredentialsSelected(): Boolean
    fun setSaveCredentialsSelected(isSelected: Boolean)

    fun saveLogin(login: String)
    fun savePassword(password: String)

    fun getLogin(): String
    fun getPassword(): String

    fun saveToken(token: String)
    fun getToken(): String
}