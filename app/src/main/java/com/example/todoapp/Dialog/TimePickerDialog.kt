package com.example.todoapp.Dialog

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.todoapp.Interfaces.ITimeListener
import com.example.todoapp.databinding.FragmentTimePickerDialogBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalTime


class TimePickerDialog(
    private val onTimePicker : ITimeListener
) : DialogFragment() {
    private lateinit var binding : FragmentTimePickerDialogBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimePickerDialogBinding.inflate(inflater, container, false)
        initComponent()
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initComponent(){
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(LocalTime.now().hour)
            .setMinute(LocalTime.now().minute)
            .setTitleText("Select time")
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .build()
        timePicker.addOnPositiveButtonClickListener {
            onTimePicker.onTimeSelected(timePicker.hour, timePicker.minute)
            dismiss()
        }
        timePicker.addOnDismissListener {
            dismiss()
        }
        timePicker.show(childFragmentManager, "TimePickerDialog")
    }
}