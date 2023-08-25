package com.example.todoapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todoapp.Interfaces.ITimeListener
import com.example.todoapp.Model.Category
import com.example.todoapp.R
import com.example.todoapp.Utils.DateTimeUtils
import com.example.todoapp.databinding.FragmentAddTaskDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate
import java.time.LocalTime

class AddTaskDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddTaskDialogBinding
    private var date = ""
    private var time = ""
    private lateinit var category: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTaskDialogBinding.inflate(inflater, container, false)
        initComponent()
        initBehavior()
        return binding.root
    }
    private fun initComponent(){
        binding.apply{
            btSetDueDate.setOnClickListener{
                setDueDate()
            }
        }
    }
    private fun initBehavior(){

    }
    private fun setDueDate() {
        val dialog = DateTimePickerDialog(object : ITimeListener {

            override fun onDateTimeSelected(date: LocalDate?, time: LocalTime?) {
                setDueDate(date, time)
            }
        }).show(childFragmentManager, "DateTimePickerDialog")
    }
    private fun setDueDate(date: LocalDate?, time: LocalTime?){
        this.date = date?.toString() ?: ""
        this.time = time?.toString() ?: ""
        binding.btSetDueDate.text = DateTimeUtils.formatDateTime(date, time)
    }


}