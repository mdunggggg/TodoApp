package com.example.todoapp.Adapter.RecyclerViewAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Model.Task
import com.example.todoapp.databinding.ItemTaskRvBinding
import com.example.todoapp.databinding.ItemTaskSearchViewBinding

class TaskSearchViewAdapter : ListAdapter<Task, TaskSearchViewAdapter.TaskSearchViewViewHolder>(DiffCallback) {
    inner class TaskSearchViewViewHolder(private val binding : ItemTaskSearchViewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(task : Task){
            binding.apply {
                tvTaskSearchView.apply {
                    text = task.title
                    paintFlags = if(task.isFinish){
                        paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                    }else{
                        paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    }
                }
                cvCategoryColorSearchView.setCardBackgroundColor(task.color)
                tvCategoryNameSearchView.text = task.titleCategory
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskSearchViewViewHolder {
        return TaskSearchViewViewHolder(ItemTaskSearchViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TaskSearchViewViewHolder, position: Int) {
        val current = getItem(position)

        holder.bind(current)
    }


    companion object{
        private val DiffCallback = object : DiffUtil.ItemCallback<Task>(){
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.idTask == newItem.idTask
            }

        }
    }
}