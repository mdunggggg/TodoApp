package com.example.todoapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.todoapp.Adapter.ActivityMainViewPager
import com.example.todoapp.Fragment.AddTaskDialogFragment
import com.example.todoapp.Fragment.CalendarFragment
import com.example.todoapp.Fragment.HomeFragment
import com.example.todoapp.Fragment.SettingFragment
import com.example.todoapp.Fragment.StatisticFragment
import com.example.todoapp.databinding.ActivityMainBinding
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener

class MainActivity : AppCompatActivity() {
    private lateinit var pagerAdapter: ActivityMainViewPager
    private lateinit var binding : ActivityMainBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var bubbleNavigationView: BubbleNavigationLinearView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
        initComponent()
        initBehavior()
        initViewPager()
    }
    private fun initComponent(){
        viewPager = binding.viewPager
        bubbleNavigationView = binding.bottomNavigationViewLinear
        binding.btAddTask.setOnClickListener{
            AddTaskDialogFragment().show(supportFragmentManager, "AddTaskDialogFragment")
        }
    }
    private fun initBehavior(){
            pagerAdapter = ActivityMainViewPager(supportFragmentManager, lifecycle, arrayListOf(
                HomeFragment(), CalendarFragment(),StatisticFragment(), SettingFragment()))
    }
    private fun initViewPager(){
        viewPager.adapter = pagerAdapter
        viewPager.apply {
            setPageTransformer( ViewPager2.PageTransformer { page, position ->
                page.pivotX = (if (position < 0) 0 else page.width).toFloat()
                page.scaleX = if (position < 0) 1f + position else 1f - position
            })
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    bubbleNavigationView.setCurrentActiveItem(position)
                }
            })
        }

        bubbleNavigationView.apply {
            setNavigationChangeListener { _, position ->
                viewPager.setCurrentItem(position, true)
            }
            setNavigationChangeListener(BubbleNavigationChangeListener { _: View?, position: Int ->
                viewPager.setCurrentItem(
                    position,
                    true
                )
            })
        }
    }

}