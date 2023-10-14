package com.example.todoapp.Fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.example.todoapp.Adapter.RecyclerViewAdapter.ItemCategoryStatisticAdapter
import com.example.todoapp.Adapter.RecyclerViewAdapter.ProgressBarAdapter
import com.example.todoapp.Model.CategoryAndTask
import com.example.todoapp.Model.TypeStatistic
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticTabBinding.inflate(inflater, container, false)

        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRangeDate()
        initComponent()
        observeData()

    }

    private fun observeData() {
        taskViewModel.getAllTasksInRange(rangeDate.toString()).observe(viewLifecycleOwner){ tasks ->
            val totalFinishedTask = tasks.filter {
                it.isFinish && !it.isStored
            }.size
            val totalTask = tasks.filter {
                !it.isStored
            }.size
            binding.apply {
                tvTotalTask.text = totalTask.toString()
                if(totalTask == 0) tvPercentTask.text = "0"
                else{
                    tvPercentTask.text = "${totalFinishedTask * 100 / totalTask}"
                }
                tvFinishedTask.text = totalFinishedTask.toString()
            }
        }
        taskViewModel.getCategoryWithTasks().observe(viewLifecycleOwner){
            val listData : MutableList<CategoryAndTask> = mutableListOf()
            for(category in it){
                val totalFinishedTask = category.tasks.filter { task ->
                    task.isFinish && !task.isStored && task.dueDate >= rangeDate.toString()
                }.size
                val totalTask = category.tasks.filter { task ->
                    !task.isStored && task.dueDate >= rangeDate.toString()
                }.size
                listData.add(CategoryAndTask(category.category.titleCategory, totalTask, totalFinishedTask, category.category.color))
            }
            itemCategoryStatisticAdapter.submitList(listData.toList())
            progressBarAdapter.submitList(listData.toList())
            if(listData.isEmpty()){
                binding.emptyListBg.visibility = View.VISIBLE
            }
            else{
                binding.emptyListBg.visibility = View.GONE
            }
        }
    }

    private fun initComponent() {
        binding.rvCategoryStatistic.adapter = itemCategoryStatisticAdapter
        binding.rvProgressBar.adapter = progressBarAdapter

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initRangeDate() {
        typeStatistic = requireArguments().getSerializable("type") as TypeStatistic
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
    }

}