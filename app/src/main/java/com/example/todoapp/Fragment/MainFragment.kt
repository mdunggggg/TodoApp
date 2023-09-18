package com.example.todoapp.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.todoapp.Adapter.ViewPagerAdapter.FragmentMainViewPager
import com.example.todoapp.Dialog.AddCategoryDialog
import com.example.todoapp.Dialog.AddTaskDialog
import com.example.todoapp.Interfaces.IAddCategoryListener
import com.example.todoapp.Interfaces.IAddTaskListener
import com.example.todoapp.Model.Category
import com.example.todoapp.Model.Task
import com.example.todoapp.R
import com.example.todoapp.Utils.DateTimeUtils
import com.example.todoapp.Utils.StringUtils
import com.example.todoapp.ViewModel.CategoryViewModel
import com.example.todoapp.ViewModel.TaskViewModel
import com.example.todoapp.Worker.NotificationWorker
import com.example.todoapp.databinding.FragmentMainBinding
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import java.util.concurrent.TimeUnit

class MainFragment : Fragment() {
    private lateinit var binding : FragmentMainBinding
    private lateinit var pagerAdapter: FragmentMainViewPager
    private lateinit var viewPager: ViewPager2
    private lateinit var bubbleNavigationView: BubbleNavigationLinearView

    private val rotateClockwise: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.rotate_clockwise
        )
    }
    private val rotateCounterClockwise: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.rotate_counter_clockwise
        )
    }
    private val fadeIn: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.fade_in
        )
    }
    private val fadeOut: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.fade_out
        )
    }
    private val taskViewModel : TaskViewModel by activityViewModels(){
        TaskViewModel.TaskViewModelFactory(requireActivity().application)
    }
    private val categoryViewModel : CategoryViewModel by activityViewModels {
        CategoryViewModel.CategoryViewModelFactory(requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }
    companion object{
        const val TAG = "MainFragment"
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
            HomeFragment(), CalendarFragment(), StatisticFragment(), SettingFragment()
        ))

        viewPager.adapter = pagerAdapter
    }
    private fun initBehavior(){
        binding.btAdd.setOnClickListener {
            onBtnAddClick()
        }
        binding.btAddCategory.setOnClickListener {
            //onBtnAddCategoryClick()
        }
        binding.btAddTask.setOnClickListener {
            onBtnAddTaskClick()
        }
        binding.btTrash.setOnClickListener {
            onBtnTrashClick()
        }
    }

    private fun onBtnTrashClick() {
        goToTrashFragment()
    }

    private fun goToTrashFragment() {
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToTrashFragment()
        )
    }

    private fun onBtnAddCategoryClick() {
        AddCategoryDialog(object : IAddCategoryListener {
            override fun onAddCategory(category: Category) {
                categoryViewModel.insertCategory(category)
            }
        }).show(parentFragmentManager, AddCategoryDialog.TAG)
    }

    private fun onBtnAddTaskClick() {
        AddTaskDialog(object : IAddTaskListener {
            override fun onAddTask(task: Task) {
                taskViewModel.insertTask(task)
                setTimeNotification(task)
            }
        }).show(parentFragmentManager, AddTaskDialog.TAG)
    }

    private fun onBtnAddClick() {
        binding.apply {
            if(btAddTask.visibility == View.VISIBLE){
                btAdd.startAnimation(rotateCounterClockwise)
                btAddTask.visibility = View.GONE
                btAddCategory.visibility = View.GONE
                btTrash.visibility = View.GONE
                btAddTask.startAnimation(fadeOut)
                btAddCategory.startAnimation(fadeOut)
                btTrash.startAnimation(fadeOut)
            }
            else{
                btAdd.startAnimation(rotateClockwise)
                btAddTask.visibility = View.VISIBLE
                btAddCategory.visibility = View.VISIBLE
                btTrash.visibility = View.VISIBLE
                btAddTask.startAnimation(fadeIn)
                btAddCategory.startAnimation(fadeIn)
                btTrash.startAnimation(fadeIn)
            }
        }
    }

    private fun initViewPager(){
        viewPager.apply {
            setPageTransformer { page, position ->
                page.pivotX = (if (position < 0) 0 else page.width).toFloat()
                page.scaleX = if (position < 0) 1f + position else 1f - position
            }
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
            setNavigationChangeListener { _: View?, position: Int ->
                viewPager.setCurrentItem(
                    position,
                    true
                )
            }
        }
    }
    private fun setTimeNotification(task : Task){
        val initialDelay = DateTimeUtils.getDelayTime(task.dueDate, task.dueTime)
        val taskJson = StringUtils.serializeToJson(task)
        val data = Data.Builder()
            .putString("title", task.title)
            .putString("content", task.content)
            .putString("task", taskJson)
            .build()
        val notificationRequest : WorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()
        WorkManager.getInstance(requireContext()).enqueue(notificationRequest)
    }


}