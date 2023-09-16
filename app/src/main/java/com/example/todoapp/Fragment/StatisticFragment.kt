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
import com.example.todoapp.Adapter.ViewPagerAdapter.FragmentStatisticViewPager
import com.example.todoapp.Model.CategoryAndTask
import com.example.todoapp.Model.TypeStatistic
import com.example.todoapp.ViewModel.CategoryViewModel
import com.example.todoapp.ViewModel.TaskViewModel
import com.example.todoapp.databinding.FragmentStatisticBinding
import com.google.android.material.tabs.TabLayoutMediator


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
        val tabType = listOf(TypeStatistic.WEEKLY, TypeStatistic.MONTHLY, TypeStatistic.YEARLY, TypeStatistic.ALL_TIME)
        val tabTitle = listOf("Weekly", "Monthly", "Yearly", "All")
        binding.viewPager.adapter = FragmentStatisticViewPager(childFragmentManager, lifecycle, tabType)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabType[position].type
        }.attach()
    }
}