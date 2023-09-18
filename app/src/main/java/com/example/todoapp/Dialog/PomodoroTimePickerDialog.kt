package com.example.todoapp.Dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.todoapp.databinding.FragmentPomodoroTimePickerDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PomodoroTimePickerDialog(
    private val goToPomodoroFragment : (pomodoroTime : Int, shortBreakTime : Int, longBreakTime : Int) -> Unit
) : DialogFragment() {
    private lateinit var binding: FragmentPomodoroTimePickerDialogBinding
    companion object {
        const val TAG = "PomodoroTimePickerDialog"
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentPomodoroTimePickerDialogBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(requireContext()).setView(binding.root)
        initBehavior()
        return dialog.create()
    }

    private fun initBehavior() {
        binding.pomodoroTimePickerOkBtn.setOnClickListener{
            goToPomodoroFragment(
                binding.pomodoroTimeCount.text.toString().toInt(),
                binding.pomodoroShortTimeCount.text.toString().toInt(),
                binding.pomodoroLongBreakCount.text.toString().toInt()
            )
            dismiss()
        }

    }


}