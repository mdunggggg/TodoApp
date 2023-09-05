package com.example.todoapp.Fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.example.todoapp.Adapter.RecyclerViewAdapter.SubtasksAdapter
import com.example.todoapp.Dialog.DatePickerDialog
import com.example.todoapp.Dialog.TimePickerDialog
import com.example.todoapp.Interfaces.IDateListener
import com.example.todoapp.Interfaces.ITimeListener
import com.example.todoapp.Model.Subtask
import com.example.todoapp.Model.Task
import com.example.todoapp.R
import com.example.todoapp.Utils.DateTimeUtils
import com.example.todoapp.Utils.KeyBoardUtils
import com.example.todoapp.Utils.KeyBoardUtils.onDone
import com.example.todoapp.ViewModel.DetailTaskViewModel
import com.example.todoapp.ViewModel.DetailTaskViewModelFactory
import com.example.todoapp.ViewModel.TaskViewModel
import com.example.todoapp.databinding.FragmentDetailTaskBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class DetailTaskFragment : Fragment() {
    private lateinit var binding : FragmentDetailTaskBinding
    private lateinit var task : Task
    private val args : DetailTaskFragmentArgs by navArgs()
    private lateinit var subtasksAdapter: SubtasksAdapter
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
    ): View? {
        binding = FragmentDetailTaskBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        return binding.root
    }

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
            tvTaskDueDate.text = DateTimeUtils.formatToCustomPattern(task.dueDate)
            tvTaskDueTime.text = task.dueTime
            rvSubtasks.adapter = subtasksAdapter
            cbTaskStatus.isChecked = task.isFinish
        }

    }
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
            MaterialAlertDialogBuilder(requireContext())
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
        DatePickerDialog(
            object : IDateListener {
                override fun onDateSelected(date: String) {
                    setDueDate(date)
                }
            }
        ).show(parentFragmentManager, "SET_DATE")
    }
    private fun setDueDate(date : String){
        detailTaskViewModel.newDueDate = DateTimeUtils.formatToDefaultPattern(date)
        binding.tvTaskDueDate.text =  date
    }
    private fun setDueTime(){
        TimePickerDialog(
            object : ITimeListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onTimeSelected(hour: Int, minute: Int) {
                    setDueTime(hour, minute)
                }
            }
        ).show(parentFragmentManager, "SET_TIME")
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDueTime(hour : Int, minute : Int){
        detailTaskViewModel.newDueTime = DateTimeUtils.formatTime(hour, minute)
        binding.tvTaskDueTime.text = DateTimeUtils.formatTime(hour, minute)
    }

}