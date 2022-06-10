package com.example.dailytaskhelper.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytaskhelper.MainActivity
import com.example.dailytaskhelper.R
import com.example.dailytaskhelper.adapters.TodoItemAdapter
import com.example.dailytaskhelper.dataClasses.todo_item
import com.example.dailytaskhelper.databaseHelper.DatabaseHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [todo_manager.newInstance] factory method to
 * create an instance of this fragment.
 */
class todo_manager : Fragment() {
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
        val view =  inflater.inflate(R.layout.fragment_todo_manager, container, false)

        val mydb = DatabaseHandler(requireContext())
        val project = mydb.getAllTasks() as ArrayList

        val todolist = view.findViewById<RecyclerView>(R.id.todo_items)
        todolist.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        val todo_list = ArrayList<todo_item>()


        for (i in project) {
            for (j in i.todolist) {
                if ( ! j.checked) {
                    todo_list.add(j)
                }
            }

        }
        for (i in project) {
            for (j in i.todolist) {
                if ( j.checked) {
                    todo_list.add(j)
                }
            }

        }



        if (project.isEmpty()) {
            todolist.visibility = View.GONE
            val img = view.findViewById<ImageView>(R.id.done)
            img.visibility = View.VISIBLE
            img.setImageResource(R.drawable.no_data_found)

            view.findViewById<TextView>(R.id.done_text).visibility = View.VISIBLE
            view.findViewById<TextView>(R.id.done_text).text =
                "No Todo yet !! \nConsider Adding one "
        }

        todolist.adapter = TodoItemAdapter(todo_list)
        if (todo_list.isEmpty() and project.isNotEmpty()){
            todolist.visibility = View.GONE
            view.findViewById<ImageView>(R.id.done).visibility = View.VISIBLE
            view.findViewById<TextView>(R.id.done_text).visibility = View.VISIBLE

        }



        view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            val act = activity as MainActivity
            act.change(add_task())
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
         * @return A new instance of fragment todo_manager.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            todo_manager().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}