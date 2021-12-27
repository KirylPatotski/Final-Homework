package com.omisoft.myapplication.mvvm.ui.draft.alarm

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.omisoft.myapplication.R
import java.util.*


class AlarmFragment : Fragment() {

    private lateinit var timePicker: TimePicker
    private lateinit var selectAlarm: AppCompatButton
    private lateinit var select: AppCompatButton
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var minutes = 0
    private var hours = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_alarm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiver = AlarmReceiver()
        requireContext().registerReceiver(receiver, IntentFilter("alarm receiver"))
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    timePicker
                } else {
                    Toast.makeText(requireContext(), "We have not access to storage", Toast.LENGTH_LONG).show()
                }
            }

        timePicker = view.findViewById(R.id.time_picker)
        selectAlarm = view.findViewById(R.id.button_set_alarm)
        select = view.findViewById(R.id.select)

        selectAlarm.setOnClickListener {
            checkAlarmPermission()
        }
        select.setOnClickListener {
            timePicker.isVisible = false
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, hours)
                set(Calendar.MINUTE, minutes)
            }
            scheduleAlarm(calendar.timeInMillis)
        }
    }

    private fun checkAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val isAlarmGranted = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            if (isAlarmGranted) {
                openTimePicker()
            } else {
                requestAlarmPermission()
            }
        } else {
            openTimePicker()
        }
    }

    private fun openTimePicker() {
        val now: Calendar = Calendar.getInstance()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.hour = now.get(Calendar.HOUR_OF_DAY)
            timePicker.minute = now.get(Calendar.MINUTE)
        } else {
            timePicker.currentHour = now.get(Calendar.HOUR_OF_DAY)
            timePicker.currentMinute = now.get(Calendar.MINUTE)
        }

        timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            this@AlarmFragment.minutes = minute
            this@AlarmFragment.hours = hourOfDay
        }
        timePicker.isVisible = true
    }

    private fun scheduleAlarm(time: Long) {
        val alarmManager =
            requireContext().getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val pendingIntent =
            PendingIntent.getBroadcast(
                requireContext(),
                0,
                Intent("alarm receiver"),
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        alarmManager?.setInexactRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun requestAlarmPermission() {
        requestPermissionLauncher.launch(Manifest.permission.SCHEDULE_EXACT_ALARM)
    }

    class AlarmReceiver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
//          TODO: Do something
        }
    }
}