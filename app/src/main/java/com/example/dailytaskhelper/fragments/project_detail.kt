package com.example.dailytaskhelper.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
 * Use the [project_detail.newInstance] factory method to
 * create an instance of this fragment.
 */
class project_detail : Fragment() {
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
        val view =  inflater.inflate(R.layout.fragment_project_detail, container, false)

        val todolist = view.findViewById<RecyclerView>(R.id.todo_items)
        todolist.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)


        val bundle = this.arguments
        if (bundle != null) {
            val projectname = bundle.getString("project_name")
            var todo_list = DatabaseHandler(requireContext()).getProjectByName(projectname).todolist
            val completed = ArrayList<todo_item>()
            val remained =  ArrayList<todo_item>()


            for (i in todo_list){
                if(i.checked){
                    completed.add(i)
                }else{
                    remained.add(i)
                }
            }




            todolist.adapter = todo_list?.let { TodoItemAdapter(it) }

            view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
                val act = activity as MainActivity
                val bundle = Bundle()
                val createProject = add_task()
                bundle.putString("project_name", projectname)
                createProject.arguments = bundle
                act.change(createProject)
            }
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
         * @return A new instance of fragment project_detail.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            project_detail().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}