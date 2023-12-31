package com.example.todoapp.Dialog

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.todoapp.Interfaces.IAddTaskListener
import com.example.todoapp.Interfaces.ICategoryListener
import com.example.todoapp.Interfaces.IDateListener
import com.example.todoapp.Interfaces.ITimeListener
import com.example.todoapp.Model.Task
import com.example.todoapp.Utils.DateTimeUtils
import com.example.todoapp.Utils.KeyBoardUtils
import com.example.todoapp.Utils.KeyBoardUtils.onDone
import com.example.todoapp.databinding.FragmentAddTaskDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalDate
import java.time.LocalTime

class AddTaskDialog(private val addTaskListener: IAddTaskListener) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddTaskDialogBinding
    private var date = ""
    private var time = ""
    private var color = 0
    private var category = ""
    companion object{
        const val TAG = "AddTaskDialogFragment"
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskDialogBinding.inflate(inflater, container, false)
        initBehavior()
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initBehavior(){
        binding.apply{
            etTaskDescription.apply {
                onDone {
                    KeyBoardUtils.hideSoftKeyboard(binding.root, requireActivity())
                }
            }
            etTaskName.apply {
                onDone {
                    KeyBoardUtils.hideSoftKeyboard(binding.root, requireActivity())
                }
            }
            btSetDueDate.setOnClickListener{
                setDueDate()
            }
            btSetDueTime.setOnClickListener{
                setDueTime()
            }
            btSetCategory.setOnClickListener {
                setCategory()
            }
            btAddTask.setOnClickListener{
                addTask()
            }

        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDueDate() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setTitleText("Select date")
            .build()
        datePicker.addOnPositiveButtonClickListener {
            setDueDate(datePicker.headerText)
        }
        datePicker.show(childFragmentManager, "DatePickerDialog")
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDueTime(){
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(LocalTime.now().hour)
            .setMinute(LocalTime.now().minute)
            .setTitleText("Select time")
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .build()
        timePicker.addOnPositiveButtonClickListener {
            setDueTime(timePicker.hour, timePicker.minute)
        }

        timePicker.show(childFragmentManager, "TimePickerDialog")
    }

    private fun setDueDate(date : String){
        this.date = DateTimeUtils.formatDateToPattern(
            date,
            DateTimeUtils.PatternDate.CUSTOM_PATTERN_DATE.pattern,
            DateTimeUtils.PatternDate.DEFAULT_PATTERN_DATE.pattern
        )
        binding.btSetDueDate.text = date
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDueTime(hour : Int, minute : Int){
        this.time = DateTimeUtils.formatTime(hour, minute)
        binding.btSetDueTime.text = this.time
    }

    private fun setCategory(){
        val category = CategoryPickerDialog(object : ICategoryListener{
            override fun onClickCategory(nameCategory: String, colorCategory : Int) {
                binding.btSetCategory.text = nameCategory
                category = nameCategory
                color = colorCategory
            }
        })
        category.show(childFragmentManager, CategoryPickerDialog.TAG)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun addTask(){
        if (this.time == "" || this.date == "" || category == "" || binding.etTaskName.text.toString() == "" || binding.etTaskDescription.text.toString() == ""){
            Toast.makeText(context, "Please fill all information", Toast.LENGTH_SHORT).show()
            return
        }
        addTaskListener.onAddTask(
            Task(
                title = binding.etTaskName.text.toString(),
                content = binding.etTaskDescription.text.toString(),
                titleCategory = category,
                dateCreated = LocalDate.now().toString(),
                dueDate = date,
                color = color,
                dueTime = time
            )
        )
        dismiss()
    }

}