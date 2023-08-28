package com.example.todoapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.todoapp.Fragment.DetailTaskFragment
import com.example.todoapp.Fragment.MainFragment
import com.example.todoapp.Model.Task
import com.example.todoapp.ViewModel.TaskViewModel
import com.example.todoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
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
        supportFragmentManager.commit {
            replace(
                binding.root.id,
                MainFragment.newInstance {
                     goToDetailTaskFragment(it)
                },
            )
            setReorderingAllowed(true)
            addToBackStack("MainFragment")
        }
    }
    private fun goToDetailTaskFragment(task: Task){
        supportFragmentManager.commit {
            replace(
                binding.root.id,
                DetailTaskFragment(),
            )
            setReorderingAllowed(true)
            addToBackStack("DetailTaskFragment")
        }
    }

}

