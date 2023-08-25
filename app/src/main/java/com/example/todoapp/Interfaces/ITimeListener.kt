package com.example.todoapp.Interfaces

import java.time.LocalDate
import java.time.LocalTime

interface ITimeListener {
    fun onDateTimeSelected(date : LocalDate?, time : LocalTime?)
}