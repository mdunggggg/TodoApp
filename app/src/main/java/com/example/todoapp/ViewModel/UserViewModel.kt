package com.example.todoapp.ViewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoapp.DataStore.StoreToDo
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : ViewModel() {
    var userName : MutableLiveData<String> = MutableLiveData()
    var userEmail : MutableLiveData<String> = MutableLiveData()
    var userAvatar : MutableLiveData<String> = MutableLiveData()
    var userBackground : MutableLiveData<String> = MutableLiveData()
    var userPhoneNumber : MutableLiveData<String> = MutableLiveData()
    var userBirthday : MutableLiveData<String> = MutableLiveData()
    private val storeToDo : StoreToDo = StoreToDo(application)
    companion object{
        const val DEFAULT_USER_NAME = "Guest"
        const val DEFAULT_USER_EMAIL = ""
        const val DEFAULT_AVATAR = "android.resource://com.example.todoapp/drawable/meo"
        const val DEFAULT_COVER_IMAGE = "android.resource://com.example.todoapp/drawable/proptit"
        const val DEFAULT_PHONE_NUMBER = ""
        const val DEFAULT_BIRTHDAY = ""
    }
    init {
        viewModelScope.launch {
            userName.value = storeToDo.readString(StoreToDo.KEY_USER_NAME, DEFAULT_USER_NAME)
            userEmail.value = storeToDo.readString(StoreToDo.KEY_USER_EMAIL, DEFAULT_USER_EMAIL)
            userAvatar.value = storeToDo.readString(StoreToDo.KEY_AVATAR, DEFAULT_AVATAR)
            userBackground.value = storeToDo.readString(StoreToDo.KEY_COVER_IMAGE, DEFAULT_COVER_IMAGE)
        }
    }
    fun updateUser(userName : String, userEmail : String, birthday : String , phoneNumber : String){
        this.userName = MutableLiveData(userName)
        this.userEmail = MutableLiveData(userEmail)
        this.userBirthday = MutableLiveData(birthday)
        this.userPhoneNumber = MutableLiveData(phoneNumber)
        viewModelScope.launch {
            storeToDo.write(StoreToDo.KEY_USER_NAME, userName)
            storeToDo.write(StoreToDo.KEY_USER_EMAIL, userEmail)
            storeToDo.write(StoreToDo.KEY_PHONE_NUMBER, phoneNumber)
            storeToDo.write(StoreToDo.KEY_BIRTHDAY, birthday)
        }
    }
    fun updateAvatar(avatar : String){
        this.userAvatar = MutableLiveData(avatar)
        viewModelScope.launch {
            storeToDo.write(StoreToDo.KEY_AVATAR, avatar)
        }
    }
    class UserViewModelFactory(private val application: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(UserViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}