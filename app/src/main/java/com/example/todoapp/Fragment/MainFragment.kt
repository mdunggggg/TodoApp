package com.example.todoapp.Fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.todoapp.Adapter.ViewPagerAdapter.FragmentMainViewPager
import com.example.todoapp.Dialog.AddTaskDialog
import com.example.todoapp.Interfaces.IAddTaskListener
import com.example.todoapp.Model.Task
import com.example.todoapp.MyApplication
import com.example.todoapp.R
import com.example.todoapp.Utils.DateTimeUtils
import com.example.todoapp.Utils.StringUtils
import com.example.todoapp.ViewModel.TaskViewModel
import com.example.todoapp.Worker.NotificationWorker
import com.example.todoapp.databinding.FragmentMainBinding
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MainFragment : Fragment() {
    private lateinit var binding : FragmentMainBinding
    private lateinit var pagerAdapter: FragmentMainViewPager
    private lateinit var viewPager: ViewPager2
    private lateinit var bubbleNavigationView: BubbleNavigationLinearView
    private val taskViewModel : TaskViewModel by activityViewModels(){
        TaskViewModel.TaskViewModelFactory(requireActivity().application)
    }
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

        binding.btAddTask.setOnClickListener {
            AddTaskDialog(object : IAddTaskListener {
                override fun onAddTask(task: Task) {
                    taskViewModel.insertTask(task)
                    setTimeNotification(task)
                }
            }).show(parentFragmentManager, AddTaskDialog.TAG)
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
            setNavigationChangeListener(BubbleNavigationChangeListener { _: View?, position: Int ->
                viewPager.setCurrentItem(
                    position,
                    true
                )
            })
        }
    }
    private fun testNotification(){
        val builder = NotificationCompat.Builder(requireContext(), MyApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Test notification")
            .setContentText("This is a test notification")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        with(NotificationManagerCompat.from(requireContext())){
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(MyApplication.getNotificationId(), builder.build())
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