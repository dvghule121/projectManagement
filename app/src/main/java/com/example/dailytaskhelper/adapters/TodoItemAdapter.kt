package com.example.dailytaskhelper.adapters

import android.graphics.Paint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytaskhelper.MainActivity
import com.example.dailytaskhelper.R
import com.example.dailytaskhelper.dataClasses.todo_item
import com.example.dailytaskhelper.databaseHelper.DatabaseHandler
import com.example.dailytaskhelper.fragments.main_page
import com.example.dailytaskhelper.fragments.project_detail


class TodoItemAdapter(val project: ArrayList<todo_item>) : RecyclerView.Adapter<TodoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.todocard, parent, false)
        return TodoViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val project_item = project[position]
        holder.project_task.text = project_item.todo_task
        holder.due_date.text = "Due till ${project_item.Duedate}"
        holder.project_task.isChecked = project_item.checked
        holder.project_name.text = project_item.project_name
        if (project_item.checked){
            holder.project_task.setPaintFlags(holder.project_task.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
            holder.project_task.isEnabled = false
            holder.due_date.text = "Done"
        }



        holder.project_task.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val mydb = DatabaseHandler(context = holder.project_name.context)
                val pr = mydb.getProjectByName(project_item.project_name)
                project_item.checked = isChecked
                for (i in pr.todolist){
                    if (i.todo_task == project_item.todo_task){
                        Log.d("TAG", "onBindViewHolder: task matched")
                        i.checked = isChecked
                    }
                }


                mydb.updateTask(pr.id,pr)
                if (holder.project_name.context is MainActivity) {
                    val activity = holder.project_name.context as MainActivity
                    activity.change(main_page())
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return project.size
    }
}

class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val project_task = itemView.findViewById<CheckBox>(R.id.task)
    val project_name = itemView.findViewById<TextView>(R.id.project_name)
    val due_date = itemView.findViewById<TextView>(R.id.due_date)


}
