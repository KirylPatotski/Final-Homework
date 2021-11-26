package com.omisoft.myapplication.mvp

class MvpAuthModelImpl {

    fun onLoginClicked(email: String, password: String): Boolean {
        val isEmailValid = email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val passwordValid = password.isNotBlank() && password.length > 5

        return isEmailValid && passwordValid
    }
}