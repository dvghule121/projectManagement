package com.example.dailytaskhelper.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.dailytaskhelper.MainActivity
import com.example.dailytaskhelper.R
import com.example.dailytaskhelper.dataClasses.Project
import com.example.dailytaskhelper.dataClasses.todo_item
import com.example.dailytaskhelper.databaseHelper.DatabaseHandler
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [create_project.newInstance] factory method to
 * create an instance of this fragment.
 */
class create_project : Fragment() {
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

        val bundle = arguments


        // Inflate the layout for this fragment
        var datePickerDialog = context?.let {
            DatePickerDialog(
                it,
                { datePicker, yearOfDay, MonthOfDay, dayOfDay -> },
                0,
                0,
                0
            )
        }
        val view = inflater.inflate(R.layout.fragment_add_todo, container, false)
        val Date_et = view.findViewById<EditText>(R.id.date)
        val date = view.findViewById<ImageView>(R.id.date_imgbtn)


        val calendar: Calendar = Calendar.getInstance()
        val currentYear: Int = calendar.get(Calendar.YEAR)
        val currentMonth: Int = calendar.get(Calendar.MONTH)
        val currentDay: Int = calendar.get(Calendar.DAY_OF_MONTH)




        datePickerDialog = DatePickerDialog(
            requireContext(),
            { datePicker, yearOfDay, MonthOfDay, dayOfDay ->
                Date_et.setText(
                    "${
                        String.format(
                            "%02d",
                            dayOfDay
                        )
                    }-${String.format("%02d", MonthOfDay + 1)}-$yearOfDay"
                )
            },
            currentYear,
            currentMonth,
            currentDay,
        )


        date.setOnClickListener {
            datePickerDialog.show()
        }

        view.findViewById<Button>(R.id.add_btn).setOnClickListener {



            val title = view.findViewById<EditText>(R.id.project_name).text
            val task = view.findViewById<EditText>(R.id.initial_task).text

            val Due_date = Date_et.text
            val mydb = DatabaseHandler(requireContext())

            val todoList = ArrayList<todo_item>()
            todoList.add(
                todo_item(
                    title.toString(), task.toString(), Due_date.toString(),
                    false
                )
            )


            val project = Project(
                0,
                title.toString(), Due_date.toString(), todoList
            )
            mydb.insertTask(project)
            val act = activity as MainActivity
            act.change(project_page())


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
         * @return A new instance of fragment add_todo.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            create_project().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}