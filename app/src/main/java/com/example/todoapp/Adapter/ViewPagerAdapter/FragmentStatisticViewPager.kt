package com.example.todoapp.Adapter.ViewPagerAdapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.todoapp.Fragment.StatisticTabFragment
import com.example.todoapp.Model.TypeStatistic

class FragmentStatisticViewPager(fragmentManager: FragmentManager, lifecycle: Lifecycle, private val typeFragment : List<TypeStatistic>) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return typeFragment.size
    }

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putSerializable("type", typeFragment[position])
        val fragment = StatisticTabFragment()
        fragment.arguments = bundle
        return fragment
    }
}