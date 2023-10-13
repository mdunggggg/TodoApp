package com.example.todoapp.Dialog

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.todoapp.Interfaces.IAddCategoryListener
import com.example.todoapp.Model.Category
import com.example.todoapp.databinding.FragmentAddCategoryDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddCategoryDialog(private val onAddCategory : IAddCategoryListener) : BottomSheetDialogFragment() {
    private lateinit var binding : FragmentAddCategoryDialogBinding
    private var color = Color.BLACK
    private var title = ""
    companion object{
        const val TAG = "AddCategoryDialog"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCategoryDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btSetColorCategory.setOnClickListener {
            setColorCategory()
        }
        binding.btAddCategory.setOnClickListener {
            addCategory()
        }
    }

    private fun setColorCategory(){
        val colorPickerDialog = ColorPickerDialog{
            color -> binding.apply {
                btSetColorCategory.backgroundTintList = ColorStateList.valueOf(color)
             }
            this.color = color
        }
        colorPickerDialog.show(childFragmentManager, ColorPickerDialog.TAG)
    }
    private fun addCategory(){
        if(binding.etCategoryName.text.toString() == ""){
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        onAddCategory.onAddCategory(
            Category(titleCategory = binding.etCategoryName.text.toString(), color = color)
        )
        dismiss()
    }


}