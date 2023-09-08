package com.example.todoapp.Utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


object DateTimeUtils {
    private const val defaultPatternDate : String = "dd/MM/yyyy"
    private const val customPatternDate : String = "MMM d, yyyy"
     private const val patternTime : String = "HH:mm"


    private fun formatDateToPattern(date: String, inputPattern: String, outputPattern: String): String {
        val inputFormat = SimpleDateFormat(inputPattern, Locale.ENGLISH)
        val outputFormat = SimpleDateFormat(outputPattern, Locale.ENGLISH)
        val dateNew: Date? = inputFormat.parse(date)
        return dateNew?.let { outputFormat.format(it) } ?: ""
    }

    fun formatToDefaultPattern(date: String): String {
        return formatDateToPattern(date, customPatternDate, defaultPatternDate) ?: ""
    }

    fun formatToCustomPattern(date: String): String {
        return formatDateToPattern(date, defaultPatternDate, customPatternDate) ?: ""
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
        return "$dayString/$monthString/$year"
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
            DateTimeFormatter.ofPattern(DateTimeUtils.patternTime)
        } else {
            return null
        }
        return this.format(formatter)
    }


}