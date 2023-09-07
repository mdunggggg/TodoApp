package com.example.todoapp.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.todoapp.Adapter.RecyclerViewAdapter.ItemCategoryStatisticAdapter
import com.example.todoapp.Adapter.RecyclerViewAdapter.ProgressBarAdapter
import com.example.todoapp.Model.CategoryAndTask
import com.example.todoapp.ViewModel.CategoryViewModel
import com.example.todoapp.ViewModel.TaskViewModel
import com.example.todoapp.databinding.FragmentStatisticBinding


class StatisticFragment : Fragment() {
    private lateinit var binding : FragmentStatisticBinding
    private val taskViewModel : TaskViewModel by activityViewModels {
        TaskViewModel.TaskViewModelFactory(requireActivity().application)
    }
    private val itemCategoryStatisticAdapter : ItemCategoryStatisticAdapter by lazy {
        ItemCategoryStatisticAdapter()
    }
    private val progressBarAdapter : ProgressBarAdapter by lazy {
        ProgressBarAdapter()
    }
    private val categoryViewModel : CategoryViewModel by activityViewModels {
        CategoryViewModel.CategoryViewModelFactory(requireActivity().application)
    }
    companion object{
        const val TAG = "StatisticFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initComponent()
    }

    @SuppressLint("SetTextI18n")
    private fun initComponent(){
        binding.rvCategoryStatistic.adapter = itemCategoryStatisticAdapter
        binding.rvProgressBar.adapter = progressBarAdapter
        taskViewModel.getAllTasks().observe(viewLifecycleOwner){ tasks ->
                var totalFinishedTask = 0
                tasks.forEach {
                    if (it.isFinish) totalFinishedTask += 1
                }
                binding.apply {
                    tvTotalTask.text = tasks.size.toString()
                    if(tasks.isEmpty()) tvPercentTask.text = "0"
                    else{
                        tvPercentTask.text = "${totalFinishedTask * 100 / tasks.size}"
                    }
                    tvFinishedTask.text = totalFinishedTask.toString()
                }
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
            itemCategoryStatisticAdapter.submitList(listData.toList())
            progressBarAdapter.submitList(listData.toList())
        }
    }
}