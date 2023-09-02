package com.example.todoapp.Dialog

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.example.todoapp.Interfaces.IAddTaskListener
import com.example.todoapp.Interfaces.ICategoryListener
import com.example.todoapp.Interfaces.ITimeListener
import com.example.todoapp.Model.Category
import com.example.todoapp.Model.Task
import com.example.todoapp.Utils.DateTimeUtils
import com.example.todoapp.ViewModel.CategoryViewModel
import com.example.todoapp.databinding.FragmentAddTaskDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate
import java.time.LocalTime
import kotlin.math.log

class AddTaskDialog(private val addTaskListener: IAddTaskListener) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddTaskDialogBinding
    private var date = ""
    private var time = ""
    private lateinit var category: String
    private val categoryViewModel : CategoryViewModel by activityViewModels {
        CategoryViewModel.CategoryViewModelFactory(requireActivity().application)
    }
    companion object{
        const val TAG = "AddTaskDialogFragment"
    }
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
            btSetCategory.setOnClickListener {
                setCategory()
            }
            btAddTask.setOnClickListener{
                addTask()
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

    private fun setCategory(){
        val category = CategoryPickerDialog(object : ICategoryListener{
            override fun onClickCategory(nameCategory: String) {
                binding.btSetCategory.text = nameCategory
                category = nameCategory
            }
        })
        category.show(childFragmentManager, CategoryPickerDialog.TAG)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun addTask(){
        if (binding.etTaskName.text.toString().isEmpty() || binding.etTaskDescription.text.toString().isEmpty() || binding.btSetCategory.text.toString().isEmpty() || binding.btSetDueDate.text.toString().isEmpty()){
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
                dueTime = time
            )
        )
        updateCategory()
        dismiss()
    }
    private fun updateCategory(){
        val category = categoryViewModel.getCategoryByTitle(category).observe(
            viewLifecycleOwner
        ) {
            it?.let {
                categoryViewModel.updateCategory(
                    it.copy(
                        numTask = it.numTask + 1
                    )
                )
            }
        }
    }

}