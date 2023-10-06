package com.example.todoapp.Dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Adapter.RecyclerViewAdapter.CategoryAdapter
import com.example.todoapp.Interfaces.IAddCategoryListener
import com.example.todoapp.Interfaces.ICategoryListener
import com.example.todoapp.Model.Category
import com.example.todoapp.Utils.SwipeHelper
import com.example.todoapp.ViewModel.CategoryViewModel
import com.example.todoapp.ViewModel.TaskViewModel
import com.example.todoapp.databinding.FragmentCategoryPickerDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CategoryPickerDialog(
    private val categoryListener : ICategoryListener

) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCategoryPickerDialogBinding
    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter( categoryListener, onCountTask, this@CategoryPickerDialog::dismiss)
    }
    private val categoryViewModel : CategoryViewModel by activityViewModels {
        CategoryViewModel.CategoryViewModelFactory(requireActivity().application)
    }
    private val taskViewModel : TaskViewModel by activityViewModels {
        TaskViewModel.TaskViewModelFactory(requireActivity().application)
    }
    companion object{
        const val TAG = "CategoryPickerDialog"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryPickerDialogBinding.inflate(inflater, container, false)
        initComponent()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initComponent()
        initBehavior()
        setUpSwipeAction();
    }
    private fun initComponent(){
        binding.rvCategories.apply {
            adapter = categoryAdapter
        }
        categoryViewModel.getAllCategory().observe(viewLifecycleOwner){
            categoryAdapter.submitList(it)
            Log.d(TAG, "initComponent: ${it.size}")
        }
    }
    private fun initBehavior(){
        binding.btAddCategory.setOnClickListener {
            addCategory()
        }
    }
    private fun addCategory(){
        AddCategoryDialog(object : IAddCategoryListener{
            override fun onAddCategory(category: Category) {
                categoryViewModel.insertCategory(category)
            }
        }).show(childFragmentManager, AddCategoryDialog.TAG)
    }
    private val onCountTask = { titleCategory : String ->
        categoryViewModel.getCategoryWithTasksByTitle(titleCategory).tasks.filter {
            !it.isStored
        }.size.toString()
    }
    private fun setUpSwipeAction(){
        ItemTouchHelper(object : SwipeHelper(requireContext()){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val category = categoryAdapter.currentList[position]
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Delete the category and all tasks")
                    .setMessage("Are you sure want to delete this category and all tasks ")
                    .setPositiveButton("Yes"){ _, _ ->
                        categoryViewModel.deleteCategory(category)
                        taskViewModel.clearTasksByCategory(category.titleCategory)
                    }
                    .setNegativeButton("No"){ _, _ ->
                        categoryAdapter.notifyItemChanged(position)
                    }
                    .show()
            }
        }).attachToRecyclerView(binding.rvCategories)
    }

}