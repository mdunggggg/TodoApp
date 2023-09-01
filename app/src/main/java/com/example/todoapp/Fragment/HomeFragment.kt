package com.example.todoapp.Fragment

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.Adapter.RecyclerViewAdapter.HomeTaskAdapter
import com.example.todoapp.Interfaces.IItemTaskListener
import com.example.todoapp.Model.Subtask
import com.example.todoapp.Model.Task
import com.example.todoapp.ViewModel.TaskViewModel
import com.example.todoapp.databinding.FragmentHomeBinding


class HomeFragment() : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var onItemClick: IItemTaskListener
    private val homeTaskAdapter: HomeTaskAdapter by lazy {
        HomeTaskAdapter { task: Task ->
            goToDetailFragment(task)
        }
    }
    private val taskViewModel : TaskViewModel by activityViewModels {
        TaskViewModel.TaskViewModelFactory(requireActivity().application)
    }
    companion object{
        const val TAG = "HomeFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initComponent()
        taskViewModel.getAllTasks().observe(
            viewLifecycleOwner
        ) {
            homeTaskAdapter.submitList(it)
        }
    }
    private fun initComponent(){
        binding.rvTaskHome.adapter = homeTaskAdapter
    }
    private fun goToDetailFragment(task: Task){
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToDetailTaskFragment(task)
        )
    }


}