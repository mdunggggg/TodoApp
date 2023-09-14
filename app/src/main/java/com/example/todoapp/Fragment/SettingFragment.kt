package com.example.todoapp.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.todoapp.databinding.FragmentSettingBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingFragment : Fragment() {
    private lateinit var binding : FragmentSettingBinding
    companion object{
        const val TAG = "SettingFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        initBehavior()
        return binding.root
    }

    private fun initBehavior(){
        binding.themeApp.setOnClickListener {
            showThemeDialog()
        }
    }
    private fun showThemeDialog(){
        val singleItems = arrayOf("Light Mode", "Dark Mode", "System Default")
        var checkedItem = 0
        MaterialAlertDialogBuilder(requireContext())
            . setTitle("Choose theme")
            .setPositiveButton("OK"){ _, _ ->
                when(checkedItem){
                    0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
            .setNegativeButton("CANCEL"){_, _ ->
                Log.d(TAG, "showThemeDialog: Cancel")
            }
            .setSingleChoiceItems(singleItems, checkedItem){ _, which ->
                checkedItem = which
            }
            .show()
    }
}