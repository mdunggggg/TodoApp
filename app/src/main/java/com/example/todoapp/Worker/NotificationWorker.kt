package com.example.todoapp.Worker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.todoapp.MainActivity
import com.example.todoapp.Model.Task
import com.example.todoapp.Model.TypeNotification
import com.example.todoapp.MyApplication
import com.example.todoapp.R
import com.example.todoapp.Utils.StringUtils

class NotificationWorker(
    private val context : Context, workerParams: WorkerParameters
) : Worker(context, workerParams){
    override fun doWork(): Result {
        if(inputData.getString("key").equals(TypeNotification.TaskNotification.name)){
            val title = inputData.getString("title")
            val content = inputData.getString("content")
            val task = StringUtils.deserializeFromJson(inputData.getString("task")!!, Task::class.java)
            showNotification(title!!, content!!, task!!)
        }
        else{
            val title = inputData.getString("title")
            val content = inputData.getString("content")
            showNotification(title!!, content!!)
        }

        return Result.success()
    }
    private fun showNotification(title : String, content : String, task : Task){
        val bundle = Bundle()
        bundle.putSerializable("TaskArgs", task)
        val pendingIntent = NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.detailTaskFragment)
            .setArguments(bundle)
            .createPendingIntent()
        val builder = NotificationCompat.Builder(context, MyApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_logo_no_color)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setColor(ContextCompat.getColor(context, R.color.green_active))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(context)){
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(MyApplication.getNotificationId(), builder.build())
        }
    }
    private fun showNotification(title : String, content : String){
        val pendingIntent = NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.detailTaskFragment)
            .createPendingIntent()
        val builder = NotificationCompat.Builder(context, MyApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_logo_no_color)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setColor(ContextCompat.getColor(context, R.color.green_active))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(context)){
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(MyApplication.getNotificationId(), builder.build())
        }
    }
}