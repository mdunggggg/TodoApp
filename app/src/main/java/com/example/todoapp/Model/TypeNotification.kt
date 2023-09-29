package com.example.todoapp.Model

enum class TypeNotification(private val type : String){
    TaskNotification("task_notification"),
    PomodoroNotification("pomodoro_notification")
}