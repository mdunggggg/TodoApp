package com.example.todoapp

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.NavController
import com.example.todoapp.Fragment.DetailTaskFragment
import com.example.todoapp.Fragment.MainFragment
import com.example.todoapp.Model.Task
import com.example.todoapp.ViewModel.TaskViewModel
import com.example.todoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

//    private val taskViewModel : TaskViewModel by viewModels(){
//        TaskViewModel.TaskViewModelFactory(application)
//    }


    private val taskViewModel : TaskViewModel by viewModels(){
        TaskViewModel.TaskViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        with(window){
//            setFlags(
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//            )
//        }
    }
}

