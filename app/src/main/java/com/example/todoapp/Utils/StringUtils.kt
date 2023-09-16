package com.example.todoapp.Utils

import com.example.todoapp.Model.Task
import com.google.gson.Gson


object StringUtils {
    fun serializeToJson(myClass: Task): String? {
        val gson = Gson()
        return gson.toJson(myClass)
    }
    fun deserializeFromJson(jsonString: String?, java: Class<Task>): Task? {
        val gson = Gson()
        return gson.fromJson(jsonString, Task::class.java)
    }
}