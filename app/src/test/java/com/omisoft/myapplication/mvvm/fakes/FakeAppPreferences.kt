package com.omisoft.myapplication.mvvm.fakes

import com.omisoft.myapplication.mvvm.data.storage.preferences.AppPreferences

class FakeAppPreferences : AppPreferences {
    override fun isSaveCredentialsSelected(): Boolean {
        return true
    }

    override fun setSaveCredentialsSelected(isSelected: Boolean) {}

    override fun saveLogin(login: String) {}

    override fun savePassword(password: String) {}

    override fun getLogin(): String = ""

    override fun getPassword(): String = ""

    override fun saveToken(token: String) {}

    override fun getToken(): String = ""
}