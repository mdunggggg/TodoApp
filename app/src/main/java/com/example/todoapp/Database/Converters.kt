package com.example.todoapp.Database

import androidx.room.TypeConverter
import com.example.todoapp.Model.Subtask
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromJson(jsonString: String): List<Subtask> {
        val listType = object : TypeToken<List<Subtask>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }

    @TypeConverter
    fun fromStringList(list: List<Subtask>): String = Gson().toJson(list)
}