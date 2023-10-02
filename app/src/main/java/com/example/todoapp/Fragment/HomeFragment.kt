package com.example.todoapp.Fragment

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todoapp.Adapter.RecyclerViewAdapter.CategoryHomeAdapter
import com.example.todoapp.Adapter.RecyclerViewAdapter.CategorySearchViewAdapter
import com.example.todoapp.Adapter.RecyclerViewAdapter.TaskSearchViewAdapter
import com.example.todoapp.Adapter.ViewPagerAdapter.FragmentHomeViewPager
import com.example.todoapp.DataStore.StoreToDo
import com.example.todoapp.Model.CategoryAndTask
import com.example.todoapp.Model.Task
import com.example.todoapp.Model.TypeStatus
import com.example.todoapp.R
import com.example.todoapp.ViewModel.CategoryViewModel
import com.example.todoapp.ViewModel.TaskViewModel
import com.example.todoapp.ViewModel.UserViewModel
import com.example.todoapp.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import java.time.LocalDate


class HomeFragment() : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var todoStore : StoreToDo
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
    private val userViewModel : UserViewModel by activityViewModels{
        UserViewModel.UserViewModelFactory(requireActivity().application)
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
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initComponent()
        initBehavior()
        initObserver()

    }

    private fun initObserver() {
        taskViewModel.getCategoryWithTasks().observe(viewLifecycleOwner) {
            val listData: MutableList<CategoryAndTask> = mutableListOf()
            for (category in it) {
                val totalFinishedTask = category.tasks.filter { task ->
                    task.isFinish && !task.isStored
                }.size
                val totalTask = category.tasks.filter { task ->
                    !task.isStored
                }.size
                listData.add(
                    CategoryAndTask(
                        category.category.titleCategory,
                        totalTask,
                        totalFinishedTask,
                        category.category.color
                    )
                )
            }
            categoryHomeAdapter.submitList(listData.toList())
            if (listData.isEmpty()) {
                binding.apply {
                    tvCategory.visibility = View.GONE
                    rvCategoryHome.visibility = View.GONE
                }
            } else {
                binding.apply {
                    tvCategory.visibility = View.VISIBLE
                    rvCategoryHome.visibility = View.VISIBLE
                }
            }
        }
        userViewModel.userAvatar.observe(viewLifecycleOwner){
            binding.civAvatar.setImageURI(Uri.parse(it))
        }
        userViewModel.userName.observe(viewLifecycleOwner){
            binding.tvName.text = it
        }


    }
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
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
        val date = LocalDate.now()
        taskViewModel.getAllTasksByDate(date.toString()).observe(viewLifecycleOwner){
            binding.tvTasksToday.text = it.size.toString() + " tasks today"
        }
        binding.ivCaigiday.setOnClickListener{
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToEditProfileFragment()
            )
        }


    }
    private fun initComponent(){
        binding.rvCategoryHome.adapter = categoryHomeAdapter
        binding.rvTaskSearchView.adapter = taskSearchViewAdapter
        binding.rvCategorySearchView.adapter = categorySearchViewAdapter
        todoStore = StoreToDo(requireContext())
        lifecycleScope.launch {
            val avatar = todoStore.readString(StoreToDo.KEY_AVATAR, getPictureFromDrawable(R.drawable.meo))
            val userName = todoStore.readString(StoreToDo.KEY_USER_NAME, "Guest")
            binding.civAvatar.setImageURI(Uri.parse(avatar))
            binding.tvName.text = userName
        }
        initViewPager()
    }
    private fun getPictureFromDrawable(id : Int) : String{
        return  ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(
            id
        ) + '/' + resources.getResourceTypeName(id) + '/' + resources.getResourceEntryName(
            id
        )
    }
    private fun initViewPager(){
        val tabType = listOf(TypeStatus.ON_PROGRESS, TypeStatus.FINISHED)
        binding.viewPager.adapter = FragmentHomeViewPager(childFragmentManager, lifecycle, tabType)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabType[position].status
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