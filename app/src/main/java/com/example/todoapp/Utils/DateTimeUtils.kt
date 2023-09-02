package com.example.todoapp.Utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


object DateTimeUtils {
    private const val patternDate : String = "dd/MM/yyyy"
    private const val patternMMMDYYYY : String = "MMM d, yyyy"
     private const val patternDateWithStringMonth : String = "dd MMM yyyy"
     private const val patternTime : String = "HH:mm"

    fun formatDate(date: String): String {
        val inputFormat = SimpleDateFormat(patternMMMDYYYY, Locale.ENGLISH)
        val outputFormat = SimpleDateFormat(patternDate, Locale.ENGLISH)
        val dateNew: Date? = inputFormat.parse(date)
        return outputFormat.format(dateNew!!)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatTime(hour : Int, minute : Int) : String{
        val time = LocalTime.of(hour, minute)
        return time.toStringTime()!!
    }

    fun formatDateTime(date : String , time : String?) : String?{
        val inputFormat = SimpleDateFormat(patternDate, Locale.ENGLISH)
        val outputFormat = SimpleDateFormat(patternMMMDYYYY, Locale.ENGLISH)
        val dateNew: Date? = inputFormat.parse(date)
        val formattedDate = outputFormat.format(dateNew)
        return "$formattedDate $time"
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