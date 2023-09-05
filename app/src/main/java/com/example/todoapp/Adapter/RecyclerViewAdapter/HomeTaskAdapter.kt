package com.example.todoapp.Adapter.RecyclerViewAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Interfaces.IItemTaskListener
import com.example.todoapp.Model.Task
import com.example.todoapp.databinding.ItemTaskRvBinding

class HomeTaskAdapter(
    private val onItemClicked : (Task) -> Unit

) : ListAdapter<Task, HomeTaskAdapter.HomeTaskViewHolder>(DiffCallback) {
        inner class HomeTaskViewHolder(private val binding : ItemTaskRvBinding) : RecyclerView.ViewHolder(binding.root){
               fun bind(task : Task){
                      binding.apply {
                         tvTaskName.apply {
                             text = task.title
                             paintFlags = if(task.isFinish){
                                 paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                             }else{
                                 paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
                             }
                         }
                         tvTaskTime.text = task.dueTime
                         tvTaskDescription.apply {
                             text = task.content
                             paintFlags = if(task.isFinish){
                                 paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                             }else{
                                 paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
                             }
                         }
                          tvCategoryName.text = task.titleCategory
                          itemView.setOnClickListener {
                                onItemClicked(task)
                          }


                      }
               }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeTaskViewHolder {
                return HomeTaskViewHolder(ItemTaskRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

    override fun onBindViewHolder(holder: HomeTaskViewHolder, position: Int) {
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