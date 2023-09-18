package com.example.todoapp.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Adapter.RecyclerViewAdapter.CategoryHomeAdapter
import com.example.todoapp.Adapter.RecyclerViewAdapter.CategorySearchViewAdapter
import com.example.todoapp.Adapter.RecyclerViewAdapter.HomeTaskAdapter
import com.example.todoapp.Adapter.RecyclerViewAdapter.TaskSearchViewAdapter
import com.example.todoapp.Model.Task
import com.example.todoapp.R
import com.example.todoapp.Utils.SwipeHelper
import com.example.todoapp.ViewModel.CategoryViewModel
import com.example.todoapp.ViewModel.TaskViewModel
import com.example.todoapp.databinding.FragmentHomeTabBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class HomeTabFragment : Fragment() {
   private lateinit var binding : FragmentHomeTabBinding
   private lateinit var typeView : HomeFragment.Companion.TypeView
    private val taskViewModel : TaskViewModel by activityViewModels {
        TaskViewModel.TaskViewModelFactory(requireActivity().application)
    }
    private val categoryViewModel : CategoryViewModel by activityViewModels{
        CategoryViewModel.CategoryViewModelFactory(requireActivity().application)
    }
    private val homeTaskAdapter: HomeTaskAdapter by lazy {
        HomeTaskAdapter { task: Task ->
            goToDetailFragment(task)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        typeView = requireArguments().getSerializable("type") as HomeFragment.Companion.TypeView
        when (typeView) {
            HomeFragment.Companion.TypeView.ON_PROGRESS -> {
                binding.rvTaskHome.adapter = homeTaskAdapter
                taskViewModel.getAllUnFinishTasks().observe(viewLifecycleOwner){ tasks ->
                    homeTaskAdapter.submitList(tasks)
                }
            }
            HomeFragment.Companion.TypeView.FINISHED -> {
                binding.rvTaskHome.adapter = homeTaskAdapter
                taskViewModel.getAllFinishTasks().observe(viewLifecycleOwner){ tasks ->
                    homeTaskAdapter.submitList(tasks)
                }
            }
        }
        setUpSwipeAction()
    }
    private fun goToDetailFragment(task: Task){
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToDetailTaskFragment(task)
        )
    }
    private fun setUpSwipeAction(){
        ItemTouchHelper(object : SwipeHelper(requireContext()){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val task = homeTaskAdapter.currentList[position]
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Move to trash")
                    .setMessage("Are you sure want to " +
                            "move this task to trash?")
                    .setPositiveButton("Yes"){ _, _ ->
                        taskViewModel.updateTask(task.copy(isStored = true))
                        Snackbar.make(binding.root, "Task is moved to trash", Snackbar.LENGTH_LONG).show()
                    }
                    .setNegativeButton("No"){ dialog, _ ->
                        dialog.dismiss()
                        homeTaskAdapter.notifyItemChanged(position)
                    }
                    .show()
            }
        }).attachToRecyclerView(binding.rvTaskHome)
    }


}