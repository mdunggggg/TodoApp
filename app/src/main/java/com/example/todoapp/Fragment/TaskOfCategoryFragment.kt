package com.example.todoapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.Adapter.RecyclerViewAdapter.HomeTaskAdapter
import com.example.todoapp.Model.Category
import com.example.todoapp.Model.Task
import com.example.todoapp.ViewModel.TaskViewModel
import com.example.todoapp.databinding.FragmentTaskOfCategoryBinding


class TaskOfCategoryFragment : Fragment() {
    private lateinit var binding : FragmentTaskOfCategoryBinding
    private lateinit var category: Category
    private val taskAdapter: HomeTaskAdapter by lazy {
        HomeTaskAdapter { task: Task ->
            goToDetailFragment(task)
        }
    }
    private val taskViewModel : TaskViewModel by activityViewModels {
        TaskViewModel.TaskViewModelFactory(requireActivity().application)
    }
    private val args : TaskOfCategoryFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskOfCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        receiveData()
        initComponent()
        initBehavior()
        observeData()
    }

    private fun initBehavior() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeData() {
        taskViewModel.getAllTasksByCategory(category.titleCategory).observe(
            viewLifecycleOwner
        ) {
            taskAdapter.submitList(it)
        }
    }

    private fun initComponent() {
        binding.rvTaskHome.adapter = taskAdapter
        binding.toolbar.title = category.titleCategory

    }

    private fun receiveData(){
        category = args.categoryArgs
    }
    private fun goToDetailFragment(task: Task){
        findNavController().navigate(
            TaskOfCategoryFragmentDirections.actionTaskOfCategoryFragmentToDetailTaskFragment(task)
        )
    }


}