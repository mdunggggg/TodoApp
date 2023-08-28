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
                         tvTaskName.text = task.title
                         tvTaskTime.text = task.dueTime
                         tvTaskDescription.text = task.content

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