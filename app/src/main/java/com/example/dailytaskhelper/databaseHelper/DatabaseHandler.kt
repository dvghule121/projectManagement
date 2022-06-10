package com.example.dailytaskhelper.databaseHelper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.dailytaskhelper.dataClasses.Project
import com.example.dailytaskhelper.dataClasses.todo_item
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private lateinit var db : SQLiteDatabase
    private val TABLE_NAME = "PROJECT_TABLE"
    private val COL_1 = "ID"
    private val COL_2 = "NAME"
    private val COL_3 = "DUE_DATE"
    private val COL_4 = "TODO_LIST"


    override fun onCreate(db: SQLiteDatabase) {
        val SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME(ID INTEGER PRIMARY KEY AUTOINCREMENT , NAME TEXT , TOTAL_TASK INTEGER, COMPLETED_TASK INTEGER, DUE_DATE TEXT, TODO_LIST TEXT, COMPLETED_LIST TEXT )"
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    fun insertTask(model: Project) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_2, model.title)
        values.put(COL_3, model?.due_days)
        values.put(COL_4, Gson().toJson(model.todolist))

        db.insert(TABLE_NAME, null, values)
    }

    fun updateTask(id: Int, model: Project?) {
        db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_2, model?.title)
        values.put(COL_3, model?.due_days)
        values.put(COL_4, Gson().toJson(model?.todolist))
        db.update(TABLE_NAME, values, "ID=?", arrayOf(id.toString()))
    }

    @SuppressLint("Range")
    fun getProjectByName(name:String?):Project{
        db = this.writableDatabase
        var cursor: Cursor? = null
        val modelList: ArrayList<Project> = ArrayList()
        db.beginTransaction()
        try {
            cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE NAME = '$name'",null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        val todoList = cursor.getString(cursor.getColumnIndex(COL_4))
                        val myType = object : TypeToken<ArrayList<todo_item>>() {}.type
                        val logs = Gson().fromJson<ArrayList<todo_item>>(todoList, myType)
                        val task = Project(cursor.getInt(cursor.getColumnIndex(COL_1)),cursor.getString(cursor.getColumnIndex(COL_2)),cursor.getString(cursor.getColumnIndex(COL_3)), logs)

                        modelList.add(task)
                    } while (cursor.moveToNext())
                }
            }
        }finally {
            db.endTransaction()
            cursor?.close()
        }
         return modelList[0]
        }

    fun deleteTask(id: Int) {
        db = this.writableDatabase
        db.delete(TABLE_NAME, "ID=?", arrayOf(id.toString()))
    }


    @SuppressLint("Range")
    fun getAllTasks(): ArrayList<Project>? {
        db = this.writableDatabase
        var cursor: Cursor? = null
        val modelList: ArrayList<Project> = ArrayList()
        db.beginTransaction()
        try {
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        val todoList = cursor.getString(cursor.getColumnIndex(COL_4))
                        val myType = object : TypeToken<ArrayList<todo_item>>() {}.type
                        val logs = Gson().fromJson<ArrayList<todo_item>>(todoList, myType)
                        val task = Project(cursor.getInt(cursor.getColumnIndex(COL_1)),cursor.getString(cursor.getColumnIndex(COL_2)),cursor.getString(cursor.getColumnIndex(COL_3)), logs)

                        modelList.add(task)
                    } while (cursor.moveToNext())
                }
            }
        } finally {
            db.endTransaction()
            cursor?.close()
        }
        return modelList
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME");
        onCreate(db);
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 4
        private val DATABASE_NAME = "PROJECT_DATABASE"
    }
}