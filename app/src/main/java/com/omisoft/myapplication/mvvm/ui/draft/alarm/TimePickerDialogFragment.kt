package com.omisoft.myapplication.mvvm.ui.draft.alarm

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.omisoft.myapplication.mvvm.ui.models.alarm.SelectedTime
import java.util.*

class TimePickerDialogFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private val viewModel by activityViewModels<AlarmViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        viewModel.onTimeSelected(SelectedTime(hourOfDay, minute))
    }
}