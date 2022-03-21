package com.homework.app.mvvw.data

import android.content.Context
import android.content.SharedPreferences

class AppPreferencesImpl private constructor() : AppPreferences {
    companion object {
        private const val PREFERENCE_NAME = "AppPreferences"
        private const val PREFERENCE_IS_SAVE_CREDENTIALS_SELECTED = "PREFERENCE_IS_SAVE_CREDENTIALS_SELECTED"
        private const val PREFERENCE_LOGIN = "PREFERENCE_LOGIN"
        private const val PREFERENCE_PASSWORD = "PREFERENCE_PASSWORD"
        private const val PREFERENCE_TOKEN = "PREFERENCE_TOKEN"
        private const val FAVORITE_TOKEN = "PREFERENCE_TOKEN"

        private var instance: AppPreferences? = null
        private var settings: SharedPreferences? = null

        fun getInstance(context: Context): AppPreferences {
            if (instance == null) {
                instance = AppPreferencesImpl()
                settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            }
            return instance!!
        }
    }

    override fun isSaveCredentialsSelected(): Boolean {
        return settings?.getBoolean(PREFERENCE_IS_SAVE_CREDENTIALS_SELECTED, false) ?: false
    }

    override fun setSaveCredentialsSelected(isSelected: Boolean) =
        settings?.edit()?.putBoolean(PREFERENCE_IS_SAVE_CREDENTIALS_SELECTED, isSelected)?.apply() ?: Unit

    override fun saveLogin(login: String) =
        settings?.edit()?.putString(PREFERENCE_LOGIN, login)?.apply() ?: Unit

    override fun savePassword(password: String) =
        settings?.edit()?.putString(PREFERENCE_PASSWORD, password)?.apply() ?: Unit


    override fun getLogin(): String = settings?.getString(PREFERENCE_LOGIN, "") ?: ""

    override fun getPassword(): String = settings?.getString(PREFERENCE_PASSWORD, "") ?: ""

    override fun saveToken(token: String) = settings?.edit()?.putString(PREFERENCE_TOKEN, token)?.apply() ?: Unit

    override fun getToken(): String = settings?.getString(PREFERENCE_TOKEN, "") ?: ""


    override fun saveFavorite(str: String) {
        settings?.edit()?.putString(PREFERENCE_PASSWORD, str.toString())?.apply() ?: Unit
    }

    override fun getFavorite(): String? = settings?.getString(FAVORITE_TOKEN,"") ?: ""
}

interface AppPreferences {
    fun isSaveCredentialsSelected(): Boolean
    fun setSaveCredentialsSelected(isSelected: Boolean)

    fun saveLogin(login: String)
    fun savePassword(password: String)

    fun getLogin(): String
    fun getPassword(): String

    fun saveToken(token: String)
    fun getToken(): String

    fun saveFavorite(str: String)
    fun getFavorite(): String?
}