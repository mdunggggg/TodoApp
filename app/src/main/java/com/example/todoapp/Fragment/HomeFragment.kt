package com.example.todoapp.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.Adapter.RecyclerViewAdapter.CategoryHomeAdapter
import com.example.todoapp.Adapter.RecyclerViewAdapter.CategorySearchViewAdapter
import com.example.todoapp.Adapter.RecyclerViewAdapter.TaskSearchViewAdapter
import com.example.todoapp.Adapter.ViewPagerAdapter.FragmentHomeViewPager
import com.example.todoapp.Model.CategoryAndTask
import com.example.todoapp.Model.Task
import com.example.todoapp.ViewModel.CategoryViewModel
import com.example.todoapp.ViewModel.TaskViewModel
import com.example.todoapp.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment() : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private val categorySearchViewAdapter: CategorySearchViewAdapter by lazy {
        CategorySearchViewAdapter(onCountTask){
            goToTaskByCategoryFragment(it)
        }
    }
    private val taskSearchViewAdapter : TaskSearchViewAdapter by lazy {
        TaskSearchViewAdapter{ task ->
            goToDetailFragment(task)
        }
    }
    private val categoryHomeAdapter : CategoryHomeAdapter by lazy {
        CategoryHomeAdapter{ titleCategory ->
            goToTaskByCategoryFragment(titleCategory)
        }
    }
    private val taskViewModel : TaskViewModel by activityViewModels {
        TaskViewModel.TaskViewModelFactory(requireActivity().application)
    }
    private val categoryViewModel : CategoryViewModel by activityViewModels{
        CategoryViewModel.CategoryViewModelFactory(requireActivity().application)
    }
    companion object{
        const val TAG = "HomeFragment"
        enum class TypeView(val type : String){
            ON_PROGRESS("On progress"),
            FINISHED("Finished"),
        }
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
        initBehavior()
        initObserver()

    }

    private fun initObserver() {
        taskViewModel.getCategoryWithTasks().observe(viewLifecycleOwner){
            Log.d(TAG, "Observer task: $it")
            val listData : MutableList<CategoryAndTask> = mutableListOf()
            for(category in it){
                val totalFinishedTask = category.tasks.filter { task ->
                    task.isFinish && !task.isStored
                }.size
                val totalTask = category.tasks.filter { task ->
                    !task.isStored
                }.size
                listData.add(CategoryAndTask(category.category.titleCategory, totalTask, totalFinishedTask, category.category.color))
            }
            categoryHomeAdapter.submitList(listData.toList())
        }
//        categoryViewModel.getCategoryWithTasks().observe(viewLifecycleOwner) {
//            Log.d(TAG, "Observer category: $it")
//            val listData: MutableList<CategoryAndTask> = mutableListOf()
//            for (category in it) {
//                val totalFinishedTask = category.tasks.filter { task ->
//                    task.isFinish && !task.isStored
//                }.size
//                val totalTask = category.tasks.filter { task ->
//                    !task.isStored
//                }.size
//                listData.add(
//                    CategoryAndTask(
//                        category.category.titleCategory,
//                        totalTask,
//                        totalFinishedTask,
//                        category.category.color
//                    )
//                )
//            }
//            categoryHomeAdapter.submitList(listData.toList())
//        }
    }

    private fun initBehavior(){
        binding.searchView.editText.doOnTextChanged { text, _, _, _ ->
            if(text.isNullOrEmpty()) {
                taskViewModel.getAllUnFinishTasks().observe(
                    viewLifecycleOwner
                ) {
                    taskSearchViewAdapter.submitList(it)
                }
                categoryViewModel.getAllCategory().observe(viewLifecycleOwner){
                    categorySearchViewAdapter.submitList(it)
                }
            }
            else{
                taskViewModel.getTaskByTitle(text.toString()).observe(viewLifecycleOwner){
                    taskSearchViewAdapter.submitList(it)
                }
                categoryViewModel.getListCategoryByTitle(text.toString()).observe(viewLifecycleOwner){
                    categorySearchViewAdapter.submitList(it)
                }

            }
        }
    }
    private fun initComponent(){
        binding.rvCategoryHome.adapter = categoryHomeAdapter
        binding.rvTaskSearchView.adapter = taskSearchViewAdapter
        binding.rvCategorySearchView.adapter = categorySearchViewAdapter
        initViewPager()
    }
    private fun initViewPager(){
        val tabType = listOf(TypeView.ON_PROGRESS, TypeView.FINISHED)
        binding.viewPager.adapter = FragmentHomeViewPager(childFragmentManager, lifecycle, tabType)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabType[position].type
        }.attach()
    }
    private fun goToDetailFragment(task: Task){
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToDetailTaskFragment(task)
        )
    }
    private fun goToTaskByCategoryFragment(titleCategory : String){
        categoryViewModel.getCategoryByTitle(titleCategory).observe(viewLifecycleOwner){ category ->
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToTaskOfCategoryFragment(category)
            )
        }
    }
    private val onCountTask = { titleCategory : String ->
        categoryViewModel.getCategoryWithTasksByTitle(titleCategory).tasks.size.toString()
    }


}