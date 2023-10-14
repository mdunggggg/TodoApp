package com.example.todoapp.Fragment

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.navArgs
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.todoapp.Model.TypeNotification
import com.example.todoapp.R
import com.example.todoapp.Worker.NotificationWorker
import com.example.todoapp.databinding.FragmentPomodoroBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.concurrent.TimeUnit

class PomodoroFragment : Fragment() {
    private lateinit var binding: FragmentPomodoroBinding
    private val args : PomodoroFragmentArgs by navArgs()
    private lateinit var pomodoroTime : String
    private lateinit var shortBreakTime  : String
    private lateinit var longBreakTime : String
    private lateinit var timeLeft : String
    private var countDownTimerPomodoro : CountDownTimer? = null
    private var timeRunning : Boolean = false
    private var countState : Int = 1
    companion object{
        const val TAG = "PomodoroFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
         savedInstanceState: Bundle?
    ): View {
        binding = FragmentPomodoroBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        receiveData()
        initPomodoro()
        initBehavior()
    }

    private fun initBehavior() {
        binding.pomodoroNextButton.setOnClickListener {
            countState++
            if(countState == 9){
                countState = 1
            }
            countDownTimerPomodoro?.cancel()
            initPomodoro()
        }
        binding.pomodoroResetButton.setOnClickListener {
            countDownTimerPomodoro?.cancel()
            initPomodoro()
            pauseTimer()
        }
    }

    private fun receiveData() {
        pomodoroTime  = (args.pomodoroTimeArgs * 60 * 1000L) . toString()
        shortBreakTime = (args.shortBreakArgs  * 60 * 1000L).toString()
        longBreakTime  =  (args.longBreakArgs * 60 * 1000L).toString()
    }

    private fun initPomodoro() {
        when(countState){
            1,3,5,7 -> {
                setColor(R.color.focus, R.drawable.focus_pomodoro, R.color.center_button_focus, R.color.side_button_focus)
                timeLeft = pomodoroTime
                updateText()
                startTimer()
            }
            2,4,6 -> {
                Log.d(TAG, "initPomodoro: $countDownTimerPomodoro")
                setColor(R.color.short_break, R.drawable.short_break, R.color.center_button_short_break, R.color.side_button_short_break)
                timeLeft = shortBreakTime
                updateText()
                startTimer()
            }
            8 -> {
                setColor(R.color.long_break, R.drawable.long_break, R.color.center_button_long_break, R.color.side_button_long_break)
                timeLeft = longBreakTime
                updateText()
                startTimer()
            }
        }
        binding.pomodoroStartButton.setOnClickListener {
            if(!timeRunning){
                startTimer()
            }
            else{
                pauseTimer()
            }

        }

    }


    private fun pauseTimer() {
        setStyle(false)
        countDownTimerPomodoro?.cancel()
        timeRunning = false
        binding.pomodoroStartButtonImage.setImageResource(R.drawable.ph_play_fill)
    }

    private fun startTimer() {
        setStyle(true)
        binding.pomodoroStartButtonImage.setImageResource(R.drawable.ic_running)
        countDownTimerPomodoro = object : CountDownTimer(timeLeft.toLong() , 1000){
            override fun onTick(millisUntilFinished: Long) {
                updateText()
                timeLeft = millisUntilFinished.toString()
            }

            override fun onFinish() {
                setNotification()
                countState++
                if(countState < 9){
                    initPomodoro()
                }
                else{
                    doYouWantToContinue()
                }
            }
        }.start()
        timeRunning = true
    }
    private fun doYouWantToContinue(){
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Pomodoro is over")
                .setMessage("Do you want to continue?")
                .setPositiveButton("Yes") { _, _ ->
                    countState = 1
                    initPomodoro()
                }
                .setNegativeButton("No") { _, _ ->
                    return@setNegativeButton
                }
                .show()
        }
    }
    private fun updateText(){
        val minutes = (timeLeft.toLong() / 1000) / 60
        val seconds = (timeLeft.toLong() / 1000) % 60
        binding.pomodoroTimeCountMinute.text = if (minutes < 10) "0$minutes" else "$minutes"
        binding.pomodoroTimeCountSecond.text = if (seconds < 10) "0$seconds" else "$seconds"
    }
    @SuppressLint("SetTextI18n")
    private fun setColor(background : Int, image : Int, centerButton : Int, sideButton : Int){
        binding.apply {
            percent.text = "${countState}/8"
            root.setBackgroundColor(
                ResourcesCompat.getColor(resources, background, null))
            pomodoroImage.setImageResource(image)
            pomodoroStartButton.setCardBackgroundColor(
                ResourcesCompat.getColor(resources, centerButton, null))
            pomodoroResetButton.setCardBackgroundColor(
                ResourcesCompat.getColor(resources, sideButton, null))
            pomodoroNextButton.setCardBackgroundColor(
                ResourcesCompat.getColor(resources, sideButton, null))
        }
    }
    private fun setStyle(isRunning : Boolean){
        if (isRunning){
            binding.pomodoroTimeCountMinute.setTypeface(null, Typeface.BOLD)
            binding.pomodoroTimeCountSecond.setTypeface(null, Typeface.BOLD)
        }
        else{
            binding.pomodoroTimeCountMinute.setTypeface(null, Typeface.NORMAL)
            binding.pomodoroTimeCountSecond.setTypeface(null, Typeface.NORMAL)

        }
    }
    private fun setNotification(){
        val data = Data.Builder()
            .putString("key", TypeNotification.PomodoroNotification.name)
            .putString("title", if (countState % 2 == 0) "Focus" else "Short Break")
            .putString("content", if (countState % 2 == 0) "Time to focus" else "Time for a short break")
            .build()
        val notificationRequest : WorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(0, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()
        context?.let {
            WorkManager.getInstance(it).enqueue(notificationRequest)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimerPomodoro?.cancel()
    }


}