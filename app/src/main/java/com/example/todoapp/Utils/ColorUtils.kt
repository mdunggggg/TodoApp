package com.example.todoapp.Utils

import android.content.Context
import android.content.res.TypedArray
import androidx.core.content.ContextCompat

object ColorUtils {
    fun getListColor(context : Context, colors : List<Int>) : List<Int>{
        val listColor = mutableListOf<Int>()
        for (color in colors){
            listColor.add(ContextCompat.getColor(context, color))
        }
        return listColor
    }
    fun getColor(context: Context, color : Int) : Int {
        return ContextCompat.getColor(context, color)
    }
    private fun ContextCompat.getColor(context: Context, color: Int): Int {
        return ContextCompat.getColor(context, color)
    }
    fun Context.getColorArray(id: Int): List<Int> {
        val colorList = mutableListOf<Int>()
        val colors: TypedArray = resources.obtainTypedArray(id)

        for (i in 0 until colors.length()) {
            val color = colors.getColor(i, 0)
            colorList.add(color)
        }

        colors.recycle()
        return colorList
    }
}


