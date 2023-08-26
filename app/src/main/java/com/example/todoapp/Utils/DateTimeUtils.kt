package com.example.todoapp.Utils

import android.os.Build
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter





object DateTimeUtils {
    const val patternDate : String = "dd/MM/yyyy"
     private const val patternDateWithStringMonth : String = "dd MMM yyyy"
     private const val patternTime : String = "HH:mm"
    fun formatDateTime(date: LocalDate?, time: LocalTime?): String? {
        if (date == null)
            return null
        return if (time == null)
            date.toDateWithStringMonth()
        else
            date.toDateWithStringMonth() + " " + time.toStringTime()
    }
    fun convertToStringTime(time: LocalTime?): String? {
        return time?.toStringTime()
    }
    private fun LocalTime.toStringTime(): String? {
        val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter.ofPattern(DateTimeUtils.patternTime)
        } else {
            return null
        }
        return this.format(formatter)
    }
    fun convertToDateWithStringMonth(date: LocalDate?): String? {
        return date?.toDateWithStringMonth()
    }
    private fun LocalDate.toDateWithStringMonth(): String? {
        val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter.ofPattern(DateTimeUtils.patternDateWithStringMonth)
        } else {
            return null
        }
        return this.format(formatter)
    }
}