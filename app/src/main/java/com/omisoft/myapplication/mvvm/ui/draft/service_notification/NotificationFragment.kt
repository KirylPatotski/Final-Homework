package com.omisoft.myapplication.mvvm.ui.draft.service_notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.omisoft.myapplication.R
import com.omisoft.myapplication.mvp.success.SuccessActivity
import com.omisoft.myapplication.mvvm.utils.services.AppBinderI
import com.omisoft.myapplication.mvvm.utils.services.AppBoundService

class NotificationFragment : Fragment() {
    companion object {
        private const val TAG = "NotificationFragment"
        private const val CHANNEL_ID = "notification_channel_id"
        private const val CHANNEL_ID_ALARM = "notification_channel_id_alarm"
    }

    private var appBinderI: AppBinderI? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName?, binder: IBinder?) {
            binder?.let {
                appBinderI = (it as AppBoundService.AppBoundServiceBinder).getAppBoundServiceI()
                appBinderI?.goToBound()
            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.d(TAG, "Service disconnected unexpectedly")
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<AppCompatButton>(R.id.button_show_toast).setOnClickListener {
            appBinderI?.showToast()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels()
        }
        createNotifications()

//      Запуск ForegroundService
//        requireContext().startService(Intent(requireContext(), ProgressForegroundService::class.java))
    }

    override fun onStart() {
        super.onStart()
        requireContext().bindService(Intent(requireContext(), AppBoundService::class.java), connection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        requireContext().unbindService(connection)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannels() {
        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelName1 = getString(R.string.notification_name)
        val channelDescription1 = getString(R.string.notification_description)
        val importance1 = NotificationManager.IMPORTANCE_HIGH

        val channel1 = NotificationChannel(CHANNEL_ID, channelName1, importance1).apply {
            description = channelDescription1
        }

        notificationManager.createNotificationChannel(channel1)

//      Created Alarm Notification channel
        val channelNameAlarm = getString(R.string.notification_name_alarm)
        val channelDescriptionAlarm = getString(R.string.notification_description_alarm)
        val importanceAlarm = NotificationManager.IMPORTANCE_HIGH

        val channelAlarm = NotificationChannel(CHANNEL_ID_ALARM, channelNameAlarm, importanceAlarm).apply {
            description = channelDescriptionAlarm
        }

        notificationManager.createNotificationChannel(channelAlarm)
    }

    private fun createNotifications() {
//      Show notifications with delay
        Handler(Looper.myLooper()!!).postDelayed({
            val notificationManager = NotificationManagerCompat.from(requireContext())
            val activityIntent = Intent(requireContext(), SuccessActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(requireContext(), 0, activityIntent, FLAG_CANCEL_CURRENT)

            val notification = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("New message")
                .setContentText("Hello!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
            notificationManager.notify(0, notification)

//      Created Custom Notification channel
            val remoteViews = RemoteViews(requireActivity().packageName, R.layout.layout_custom_notification)
            remoteViews.setImageViewResource(R.id.image, R.drawable.italy)
            remoteViews.setTextViewText(R.id.title, "Title of the notification")
            remoteViews.setTextViewText(R.id.description, "Description of the notification")

            val notification2 = NotificationCompat.Builder(requireContext(), CHANNEL_ID_ALARM)
                .setCustomContentView(remoteViews)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build()

//          Кастомная реализация Notification. Раскомментировать для просмотра.
//            notificationManager.notify(1, notification2)
        }, 5000)
    }
}