package com.example.todoapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
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