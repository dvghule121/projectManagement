package com.example.dailytaskhelper.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytaskhelper.MainActivity
import com.example.dailytaskhelper.R
import com.example.dailytaskhelper.adapters.ProjectAdapter
import com.example.dailytaskhelper.databaseHelper.DatabaseHandler

import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [project_page.newInstance] factory method to
 * create an instance of this fragment.
 */
class project_page : Fragment() {
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
        val view =  inflater.inflate(R.layout.fragment_project_page, container, false)
        val projects = view.findViewById<RecyclerView>(R.id.project_items)
        projects.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)

        val mydb = DatabaseHandler(requireContext())

        val project = mydb.getAllTasks() as ArrayList

        val adapter = ProjectAdapter(requireContext(),R.layout.project_card_long)
        projects.adapter = adapter

        adapter.setData(project)



        val btn = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        btn.setOnClickListener{
           val act =  activity as MainActivity
            act.change(create_project())
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
         * @return A new instance of fragment project_page.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            project_page().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}