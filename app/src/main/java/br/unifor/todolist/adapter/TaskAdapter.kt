package br.unifor.todolist.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.unifor.todolist.R
import br.unifor.todolist.model.Task

class TaskAdapter(val tasks: List<Task>) :RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){

    private var listener :TaskItemListerner? = null

    class TaskViewHolder(itemView :View, listener: TaskItemListerner?) :RecyclerView.ViewHolder(itemView){
        val taskName = itemView.findViewById<TextView>(R.id.item_task_textview_task)
        val taskDescription = itemView.findViewById<TextView>(R.id.item_task_textview_description)
        val taskFinished = itemView.findViewById<View>(R.id.item_task_view_finished)

        init {
            itemView.setOnClickListener{
                listener?.onClick(it, adapterPosition)
            }
            itemView.setOnLongClickListener{
                listener?.onLongClick(it, adapterPosition)
                true
            }

        }

    }

    fun setTaskItemListener(listener: TaskItemListerner?){
        this.listener = listener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        val holder = TaskViewHolder(itemView, listener)
        return holder
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.taskName.text = tasks[position].name
        holder.taskDescription.text = tasks[position].description

        if(tasks[position].isDone){
            holder.taskFinished.setBackgroundColor(Color.BLUE)
        } else {
            holder.taskFinished.setBackgroundColor(Color.RED)
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}