package com.example.todoapp.Dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.todoapp.Adapter.RecyclerViewAdapter.ColorAdapter

import com.example.todoapp.R
import com.example.todoapp.Utils.ColorUtils
import com.example.todoapp.Utils.ColorUtils.getColorArray
import com.example.todoapp.databinding.FragmentColorPickerDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ColorPickerDialog(
    private val onColorClicked : (color : Int) -> Unit
) : DialogFragment() {
    private lateinit var binding: FragmentColorPickerDialogBinding

    companion object {
        const val TAG = "ColorPickerDialog"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentColorPickerDialogBinding.inflate(layoutInflater)
        initBehavior()
        val dialog = MaterialAlertDialogBuilder(requireContext()).setView(binding.root)
        return dialog.create()
    }

    private fun initBehavior() {

        binding.rvColorPicker.apply {
            adapter = ColorAdapter(requireContext().getColorArray(R.array.colors),
                {color -> onColorClicked(color)},
                this@ColorPickerDialog::dismiss
            )

        }
    }
}
