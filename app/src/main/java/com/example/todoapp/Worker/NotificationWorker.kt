package com.example.todoapp.Worker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.todoapp.MyApplication
import com.example.todoapp.R

class NotificationWorker(
    private val context : Context, workerParams: WorkerParameters
) : Worker(context, workerParams){
    override fun doWork(): Result {
        val title = inputData.getString("title")
        val content = inputData.getString("content")
        testNotification(title!!, content!!)
        return Result.success()
    }
    private fun testNotification(title : String, content : String){
        val builder = NotificationCompat.Builder(context, MyApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        with(NotificationManagerCompat.from(context)){
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(MyApplication.getNotificationId(), builder.build())
            Log.d("NotificationWorker", "doTestNotification: ")
        }
    }
}