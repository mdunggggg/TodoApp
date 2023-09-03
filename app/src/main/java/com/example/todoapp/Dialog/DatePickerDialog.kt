package com.example.todoapp.Dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.todoapp.Interfaces.IDateListener
import com.example.todoapp.databinding.FragmentDatePickerDialogBinding
import com.google.android.material.datepicker.MaterialDatePicker


class DatePickerDialog(
    private val onDatePicker : IDateListener
) : DialogFragment() {
    private lateinit var binding: FragmentDatePickerDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDatePickerDialogBinding.inflate(inflater, container, false)
        initComponent()
        return binding.root
    }
    private fun initComponent(){
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setTitleText("Select date")
            .build()
        datePicker.addOnPositiveButtonClickListener {
            onDatePicker.onDateSelected(datePicker.headerText)
            dismiss()
         }
        datePicker.addOnDismissListener {
            dismiss()
        }
        datePicker.show(childFragmentManager, "DatePickerDialog")
    }
}