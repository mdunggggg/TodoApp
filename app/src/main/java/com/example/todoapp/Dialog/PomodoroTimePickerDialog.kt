package com.example.todoapp.Dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
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
        val dialog = context?.let {
            MaterialAlertDialogBuilder(it).setView(binding.root)
        }
        initBehavior()
        if (dialog != null) {
            return dialog.create()
        }
        return super.onCreateDialog(savedInstanceState)
    }

    private fun initBehavior() {
        binding.pomodoroTimePickerOkBtn.setOnClickListener{
            onGotPomodoroTime(
                binding.pomodoroTime.text.toString(),
                binding.shortBreakTime.text.toString(),
                binding.longBreakTime.text.toString()
            )
            dismiss()
        }

    }
    private fun onGotPomodoroTime(pomodoroTime : String, shortBreakTime : String, longBreakTime : String){
        if(pomodoroTime.isEmpty() || shortBreakTime.isEmpty() || longBreakTime.isEmpty()){
            Toast.makeText(context, "Please fill all the field", Toast.LENGTH_SHORT).show()
            return
        }
       try {
           goToPomodoroFragment(pomodoroTime.toInt(), shortBreakTime.toInt(), longBreakTime.toInt())
       }
         catch (e : Exception){
             Toast.makeText(context, "Time must be a number", Toast.LENGTH_SHORT).show()
         }
    }


}