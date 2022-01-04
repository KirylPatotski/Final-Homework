package com.omisoft.myapplication.mvvm.utils.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.omisoft.myapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProgressForegroundService : Service() {
    companion object {
        private const val TAG = "ProgressForeground"
        private const val CHANNEL_ID = "notification_channel_id_foreground"
    }

    private var notificationBuilder: NotificationCompat.Builder? = null
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels()
        }

        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Progress notification")
            .setContentText("Current progress")
            .setProgress(100, 0, false)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)


        startForeground(0, notificationBuilder?.build())
        updateProgress()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        serviceJob.cancel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannels() {
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelName1 = getString(R.string.notification_name_progress)
        val channelDescription1 = getString(R.string.notification_description_progress)
        val importance1 = NotificationManager.IMPORTANCE_DEFAULT

        val channel1 = NotificationChannel(CHANNEL_ID, channelName1, importance1).apply {
            description = channelDescription1
        }

        notificationManager.createNotificationChannel(channel1)
    }

    private fun updateProgress() {
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        serviceScope.launch(Dispatchers.IO) {
            var i = 0
            while (i < 100) {
                Thread.sleep(1000)
                i += 10
                Log.d(TAG, "updateProgress: $i")
                notificationBuilder?.let { builder ->
                    builder
                        .setContentText("Current progress: ${i}%")
                        .setProgress(100, i, false)

                    notificationManager.notify(0, builder.build())
                }
            }
            stopForeground(true)
            notificationManager.cancelAll()
            stopSelf()
        }
    }
}