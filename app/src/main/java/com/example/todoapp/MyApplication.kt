package com.example.todoapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.example.todoapp.DataStore.StoreToDo
import com.example.todoapp.DataStore.StoreToDo.Companion.KEY_DARK_MODE
import kotlinx.coroutines.runBlocking
import java.util.Date

class MyApplication : Application() {
    companion object{
        const val CHANNEL_ID = "Channel_1"
         fun getNotificationId() : Int{
            return Date().time.toInt()
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        getDarkMode()

    }
    private fun getDarkMode() {
        val darkMode = runBlocking {
            StoreToDo(applicationContext).read(KEY_DARK_MODE, false)
        }
        onSetDarkMode(darkMode)
    }

    private fun onSetDarkMode(isDark : Boolean){
        AppCompatDelegate.setDefaultNightMode(
            if(isDark)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel.
            val name = getString(R.string.chanel_name)
            val descriptionText = getString(R.string.chanel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH

            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

}