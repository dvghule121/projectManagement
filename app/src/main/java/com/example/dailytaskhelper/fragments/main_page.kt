package com.example.dailytaskhelper.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytaskhelper.MainActivity
import com.example.dailytaskhelper.R
import com.example.dailytaskhelper.adapters.ProjectAdapter
import com.example.dailytaskhelper.adapters.TodoItemAdapter
import com.example.dailytaskhelper.dataClasses.todo_item
import com.example.dailytaskhelper.databaseHelper.DatabaseHandler


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [main_page.newInstance] factory method to
 * create an instance of this fragment.
 */
class main_page : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main_page, container, false)
        val projects = view.findViewById<RecyclerView>(R.id.project_view)
        projects.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val mydb = DatabaseHandler(requireContext())
        val project = mydb.getAllTasks() as ArrayList
        val adapter = ProjectAdapter(requireContext(), R.layout.project_card)
        val todolist = view.findViewById<RecyclerView>(R.id.todo_items)

        if (project.isEmpty()) {
            projects.visibility = View.GONE
            val img1 = view.findViewById<ImageView>(R.id.pr_icon_coffe)
            img1.visibility = View.VISIBLE


            img1.setImageResource(R.drawable.coffee)
            view.findViewById<Button>(R.id.add_btn).visibility = View.VISIBLE
            view.findViewById<Button>(R.id.add_btn).setOnClickListener{
                val act = activity as MainActivity
                act.change(create_project())
            }

            todolist.visibility = View.GONE
            view.findViewById<TextView>(R.id.Todo).visibility = View.GONE

        }
        adapter.setData(project)
        projects.adapter = adapter





        todolist.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val todo_list = ArrayList<todo_item>()

        var total_task = 0
        for (i in project) {
            for (j in i.todolist) {
                total_task = total_task + 1
                if ( ! j.checked) {
                    todo_list.add(j)
                }
            }

        }
        view.findViewById<TextView>(R.id.task_main_detail).text = "${todo_list.size}/$total_task tasks"

        todolist.adapter = TodoItemAdapter(todo_list)
        if (todo_list.isEmpty() and project.isNotEmpty()) {
            todolist.visibility = View.GONE
            view.findViewById<ImageView>(R.id.done).visibility = View.VISIBLE
            view.findViewById<TextView>(R.id.done_text).visibility = View.VISIBLE

        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment main_page.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            main_page().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}