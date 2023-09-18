package com.example.todoapp.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.Adapter.RecyclerViewAdapter.HomeTaskAdapter
import com.example.todoapp.Model.Task
import com.example.todoapp.R
import com.example.todoapp.ViewModel.TaskViewModel
import com.example.todoapp.databinding.FragmentTrashBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class TrashFragment : Fragment() {
   private lateinit var binding : FragmentTrashBinding
    private val homeTaskAdapter: HomeTaskAdapter by lazy {
        HomeTaskAdapter { task: Task ->
            deleteOrRestoreTask(task)
        }
    }
    private val taskViewModel : TaskViewModel by activityViewModels {
        TaskViewModel.TaskViewModelFactory(requireActivity().application)
    }
    companion object{
        const val TAG = "TrashFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrashBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setUpOnBackPress()
        return binding.root
    }

    private fun setUpOnBackPress() {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initComponent()
        initBehavior()
    }

    private fun initBehavior() {
        binding.toolbar.setNavigationOnClickListener {
            onBack()
        }
    }

    private fun initComponent() {
        binding.rvTrash.adapter = homeTaskAdapter
        taskViewModel.getAllDeletedTasks().observe(viewLifecycleOwner){ tasks ->
            Log.d(TAG, "initComponent: $tasks")
            homeTaskAdapter.submitList(tasks)
        }
    }
    private fun deleteOrRestoreTask(task: Task){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete or Restore")
            .setMessage("Do you want to delete or restore this task?")
            .setPositiveButton("Delete"){ _, _ ->
                taskViewModel.deleteTask(task)
            }
            .setNegativeButton("Restore"){ _, _ ->
                taskViewModel.updateTask(task.copy(isStored = false))
            }
            .setNeutralButton("Cancel"){ dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    private fun onBack(){
        findNavController().navigateUp()
    }


}