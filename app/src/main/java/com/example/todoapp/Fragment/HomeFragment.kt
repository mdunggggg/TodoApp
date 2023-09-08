package com.example.todoapp.Fragment

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Adapter.RecyclerViewAdapter.CategoryHomeAdapter
import com.example.todoapp.Adapter.RecyclerViewAdapter.HomeTaskAdapter
import com.example.todoapp.Interfaces.IItemTaskListener
import com.example.todoapp.Model.CategoryAndTask
import com.example.todoapp.Model.Subtask
import com.example.todoapp.Model.Task
import com.example.todoapp.ViewModel.CategoryViewModel
import com.example.todoapp.ViewModel.TaskViewModel
import com.example.todoapp.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar


class HomeFragment() : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var onItemClick: IItemTaskListener
    private val homeTaskAdapter: HomeTaskAdapter by lazy {
        HomeTaskAdapter { task: Task ->
            goToDetailFragment(task)
        }
    }
    private val categoryHomeAdapter : CategoryHomeAdapter by lazy {
        CategoryHomeAdapter{ titleCategory ->
            goToTaskByCategoryFragment(titleCategory)
        }
    }
    private val taskViewModel : TaskViewModel by activityViewModels {
        TaskViewModel.TaskViewModelFactory(requireActivity().application)
    }
    private val categoryViewModel : CategoryViewModel by activityViewModels{
        CategoryViewModel.CategoryViewModelFactory(requireActivity().application)
    }
    companion object{
        const val TAG = "HomeFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initComponent()
        taskViewModel.getAllTasksOrderByFinish().observe(
            viewLifecycleOwner
        ) {
            homeTaskAdapter.submitList(it)
        }
        taskViewModel.getCategoryWithTasks().observe(viewLifecycleOwner){
            val listData : MutableList<CategoryAndTask> = mutableListOf()
            for(category in it){
                var totalFinishedTask = 0
                var totalTask = 0
                for(task in category.tasks){
                    if (task.isFinish) totalFinishedTask += 1
                }
                totalTask = category.tasks.size
                listData.add(CategoryAndTask(category.category.titleCategory, totalTask, totalFinishedTask, category.category.color))
            }
            categoryHomeAdapter.submitList(listData.toList())
        }
        setUpSwipeAction()
    }
    private fun initComponent(){
        binding.rvTaskHome.adapter = homeTaskAdapter
        binding.rvCategoryHome.adapter = categoryHomeAdapter
    }
    private fun goToDetailFragment(task: Task){
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToDetailTaskFragment(task)
        )
    }
    private fun goToTaskByCategoryFragment(titleCategory : String){
        categoryViewModel.getCategoryByTitle(titleCategory).observe(viewLifecycleOwner){ category ->
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToTaskOfCategoryFragment(category)
            )
        }
    }
    private fun setUpSwipeAction(){
        val touchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.RIGHT
            ){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false;
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val task = homeTaskAdapter.currentList[position]
                    taskViewModel.deleteTask(task)
                    Snackbar.make(binding.root, "Task deleted", Snackbar.LENGTH_LONG).apply {
                        setAction("Undo"){
                            taskViewModel.insertTask(task)
                        }
                        show()
                    }
                }
            }

        )
        touchHelper.attachToRecyclerView(binding.rvTaskHome)
    }


}