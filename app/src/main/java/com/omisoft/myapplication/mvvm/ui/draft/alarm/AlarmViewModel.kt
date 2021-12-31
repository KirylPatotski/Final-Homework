package com.omisoft.myapplication.mvvm.ui.draft.alarm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omisoft.myapplication.mvvm.ui.models.alarm.SelectedTime

class AlarmViewModel : ViewModel() {

    val selectedTimeLiveData by lazy { MutableLiveData<SelectedTime>() }

    fun onTimeSelected(selectedTime: SelectedTime) {
        selectedTimeLiveData.value = selectedTime
    }
}