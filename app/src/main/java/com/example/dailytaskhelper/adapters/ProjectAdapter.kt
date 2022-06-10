package com.example.dailytaskhelper.adapters

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytaskhelper.MainActivity
import com.example.dailytaskhelper.R
import com.example.dailytaskhelper.dataClasses.Project
import com.example.dailytaskhelper.dataClasses.todo_item
import com.example.dailytaskhelper.databaseHelper.DatabaseHandler
import com.example.dailytaskhelper.fragments.project_detail
import com.example.dailytaskhelper.fragments.project_page
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class ProjectAdapter(val context: Context, val layout_resource: Int) :
    RecyclerView.Adapter<ProjectViewHolder>() {

    var project = emptyList<Project>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(layout_resource, parent, false)
        return ProjectViewHolder(view)
    }


    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project_item = project[position]
        holder.project_title.text = project_item.title


        val due_days = project_item.due_days
        var date = SimpleDateFormat("dd-MM-yyyy").parse(due_days)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentDate =
                SimpleDateFormat("dd-MM-yyyy").parse(
                    LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
                    )
                )


            val diff: Long = date.time - currentDate.time
            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24

            if (days >= 0) {
                holder.project_due.text = "$days days"
            }else{
                holder.project_due.text = "Expired"
            }

        } else {
            holder.project_due.text = "till $due_days"
        }

        val completed = ArrayList<todo_item>()
        for (i in project_item.todolist) {
            if (i.checked) {
                completed.add(i)
            }

        }

        holder.project_task.text =
            completed.size.toString() + " / " + project_item.todolist.size.toString()


        holder.card.setOnClickListener {
            val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
            val transaction: FragmentTransaction =
                manager.beginTransaction() //create an instance of Fragment-transaction
            val fragment = project_detail()
            val bundle = Bundle()
            bundle.putString("project_name", project_item.title)
            fragment.setArguments(bundle)

            transaction.replace(R.id.main_view, fragment).commit()

        }


        holder.progress_bar.max = project_item.todolist.size
        holder.delete.setOnClickListener {
            val mydb = DatabaseHandler(holder.card.context)
            mydb.deleteTask(project_item.id)
            if (holder.delete.context is MainActivity) {
                val activity = holder.delete.context as MainActivity
                activity.change(project_page())
            }

        }
        if (layout_resource == R.layout.project_card_long) {

            if (position == 0) {
                holder.card.setCardBackgroundColor(
                    holder.card.getContext().getResources().getColor(R.color.pink)
                )

            } else if (position % 2 == 0) {

                holder.card.setCardBackgroundColor(
                    holder.card.getContext().getResources().getColor(R.color.light_blue)
                )

            } else {
                holder.card.setCardBackgroundColor(
                    holder.card.getContext().getResources().getColor(R.color.light_yellow)
                )
                holder.project_due.setTextColor(
                    holder.card.getContext().getResources().getColor(R.color.black)
                )
                holder.project_title.setTextColor(
                    holder.card.getContext().getResources().getColor(R.color.black)
                )
                holder.project_task.setTextColor(
                    holder.card.getContext().getResources().getColor(R.color.black)
                )
                holder.progress_text.setTextColor(
                    holder.card.getContext().getResources().getColor(R.color.black)
                )
            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.progress_bar.setProgress(completed.size, true)

        }
        else{
            holder.progress_bar.visibility = View.GONE
        }


    }

    fun setData(projectlist: List<Project>) {
        this.project = projectlist

    }

    override fun getItemCount(): Int {
        return project.size
    }
}

class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val project_title = itemView.findViewById<TextView>(R.id.project_title)
    val project_due = itemView.findViewById<TextView>(R.id.due_period)
    val project_task = itemView.findViewById<TextView>(R.id.project_task)
    val progress_bar = itemView.findViewById<ProgressBar>(R.id.project_progress)
    val card = itemView.findViewById<MaterialCardView>(R.id.project_card)
    val progress_text = itemView.findViewById<TextView>(R.id.task_main_detail)
    val delete = itemView.findViewById<ImageButton>(R.id.delete)


}
