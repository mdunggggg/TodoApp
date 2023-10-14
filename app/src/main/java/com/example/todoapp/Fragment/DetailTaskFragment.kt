package com.example.todoapp.Fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.Adapter.RecyclerViewAdapter.SubtasksAdapter
import com.example.todoapp.Dialog.CategoryPickerDialog
import com.example.todoapp.Interfaces.ICategoryListener
import com.example.todoapp.Model.Subtask
import com.example.todoapp.Model.Task
import com.example.todoapp.R
import com.example.todoapp.Utils.ColorUtils
import com.example.todoapp.Utils.DateTimeUtils
import com.example.todoapp.Utils.KeyBoardUtils
import com.example.todoapp.Utils.KeyBoardUtils.onDone
import com.example.todoapp.ViewModel.DetailTaskViewModel
import com.example.todoapp.ViewModel.DetailTaskViewModelFactory
import com.example.todoapp.ViewModel.TaskViewModel
import com.example.todoapp.databinding.FragmentDetailTaskBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalTime


class DetailTaskFragment : Fragment() {
    private lateinit var binding : FragmentDetailTaskBinding
    private lateinit var task : Task
    private val args : DetailTaskFragmentArgs by navArgs()
    private lateinit var subtasksAdapter: SubtasksAdapter
    private var color = 0
    private val taskViewModel : TaskViewModel by activityViewModels {
        TaskViewModel.TaskViewModelFactory(requireActivity().application)
    }
    private val detailTaskViewModel by lazy {
            ViewModelProvider(this,
                DetailTaskViewModelFactory(
                    task)
            )[DetailTaskViewModel::class.java]
    }

    companion object{
        const val TAG = "DetailTaskFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailTaskBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setUpOnBackPress()
        return binding.root
    }
    private fun setUpOnBackPress(){
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            callback
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receiveData()
        initComponent()
        initBehavior()

    }
    private fun receiveData(){
        task = args.taskArgs

    }
    private fun initComponent(){
        subtasksAdapter = SubtasksAdapter(
            onUpdate = { position ->
                onUpdateSubtask(position)
            },
            onRemove = { position ->
                onRemoveSubtask(position)
            }
        )
        subtasksAdapter.updateData(task.subtasks)
        binding.apply {
            tvTaskName.setText(task.title)
            tvTaskDescription.setText(task.content)
            tvTaskDueDate.text = DateTimeUtils.formatDateToPattern(task.dueDate, DateTimeUtils.PatternDate.DEFAULT_PATTERN_DATE.pattern, DateTimeUtils.PatternDate.DEFAULT_PATTERN_DATE_2.pattern)
            tvTaskDueTime.text = task.dueTime
            rvSubtasks.adapter = subtasksAdapter
            cbTaskStatus.isChecked = task.isFinish
            chipCategory.text = task.titleCategory
            color = task.color
            if(task.isFinish){
                cvStatus.setCardBackgroundColor(ColorUtils.getColor("#DFF4EA"))
                tvStatus.setTextColor(ColorUtils.getColor("#039855"))
                tvStatus.text = "Completed"
            } else {
                cvStatus.setCardBackgroundColor(ColorUtils.getColor("#D9E8F4"))
                tvStatus.setTextColor(ColorUtils.getColor("#025A9A"))
                tvStatus.text = "On progress"
            }
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initBehavior(){
        binding.toolbar.setNavigationOnClickListener {
            onBack()
        }
       binding.edAddSubtask.onDone {
           hideSoftKeyBoard()
           addSubtask()
       }
        detailTaskViewModel._subTasks.observe(viewLifecycleOwner) {
            subtasksAdapter.updateData(it)
        }
        binding.tvTaskDueDate.setOnClickListener{
            setDueDate()
        }
        binding.tvTaskDueTime.setOnClickListener{
            setDueTime()
        }
        binding.chipCategory.setOnClickListener{
            val category = CategoryPickerDialog(object : ICategoryListener {
                override fun onClickCategory(nameCategory: String, colorCategory : Int) {
                    binding.chipCategory.text = nameCategory
                    color = colorCategory
                }
            })
            category.show(childFragmentManager, CategoryPickerDialog.TAG)
        }
    }
    private fun addSubtask(){
        val subtask = Subtask(binding.edAddSubtask.text.toString(), false)
        detailTaskViewModel.addSubtask(subtask)
        binding.edAddSubtask.apply {
            text.clear()
            clearFocus()
        }

   }


    private fun onBack(){
        updateDetailTaskViewModel()
        if (detailTaskViewModel.isChanged()){
            context?.let {
                MaterialAlertDialogBuilder(it)
                    .setTitle("Save changes")
                    .setMessage("Do you want to save changes?")
                    .setIcon(R.drawable.ic_save)
                    .setPositiveButton("Yes"){ _, _ ->
                        taskViewModel.updateTask(detailTaskViewModel.getNewTask())
                        findNavController().navigateUp()
                    }
                    .setNegativeButton("No"){ _, _ ->
                        findNavController().navigateUp()
                    }
                    .setNeutralButton("Cancel"){ dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }?:{
                Toast.makeText(context, "An error has occurred!! Please restart the app.", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            findNavController().navigateUp()
        }
    }
    private fun updateDetailTaskViewModel(){
        detailTaskViewModel.apply {
            newTitle = binding.tvTaskName.text.toString().trim()
            newDescription = binding.tvTaskDescription.text.toString().trim()
            newIsFinished = binding.cbTaskStatus.isChecked
            newCategory = binding.chipCategory.text.toString()
            newColor = color

        }
    }
    private fun onUpdateSubtask(position : Int){
        detailTaskViewModel.onUpdatedSubtask(position)
    }
    private fun onRemoveSubtask(position : Int){
        detailTaskViewModel.onRemoveSubtask(position)
    }
    private fun hideSoftKeyBoard(){
        KeyBoardUtils.hideSoftKeyboard(binding.root, requireActivity())
    }
    private fun setDueDate(){
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setTitleText("Select date")
            .build()
        datePicker.addOnPositiveButtonClickListener {
            setDueDate(datePicker.headerText)
        }
        datePicker.show(childFragmentManager, "DatePickerDialog")
    }
    private fun setDueDate(date : String){
        detailTaskViewModel.newDueDate = DateTimeUtils.formatDateToPattern(
            date,
            DateTimeUtils.PatternDate.CUSTOM_PATTERN_DATE.pattern,
            DateTimeUtils.PatternDate.DEFAULT_PATTERN_DATE.pattern
        )
        binding.tvTaskDueDate.text =  date
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
        timePicker.addOnDismissListener {
        }
        timePicker.show(childFragmentManager, "TimePickerDialog")
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDueTime(hour : Int, minute : Int){
        detailTaskViewModel.newDueTime = DateTimeUtils.formatTime(hour, minute)
        binding.tvTaskDueTime.text = DateTimeUtils.formatTime(hour, minute)
    }

}