package com.example.todoapp.Dialog

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.Adapter.RecyclerViewAdapter.CategoryAdapter
import com.example.todoapp.Interfaces.ICategoryListener
import com.example.todoapp.Model.Category
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentAddTaskDialogBinding
import com.example.todoapp.databinding.FragmentCategoryPickerDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CategoryPickerDialog(
    private val categoryListener : ICategoryListener

) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCategoryPickerDialogBinding
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
    private fun initComponent(){
        val list : List<Category> = listOf(
            Category(1, "Personal", ContextCompat.getColor(requireContext(), R.color.black)),
            Category(2, "Work", ContextCompat.getColor(requireContext(), R.color.red_active)),
            Category(3, "Study", ContextCompat.getColor(requireContext(), R.color.green_active)),
            Category(4, "Shopping", ContextCompat.getColor(requireContext(), R.color.purple_active)),
        )

        binding.rvCategories.apply {
            adapter = CategoryAdapter(list, categoryListener, this@CategoryPickerDialog::dismiss)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


}