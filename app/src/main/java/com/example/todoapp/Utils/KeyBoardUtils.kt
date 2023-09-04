package com.example.todoapp.Utils

import android.app.Activity
import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object KeyBoardUtils{
    fun hideSoftKeyboard(view: View? = null, activity: Activity): Boolean {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            return if (view == null) {
                inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
            } else {
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
        return false
    }
    fun EditText.onDone(callback : () -> Unit){
        setOnEditorActionListener { _, type, _ ->
            return@setOnEditorActionListener if (type == EditorInfo.IME_ACTION_DONE) {
                callback()
                true
            }
            else {
                false
            }
        }
        setOnKeyListener(
            View.OnKeyListener { _, keyCode, event ->
                return@OnKeyListener if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    callback()
                    true
                }
                else {
                    false
                }
            })
    }

}
