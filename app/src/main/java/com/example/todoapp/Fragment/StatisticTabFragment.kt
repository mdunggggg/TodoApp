package com.example.todoapp.Fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.example.todoapp.Adapter.RecyclerViewAdapter.ItemCategoryStatisticAdapter
import com.example.todoapp.Adapter.RecyclerViewAdapter.ProgressBarAdapter
import com.example.todoapp.Model.CategoryAndTask
import com.example.todoapp.Model.TypeStatistic
import com.example.todoapp.R
import com.example.todoapp.ViewModel.CategoryViewModel
import com.example.todoapp.ViewModel.TaskViewModel
import com.example.todoapp.databinding.FragmentStatisticTabBinding
import java.time.LocalDate

class StatisticTabFragment(

) : Fragment() {
    private lateinit var binding : FragmentStatisticTabBinding
    private lateinit var typeStatistic: TypeStatistic
    @RequiresApi(Build.VERSION_CODES.O)
    private val day = LocalDate.now()
    private lateinit var rangeDate: LocalDate
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticTabBinding.inflate(inflater, container, false)

        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRangeDate()
        binding.rvCategoryStatistic.adapter = itemCategoryStatisticAdapter
        binding.rvProgressBar.adapter = progressBarAdapter
        taskViewModel.getAllTasksInRange(rangeDate.toString()).observe(viewLifecycleOwner){ tasks ->
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
                    if(task.dueDate > rangeDate.toString()){
                        totalTask += 1
                        if (task.isFinish) totalFinishedTask += 1
                    }
                }
                listData.add(CategoryAndTask(category.category.titleCategory, totalTask, totalFinishedTask, category.category.color))
            }
            itemCategoryStatisticAdapter.submitList(listData.toList())
            progressBarAdapter.submitList(listData.toList())
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initRangeDate() {
        typeStatistic = requireArguments().getSerializable("type") as TypeStatistic ?: TypeStatistic.ALL_TIME
        rangeDate = when (typeStatistic) {
            TypeStatistic.WEEKLY -> {
                day.minusDays(day.dayOfWeek.value.toLong() - 1)
            }
            TypeStatistic.MONTHLY -> {
                day.minusDays(day.dayOfMonth.toLong() - 1)
            }
            TypeStatistic.YEARLY -> {
                day.minusDays(day.dayOfYear.toLong() - 1)
            }
            else -> {
                LocalDate.of(2021, 1, 1)
            }
        }
        Log.d("StatisticTabFragment", "initRangeDate: $rangeDate")
    }

}