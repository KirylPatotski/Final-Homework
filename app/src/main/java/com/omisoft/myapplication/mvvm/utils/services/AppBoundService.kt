package com.omisoft.myapplication.mvvm.utils.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.widget.Toast

class AppBoundService : Service(), AppBinderI {
    override fun onBind(p0: Intent?): IBinder {
        return AppBoundServiceBinder()
    }

    override fun showToast() {
        Toast.makeText(
            this, "Toast from AppBoundService showed", Toast.LENGTH_LONG
        ).show()
    }

    override fun goToForeground() {

    }

    override fun goToBound() {

    }

    inner class AppBoundServiceBinder : Binder() {
        fun getAppBoundServiceI(): AppBinderI = this@AppBoundService
    }
}

interface AppBinderI {
    fun showToast()

    fun goToForeground()

    fun goToBound()
}