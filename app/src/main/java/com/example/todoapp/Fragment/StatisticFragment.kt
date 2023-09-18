package com.example.todoapp.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todoapp.Adapter.ViewPagerAdapter.FragmentStatisticViewPager
import com.example.todoapp.Model.TypeStatistic
import com.example.todoapp.databinding.FragmentStatisticBinding
import com.google.android.material.tabs.TabLayoutMediator


class StatisticFragment : Fragment() {
    private lateinit var binding : FragmentStatisticBinding
    companion object{
        const val TAG = "StatisticFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initComponent()
    }

    @SuppressLint("SetTextI18n")
    private fun initComponent(){
        val tabType = listOf(TypeStatistic.WEEKLY, TypeStatistic.MONTHLY, TypeStatistic.YEARLY, TypeStatistic.ALL_TIME)
        binding.viewPager.adapter = FragmentStatisticViewPager(childFragmentManager, lifecycle, tabType)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabType[position].type
        }.attach()
    }
}