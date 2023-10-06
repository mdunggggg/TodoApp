package com.example.todoapp.Utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.lang.Long.max
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


object DateTimeUtils {
    enum class PatternDate(val pattern : String){
        DEFAULT_PATTERN_DATE("yyyy-MM-dd"),
        DEFAULT_PATTERN_DATE_2("dd MMM yyyy"),
        CUSTOM_PATTERN_DATE("MMM d, yyyy"),
        DEFAULT_PATTERN_TIME("HH:mm"),
    }
    fun formatDateToPattern(date: String, inputPattern: String, outputPattern: String): String {
        val inputFormat = SimpleDateFormat(inputPattern, Locale.ENGLISH)
        val outputFormat = SimpleDateFormat(outputPattern, Locale.ENGLISH)
        val dateNew: Date? = inputFormat.parse(date)
        return dateNew?.let { outputFormat.format(it) } ?: ""
    }

    fun formatToDefaultPattern(day : Int, month : Int, year : Int) : String{
        val dayString = if(day < 10){
            "0$day"
         } else {
            day.toString()
        }
        val monthString = if(month < 10){
            "0$month"
        } else {
            month.toString()
        }
        return "$year-$monthString-$dayString"
    }
    fun getAmPm(time : String) : String{
        val timeSplit = time.split(":")
        val hour = timeSplit[0].toInt()
        val minute = timeSplit[1].toInt()
        return if(hour < 12){
            "AM"
        } else {
            "PM"
        }
    }
        @RequiresApi(Build.VERSION_CODES.O)
    fun formatTime(hour : Int, minute : Int) : String{
        val time = LocalTime.of(hour, minute)
        return time.toStringTime()!!
    }
    private fun LocalTime.toStringTime(): String? {
        val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter.ofPattern(DateTimeUtils.PatternDate.DEFAULT_PATTERN_TIME.pattern)
        } else {
            return null
        }
        return this.format(formatter)
    }
    fun getDelayTime(date: String, time: String): Long {
        val dayOfMonth = date.split("-")[2].toInt()
        val month = date.split("-")[1].toInt()
        val year = date.split("-")[0].toInt()
        val hour = time.split(":")[0].toInt()
        val minute = time.split(":")[1].toInt()
        val notificationTime: Calendar = Calendar.getInstance()
        notificationTime.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }
        val currentTimeMillis = System.currentTimeMillis()
        val notificationTimeMillis: Long = notificationTime.timeInMillis
        val limit = 5 * 60 * 1000
        return max(0, notificationTimeMillis - currentTimeMillis - limit)
    }


}