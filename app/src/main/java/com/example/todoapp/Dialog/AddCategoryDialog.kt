package com.example.todoapp.Dialog

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Color.BLACK
import android.graphics.Color.BLUE
import android.graphics.Color.CYAN
import android.graphics.Color.DKGRAY
import android.graphics.Color.GRAY
import android.graphics.Color.GREEN
import android.graphics.Color.LTGRAY
import android.graphics.Color.MAGENTA
import android.graphics.Color.RED
import android.graphics.Color.WHITE
import android.graphics.Color.YELLOW
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.colorChooser
import com.example.todoapp.Interfaces.IAddCategoryListener
import com.example.todoapp.Model.Category
import com.example.todoapp.R
import com.example.todoapp.Utils.ColorUtils.getColorArray
import com.example.todoapp.databinding.FragmentAddCategoryDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialDialogs


class AddCategoryDialog(private val onAddCategory : IAddCategoryListener) : BottomSheetDialogFragment() {
    private lateinit var binding : FragmentAddCategoryDialogBinding
    private var color = Color.BLACK
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
        onAddCategory.onAddCategory(
            Category(title = binding.etCategoryName.text.toString(), color = color)
        )
        dismiss()
    }


}