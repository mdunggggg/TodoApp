package com.example.todoapp.Dialog

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.Adapter.RecyclerViewAdapter.CategoryAdapter
import com.example.todoapp.Interfaces.IAddCategoryListener
import com.example.todoapp.Interfaces.ICategoryListener
import com.example.todoapp.Model.Category
import com.example.todoapp.R
import com.example.todoapp.ViewModel.CategoryViewModel
import com.example.todoapp.ViewModel.TaskViewModel
import com.example.todoapp.databinding.FragmentAddTaskDialogBinding
import com.example.todoapp.databinding.FragmentCategoryPickerDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CategoryPickerDialog(
    private val categoryListener : ICategoryListener

) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCategoryPickerDialogBinding
    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter( categoryListener, this@CategoryPickerDialog::dismiss)
    }
    private val categoryViewModel : CategoryViewModel by activityViewModels {
        CategoryViewModel.CategoryViewModelFactory(requireActivity().application)
    }
    companion object{
        const val TAG = "CategoryPickerDialog"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryPickerDialogBinding.inflate(inflater, container, false)
        initComponent()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initComponent()
        initBehavior()
    }
    private fun initComponent(){
        binding.rvCategories.apply {
            adapter = categoryAdapter
        }

        categoryViewModel.getAllCategory().observe(viewLifecycleOwner){
            categoryAdapter.submitList(it)
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



}