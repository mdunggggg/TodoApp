package com.example.todoapp.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Adapter.RecyclerViewAdapter.HomeTaskAdapter
import com.example.todoapp.Model.Task
import com.example.todoapp.Model.TypeStatus
import com.example.todoapp.Utils.SwipeHelper
import com.example.todoapp.ViewModel.TaskViewModel
import com.example.todoapp.databinding.FragmentHomeTabBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class HomeTabFragment : Fragment() {
   private lateinit var binding : FragmentHomeTabBinding
   private lateinit var typeView : TypeStatus
    private val taskViewModel : TaskViewModel by activityViewModels {
        TaskViewModel.TaskViewModelFactory(requireActivity().application)
    }
    private val homeTaskAdapter: HomeTaskAdapter by lazy {
        HomeTaskAdapter { task: Task ->
            goToDetailFragment(task)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        receiveData()
        initComponent()
        setUpSwipeAction()
    }

    @SuppressLint("SetTextI18n")
    private fun initComponent() {
        when (typeView) {
            TypeStatus.ON_PROGRESS -> {
                binding.rvTaskHome.adapter = homeTaskAdapter
                taskViewModel.getAllUnFinishTasks().observe(viewLifecycleOwner){ tasks ->
                    homeTaskAdapter.submitList(tasks)
                    if(tasks.isEmpty()){
                        binding.apply {
                            emptyListBg.visibility = View.VISIBLE
                            tvEmptyTask.visibility = View.VISIBLE
                            tvEmptyTask.text = "Nothing to do :>>>>"
                            Log.d("HomeTabFragment", "onViewCreated: Nothing to do")
                        }
                    }
                    else{
                        binding.apply {
                            emptyListBg.visibility = View.GONE
                            tvEmptyTask.visibility = View.GONE
                        }
                    }
                }
            }
            TypeStatus.FINISHED -> {
                binding.rvTaskHome.adapter = homeTaskAdapter
                taskViewModel.getAllFinishTasks().observe(viewLifecycleOwner){ tasks ->
                    homeTaskAdapter.submitList(tasks)
                    if(tasks.isEmpty()){
                        binding.apply {
                            emptyListBg.visibility = View.VISIBLE
                            tvEmptyTask.visibility = View.VISIBLE
                            tvEmptyTask.text = "You haven't finished any task yet :>>>>"
                        }
                    }
                    else{
                        binding.apply {
                            emptyListBg.visibility = View.GONE
                            tvEmptyTask.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun receiveData() {
        typeView = requireArguments().getSerializable("type") as TypeStatus
    }

    private fun goToDetailFragment(task: Task){
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToDetailTaskFragment(task)
        )
    }
    private fun setUpSwipeAction(){
        context?.let {
            ItemTouchHelper(object : SwipeHelper(it){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.absoluteAdapterPosition
                    val task = homeTaskAdapter.currentList[position]
                    MaterialAlertDialogBuilder(context!!)
                        .setTitle("Move to trash")
                        .setMessage("Are you sure want to " +
                                "move this task to trash?")
                        .setPositiveButton("Yes"){ _, _ ->
                            taskViewModel.updateTask(task.copy(isStored = true))
                            Snackbar.make(binding.root, "Task is moved to trash", Snackbar.LENGTH_LONG).show()
                        }
                        .setNegativeButton("No"){ dialog, _ ->
                            dialog.dismiss()
                            homeTaskAdapter.notifyItemChanged(position)
                        }
                        .show()
                }
            }).attachToRecyclerView(binding.rvTaskHome)
        }?:{
            Toast.makeText(context, "Có lỗi xảy ra!! Vui lòng khởi động lại app", Toast.LENGTH_SHORT).show()
        }
    }


}