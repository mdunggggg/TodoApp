package com.example.todoapp.Dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentQrDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class QrDialog : DialogFragment() {
    private lateinit var binding : FragmentQrDialogBinding
    companion object{
        const val TAG = "QrDialog"
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentQrDialogBinding.inflate(layoutInflater)
        val dialog = context?.let {
            MaterialAlertDialogBuilder(it)
                .setView(binding.root)
        }
        if(dialog != null){
            return dialog.create()
        }
        return super.onCreateDialog(savedInstanceState)
    }

}