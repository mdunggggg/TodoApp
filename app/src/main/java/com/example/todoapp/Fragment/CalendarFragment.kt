package com.example.todoapp.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentCalendarBinding

import java.util.Calendar
import java.util.Date

class CalendarFragment : Fragment() {
    private lateinit var binding: FragmentCalendarBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("Fragment", "CalendarFragment onDestroy: ")
    }
}