package com.example.todoapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.Adapter.RecyclerViewAdapter.TaskCalendarAdapter
import com.example.todoapp.Model.Task
import com.example.todoapp.R
import com.example.todoapp.Utils.DateTimeUtils
import com.example.todoapp.ViewModel.TaskViewModel
import com.example.todoapp.databinding.FragmentCalendarBinding
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import java.util.Calendar

class CalendarFragment : Fragment() {
    private lateinit var binding: FragmentCalendarBinding
    private val taskViewModel : TaskViewModel by activityViewModels{
        TaskViewModel.TaskViewModelFactory(requireActivity().application)
    }
    private val taskAdapter: TaskCalendarAdapter by lazy {
        TaskCalendarAdapter { task: Task ->
            goToDetailFragment(task)
        }
    }
    private val startDate: Calendar by lazy {
        Calendar.getInstance()
    }
    private val endDate: Calendar  by lazy {
        Calendar.getInstance()
    }
    private val calendar : Calendar by lazy {
        Calendar.getInstance()
    }
    private lateinit var horizontalCalendar: HorizontalCalendar
    companion object{
        const val TAG = "CalendarFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvTaskCalendar.adapter = taskAdapter
        setUpRecyclerView(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR))
        setUpCalendar()

    }
    private fun setUpCalendar(){
        startDate.add(Calendar.MONTH, -1)
        endDate.add(Calendar.MONTH, 1)
        horizontalCalendar = HorizontalCalendar.Builder(binding.root, R.id.calendarView)
            .range(startDate, endDate)
            .datesNumberOnScreen(7)
            .configure()
            . showTopText(false)
            .formatMiddleText("dd")
            .formatBottomText("EEE")
            .end()
            .build()
        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar?, position: Int) {
                setUpRecyclerView(
                    date?.get(Calendar.DAY_OF_MONTH)!!,
                    date.get(Calendar.MONTH) + 1,
                    date.get(Calendar.YEAR)
                )
            }

        }
    }
    private fun setUpRecyclerView(day : Int, month : Int, year : Int){
        val date = DateTimeUtils.formatToDefaultPattern(day, month, year)
        taskViewModel.getAllTasksByDate(date).observe(viewLifecycleOwner){
            taskAdapter.submitList(it)
        }
    }
    private fun goToDetailFragment(task: Task){
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToDetailTaskFragment(task)
        )
    }

}