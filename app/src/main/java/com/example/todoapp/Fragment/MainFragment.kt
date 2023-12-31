package com.example.todoapp.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.todoapp.Adapter.ViewPagerAdapter.FragmentMainViewPager
import com.example.todoapp.Dialog.AddTaskDialog
import com.example.todoapp.Dialog.PomodoroTimePickerDialog
import com.example.todoapp.Interfaces.IAddTaskListener
import com.example.todoapp.Model.Task
import com.example.todoapp.Model.TypeNotification
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
        binding.btAddTask.setOnClickListener {
            onBtnAddTaskClick()
        }
        binding.btTrash.setOnClickListener {
            onBtnTrashClick()
        }
        binding.btPomodoro.setOnClickListener {
            onBtnPomodoroClick()
        }
    }

    private fun onBtnPomodoroClick() {
        PomodoroTimePickerDialog { pomodoroTime, shortBreakTime, longBreakTime ->
            goToPomodoroFragment(pomodoroTime, shortBreakTime, longBreakTime)
        }.show(parentFragmentManager, PomodoroTimePickerDialog.TAG)
    }

    private fun onBtnTrashClick() {
        goToTrashFragment()
    }

    private fun goToTrashFragment() {
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToTrashFragment()
        )
    }
    private fun goToPomodoroFragment(pomodoroTime : Int, shortBreakTime : Int, longBreakTime : Int) {
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToPomodoroFragment(
                pomodoroTime,
                shortBreakTime,
                longBreakTime
            )
        )
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
                btAdd.startAnimation(rotateClockwise)
                btAddTask.visibility = View.GONE
                btTrash.visibility = View.GONE
                btPomodoro.visibility = View.GONE
                btAddTask.startAnimation(fadeOut)
                btTrash.startAnimation(fadeOut)
                btPomodoro.startAnimation(fadeOut)
            }
            else{
                btAdd.startAnimation(rotateCounterClockwise)
                btAddTask.visibility = View.VISIBLE
                btTrash.visibility = View.VISIBLE
                btPomodoro.visibility = View.VISIBLE
                btPomodoro.startAnimation(fadeIn)
                btAddTask.startAnimation(fadeIn)
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
            .putString("key", TypeNotification.TaskNotification.name)
            .putString("title", task.title)
            .putString("content", task.content)
            .putString("task", taskJson)
            .build()
        val notificationRequest : WorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()
        context?.let {
            WorkManager.getInstance(it).enqueue(notificationRequest)
        }?:{
            Toast.makeText(context, "An error has occurred!! Please restart the app.", Toast.LENGTH_SHORT).show()
        }
    }




}