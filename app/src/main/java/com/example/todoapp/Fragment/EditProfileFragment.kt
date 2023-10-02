
package com.example.todoapp.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.ViewModel.UserViewModel
import com.example.todoapp.databinding.FragmentEditProfileBinding
import com.github.dhaval2404.imagepicker.ImagePicker


class EditProfileFragment : Fragment() {
    private lateinit var binding : FragmentEditProfileBinding
    private val userViewModel : UserViewModel by activityViewModels{
        UserViewModel.UserViewModelFactory(requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpOnBackPress()
        initComponent()
        initBehavior()
    }

    private fun initBehavior() {
        binding.profileImage.setOnClickListener{
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }
        binding.updateProfileFunction.setOnClickListener {
            val userName = binding.edFullName.text.toString()
            val userEmail = binding.edEmail.text.toString()
            val userBirthday = binding.edDateOfBirth.text.toString()
            val userPhone = binding.edPhoneNumber.text.toString()
            userViewModel.updateUser(userName, userEmail, userBirthday, userPhone)
            findNavController().navigateUp()
        }
    }
    private fun setUpOnBackPress(){
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            callback
        )
    }

    private fun initComponent() {
        binding.profileImage.setImageURI(Uri.parse(userViewModel.userAvatar.value))
        binding.headerProfile.setImageURI(Uri.parse(userViewModel.userBackground.value))
        binding.edFullName.setText(userViewModel.userName.value)
        binding.edEmail.setText(userViewModel.userEmail.value)
        binding.edDateOfBirth.setText(userViewModel.userBirthday.value)
        binding.edPhoneNumber.setText(userViewModel.userPhoneNumber.value)
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val uri : Uri? = data?.data
        binding.profileImage.setImageURI(uri)
        userViewModel.updateAvatar(uri.toString())
    }



}