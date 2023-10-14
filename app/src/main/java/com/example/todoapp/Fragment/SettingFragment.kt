package com.example.todoapp.Fragment


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.DataStore.StoreToDo
import com.example.todoapp.Dialog.QrDialog
import com.example.todoapp.ViewModel.UserViewModel
import com.example.todoapp.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {
    private lateinit var binding : FragmentSettingBinding
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initComponent()
        observeData()
        initBehavior()
    }

    private fun observeData() {
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

    private fun initComponent() {
        todoStore = StoreToDo(requireContext())

    }
    private fun initBehavior(){
        binding.apply {
            changePassword.setOnClickListener {
                Toast.makeText(context, "The fu", Toast.LENGTH_SHORT).show()
            }
            themeApp.setOnClickListener {
                Toast.makeText(context, "The function is currently under development.", Toast.LENGTH_SHORT).show()
            }
            colorScheme.setOnClickListener {
                Toast.makeText(context, "The function is currently under development.", Toast.LENGTH_SHORT).show()
            }
            feedback.setOnClickListener {
                Toast.makeText(context, "The function is currently under development.", Toast.LENGTH_SHORT).show()
            }
            rate.setOnClickListener {
                Toast.makeText(context, "The function is currently under development.", Toast.LENGTH_SHORT).show()
            }
            support.setOnClickListener {
                Toast.makeText(context, "The function is currently under development.", Toast.LENGTH_SHORT).show()
            }
            faq.setOnClickListener {
                Toast.makeText(context, "The function is currently under development.", Toast.LENGTH_SHORT).show()
            }
            privacyPolicy.setOnClickListener {
                Toast.makeText(context, "The function is currently under development.", Toast.LENGTH_SHORT).show()
            }
            termsOfUse.setOnClickListener {
                Toast.makeText(context, "The function is currently under development.", Toast.LENGTH_SHORT).show()
            }
            aboutUs.setOnClickListener {
                Toast.makeText(context, "The function is currently under development.", Toast.LENGTH_SHORT).show()
            }
            goPremium.setOnClickListener {
                showQrDialog();
            }
            edProfile.setOnClickListener {
                goToEditProfile()
            }

        }
    }

    private fun showQrDialog() {
        val dialog = QrDialog()
        dialog.show(parentFragmentManager, QrDialog.TAG)

    }

    private fun goToEditProfile() {
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToEditProfileFragment()
        )
    }
}