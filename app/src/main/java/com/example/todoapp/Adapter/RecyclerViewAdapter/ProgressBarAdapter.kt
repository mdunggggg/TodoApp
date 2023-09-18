package com.example.todoapp.Adapter.RecyclerViewAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Model.CategoryAndTask
import com.example.todoapp.Utils.ColorUtils
import com.example.todoapp.databinding.ItemProgressbarBinding

class ProgressBarAdapter() : ListAdapter<CategoryAndTask, ProgressBarAdapter.ProgressBarViewHolder>(DiffCallback) {
    inner class ProgressBarViewHolder(private val binding : ItemProgressbarBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(categoryAndTask: CategoryAndTask){
            binding.apply {
                progressBar.trackColor = ColorUtils.getColorTransparent(categoryAndTask.color)
                progressBar.setIndicatorColor(categoryAndTask.color)
                if(categoryAndTask.totalTask == 0){
                    progressBar.progress = 0
                }
                else{
                    progressBar.progress = categoryAndTask.totalTaskFinish * 100 / categoryAndTask.totalTask
                }

            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProgressBarViewHolder {
        return ProgressBarViewHolder(ItemProgressbarBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }



    override fun onBindViewHolder(holder: ProgressBarViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }
    companion object{
        private val DiffCallback = object : DiffUtil.ItemCallback<CategoryAndTask>(){
            override fun areItemsTheSame(
                oldItem: CategoryAndTask,
                newItem: CategoryAndTask
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: CategoryAndTask,
                newItem: CategoryAndTask
            ): Boolean {
                return oldItem.titleCategory == newItem.titleCategory ||
                        oldItem.totalTask == newItem.totalTask ||
                        oldItem.totalTaskFinish == newItem.totalTaskFinish
            }


        }
    }
}