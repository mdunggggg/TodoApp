package com.example.todoapp.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.todoapp.DataStore.StoreToDo
import com.example.todoapp.databinding.FragmentSettingBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingFragment : Fragment() {
    private lateinit var binding : FragmentSettingBinding
    private val scope = CoroutineScope(Dispatchers.IO)
    private lateinit var todoStore : StoreToDo


    companion object{
        const val TAG = "SettingFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        initComponent()
        initBehavior()
        return binding.root
    }

    private fun initComponent() {
        todoStore = StoreToDo(requireContext())
    }

    private fun initBehavior(){

        binding.themeApp.setOnClickListener {
            showThemeDialog()
        }
    }
    private fun showThemeDialog(){
        val singleItems = arrayOf("Light Mode", "Dark Mode", "System Default")
        var checkedItem = if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO) 1 else 0
        MaterialAlertDialogBuilder(requireContext())
            . setTitle("Choose theme")
            .setPositiveButton("OK"){ _, _ ->
                when(checkedItem){
                    0 -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        scope.launch {
                            todoStore.write(StoreToDo.KEY_DARK_MODE, false)
                        }
                    }
                    1 -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        scope.launch {
                            todoStore.write(StoreToDo.KEY_DARK_MODE, true)
                        }
                    }
                    2 -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        scope.launch {
                            todoStore.write(StoreToDo.KEY_DARK_MODE, false)
                        }
                    }
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