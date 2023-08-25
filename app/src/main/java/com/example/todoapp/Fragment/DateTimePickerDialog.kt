package com.example.todoapp.Fragment

import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.todoapp.Interfaces.ITimeListener
import com.example.todoapp.R
import com.example.todoapp.Utils.DateTimeUtils
import com.example.todoapp.databinding.FragmentDateTimePickerDialogBinding
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class DateTimePickerDialog(
    private val timeSelected : ITimeListener
) : DialogFragment() {
    private lateinit var binding: FragmentDateTimePickerDialogBinding
    private var time: LocalTime? = null

    private var date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDate.now()
    } else {
        TODO("VERSION.SDK_INT < O")
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        binding = FragmentDateTimePickerDialogBinding.inflate(layoutInflater)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initBehavior()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            updateTime()
        }
        val dialog = AlertDialog.Builder(requireContext()).setView(binding.root)
            .setTitle(getString(R.string.pick_a_date_and_time))
            .setPositiveButton(getString(R.string.save)
            ) { _, _ -> timeSelected.onDateTimeSelected(date, time) }
            .setNegativeButton(getString(R.string.cancel), null)
        return dialog.create()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initBehavior(){
        binding.apply {
            btSetDueTime.setOnClickListener{
                onSetTime()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun onSetTime(){
        val timepicker = TimePickerDialog(
            requireContext(),
            onTimeSet,
            time?.hour ?: LocalTime.now().hour,
            time?.minute ?: LocalTime.now().minute,
            true
        )
        if (time != null)
            timepicker.setButton(
                DialogInterface.BUTTON_NEUTRAL,
                getString(R.string.clear)
            ) { _, which ->
                if (which == DialogInterface.BUTTON_NEUTRAL) {
                    time = null
                    updateTime()
                }
            }
        timepicker.show()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private val onTimeSet = { _: TimePicker, hour: Int, minute: Int ->
        time = LocalTime.of(hour, minute)
        updateTime()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTime() {
        val timeForTimePicker = if (time == null) getString(R.string.set_time)
        else time!!.format(
            DateTimeFormatter.ofPattern(DateTimeUtils.patternTime)
        )
        binding.btSetDueTime.text = timeForTimePicker
    }

}