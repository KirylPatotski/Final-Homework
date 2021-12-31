package com.omisoft.myapplication.mvvm.ui.draft.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.omisoft.myapplication.R
import java.util.*

class AlarmFragment : Fragment() {

    companion object {
        private const val TAG = "AlarmFragment"
        private const val FILTER_VALUE = "AlarmReceiver"
        private const val REQUEST_CODE = 101
    }

    private val viewModel by activityViewModels<AlarmViewModel>()
    private lateinit var buttonTimePicker: AppCompatButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_alarm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireContext().registerReceiver(AlarmBroadcast(), IntentFilter(FILTER_VALUE))

        buttonTimePicker = view.findViewById(R.id.button_open_tome_picker)

        initListeners()
        subscribeToLivedata()
    }

    private fun subscribeToLivedata() {
        viewModel.selectedTimeLiveData.observe(this, { selectedTime ->
            selectedTime?.let {
                val calendar = Calendar.getInstance().apply {
                    set(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DATE), it.hours, it.minutes)
                }
                val hours = calendar.get(Calendar.HOUR_OF_DAY)
                val minutes = calendar.get(Calendar.MINUTE)
                Log.d(TAG, "Selected time: $hours:$minutes")
                scheduleAlarm(calendar.timeInMillis)
            }
        })
    }

    private fun scheduleAlarm(time: Long) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = Intent(requireContext(), AlarmBroadcast::class.java).apply {
            action = FILTER_VALUE
        }

        val pendingIntent = PendingIntent.getBroadcast(requireContext(), REQUEST_CODE, intent, 0)
        alarmManager?.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    private fun initListeners() {
        buttonTimePicker.setOnClickListener {
            val timePicker = TimePickerDialogFragment()
            timePicker.show(requireActivity().supportFragmentManager, TAG)
        }
    }
}

class AlarmBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "Alarm received", Toast.LENGTH_LONG).show()
    }
}