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
import com.example.todoapp.Adapter.RecyclerViewAdapter.SubtasksAdapter
import com.example.todoapp.Model.Subtask
import com.example.todoapp.Model.Task
import com.example.todoapp.R
import com.example.todoapp.Utils.DateTimeUtils
import com.example.todoapp.ViewModel.TaskViewModel
import com.example.todoapp.databinding.FragmentDetailTaskBinding


class DetailTaskFragment : Fragment() {
    private lateinit var binding : FragmentDetailTaskBinding
    private lateinit var task : Task
    private val subtasksAdapter : SubtasksAdapter by lazy {
        SubtasksAdapter { _: Subtask ->
            taskViewModel.updateTask(task)

        }
    }
    private val taskViewModel : TaskViewModel by activityViewModels {
        TaskViewModel.TaskViewModelFactory(requireActivity().application)
    }
    companion object{
        const val TAG = "DetailTaskFragment"
        @JvmStatic
        fun newInstance(task : Task)
            =  DetailTaskFragment().apply {
                this.task = task
             }
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
        initComponent()
        initBehavior()
    }
    private fun initComponent(){
        binding.apply {
            tvTaskName.text = task.title
            tvTaskDescription.text = task.content
            tvTaskDueDate.text = DateTimeUtils.formatDateTime(task.dueDate, task.dueTime)
            rvSubtasks.adapter = subtasksAdapter
        }
        subtasksAdapter.updateData(task.subtasks)
    }
    private fun initBehavior(){
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
       binding.edAddSubtask.onDone {
           addSubtask()
       }
    }
    private fun addSubtask(){
        val subtask = task.subtasks.toMutableList()
        subtask.add(Subtask(binding.edAddSubtask.text.toString(), false))
        binding.edAddSubtask.apply {
            text.clear()
            clearFocus()
        }
        task.subtasks = subtask
        taskViewModel.updateTask(task)
        subtasksAdapter.updateData(task.subtasks)
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
}