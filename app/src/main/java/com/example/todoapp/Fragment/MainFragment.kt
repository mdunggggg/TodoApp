package com.example.todoapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.todoapp.Adapter.ViewPagerAdapter.FragmentMainViewPager
import com.example.todoapp.Interfaces.IAddTaskListener
import com.example.todoapp.Interfaces.IItemTaskListener
import com.example.todoapp.Model.Task
import com.example.todoapp.ViewModel.TaskViewModel
import com.example.todoapp.databinding.FragmentMainBinding
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener

class MainFragment : Fragment() {
    private lateinit var binding : FragmentMainBinding
    private lateinit var pagerAdapter: FragmentMainViewPager
    private lateinit var viewPager: ViewPager2
    private lateinit var bubbleNavigationView: BubbleNavigationLinearView
    private val taskViewModel : TaskViewModel by activityViewModels(){
        TaskViewModel.TaskViewModelFactory(requireActivity().application)
    }
    // private val taskViewModel : TaskViewModel by activityViewModels()
    private lateinit var goToDetailTaskFragment : ((Task) -> Unit)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }
    companion object{
        const val TAG = "MainFragment"
        @JvmStatic
        fun newInstance(goToOnDetailTaskFragment: ((Task) -> Unit)) =
            MainFragment().apply {
                this.goToDetailTaskFragment = goToOnDetailTaskFragment
            }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initBehavior()
        initComponent()
        initViewPager()
    }
    private fun initComponent(){
        viewPager = binding.viewPager
        bubbleNavigationView = binding.bottomNavigationViewLinear
        pagerAdapter = FragmentMainViewPager(childFragmentManager, lifecycle, arrayListOf(
            HomeFragment.newInstance(object : IItemTaskListener{
                override fun onClickItemTask(task: Task) {
                    goToDetailTaskFragment.invoke(task)
                }

            }), CalendarFragment(), StatisticFragment(), SettingFragment()
        ))

        viewPager.adapter = pagerAdapter
    }
    private fun initBehavior(){

        binding.btAddTask.setOnClickListener {
            AddTaskDialogFragment(object : IAddTaskListener {
                override fun onAddTask(task: Task) {
                    taskViewModel.insertTask(task)
                }
            }).show(parentFragmentManager, AddTaskDialogFragment.TAG)
        }
    }
    private fun initViewPager(){
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