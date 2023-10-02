package com.example.todoapp.Fragment

import android.content.ContentResolver
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todoapp.DataStore.StoreToDo
import com.example.todoapp.R
import com.example.todoapp.ViewModel.UserViewModel
import com.example.todoapp.databinding.FragmentSettingBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SettingFragment : Fragment() {
    private lateinit var binding : FragmentSettingBinding
    private val scope = CoroutineScope(Dispatchers.IO)
    private lateinit var todoStore : StoreToDo
    private val userViewModel : UserViewModel by activityViewModels{
        UserViewModel.UserViewModelFactory(requireActivity().application)
    }

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
//        lifecycleScope.launch {
//            val avatar = todoStore.readString(StoreToDo.KEY_AVATAR, getPictureFromDrawable(R.drawable.meo))
//            val coverImage = todoStore.readString(StoreToDo.KEY_COVER_IMAGE, getPictureFromDrawable(R.drawable.proptit))
//            val userName = todoStore.readString(StoreToDo.KEY_USER_NAME, "Guest")
//            val userEmail = todoStore.readString(StoreToDo.KEY_USER_EMAIL, "")
//            binding.profileImage.setImageURI(Uri.parse(avatar))
//            binding.headerProfile.setImageURI(Uri.parse(coverImage))
//            binding.profileName.text = userName
//            if(userEmail.isNotEmpty()){
//                binding.profileEmail.text = userEmail
//                binding.profileEmail.visibility = View.VISIBLE
//            }
//            else
//                binding.profileEmail.visibility = View.GONE
//        }
//        Log.d(TAG, "initComponent: " + getPictureFromDrawable(R.drawable.meo))
        userViewModel.userName.observe(viewLifecycleOwner){
            binding.profileName.text = it
        }
        userViewModel.userEmail.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                binding.profileEmail.text = it
                binding.profileEmail.visibility = View.VISIBLE
            }
            else
                binding.profileEmail.visibility = View.GONE
        }
        userViewModel.userAvatar.observe(viewLifecycleOwner){
            binding.profileImage.setImageURI(Uri.parse(it))
        }
        userViewModel.userBackground.observe(viewLifecycleOwner){
            binding.headerProfile.setImageURI(Uri.parse(it))
        }
    }
    private fun initBehavior(){
        binding.themeApp.setOnClickListener {
            showThemeDialog()
        }
        binding.edProfile.setOnClickListener {
            goToEditProfile()
        }
    }

    private fun goToEditProfile() {
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToEditProfileFragment()
        )
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