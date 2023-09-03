package com.example.todoapp.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.example.todoapp.Adapter.RecyclerViewAdapter.SubtasksAdapter
import com.example.todoapp.Model.Subtask
import com.example.todoapp.Model.Task
import com.example.todoapp.R
import com.example.todoapp.Utils.DateTimeUtils
import com.example.todoapp.ViewModel.DetailTaskViewModel
import com.example.todoapp.ViewModel.DetailTaskViewModelFactory
import com.example.todoapp.ViewModel.TaskViewModel
import com.example.todoapp.databinding.FragmentDetailTaskBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class DetailTaskFragment : Fragment() {
    private lateinit var binding : FragmentDetailTaskBinding
    private lateinit var task : Task
    private val args : DetailTaskFragmentArgs by navArgs()
    private val subtasksAdapter : SubtasksAdapter by lazy {
        SubtasksAdapter {
            onUpdateSubtask(it)
        }
    }
    private val taskViewModel : TaskViewModel by activityViewModels {
        TaskViewModel.TaskViewModelFactory(requireActivity().application)
    }
    private val detailTaskViewModel by lazy {
            ViewModelProvider(this,
                DetailTaskViewModelFactory(
                    task, requireActivity().application)
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
        subtasksAdapter.updateData(task.subtasks)
        binding.apply {
            tvTaskName.setText(task.title)
            tvTaskDescription.setText(task.content)
            tvTaskDueDate.text = DateTimeUtils.formatDateTime(task.dueDate, task.dueTime)
            rvSubtasks.adapter = subtasksAdapter
        }

    }
    private fun initBehavior(){
        binding.toolbar.setNavigationOnClickListener {
            onBack()
           // findNavController().navigateUp()
        }
       binding.edAddSubtask.onDone {
           addSubtask()
       }
        detailTaskViewModel._subTasks.observe(viewLifecycleOwner) {

            subtasksAdapter.updateData(it)
        }
    }
    private fun addSubtask(){
        val subtask = task.subtasks.toMutableList()
        subtask.add(Subtask(binding.edAddSubtask.text.toString(), false))
        binding.edAddSubtask.apply {
            detailTaskViewModel.addSubtask(Subtask(binding.edAddSubtask.text.toString(), false))
            text.clear()
            clearFocus()
        }

   }

    private fun EditText.onDone(callback : () -> Unit){
        setOnEditorActionListener { _, type, _ ->
            return@setOnEditorActionListener if (type == EditorInfo.IME_ACTION_DONE) {
                callback()
                true
            } else {
                false
            }
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
            newDueDate = task.dueDate
            newDueTime = task.dueTime
        }
    }
    private fun onUpdateSubtask(position : Int){
        detailTaskViewModel.onUpdatedSubtask(position)
    }

}