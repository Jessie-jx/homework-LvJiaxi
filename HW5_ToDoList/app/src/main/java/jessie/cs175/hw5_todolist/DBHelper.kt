package jessie.cs175.hw5_todolist

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper (context: Context):SQLiteOpenHelper(context,"toDoList.db",null,1){
    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("@=>todolist", "dbHelper onCreate")
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME(_id INTEGER PRIMARY KEY AUTOINCREMENT, task TEXT, created_time TEXT)")
    }

    companion object {
        const val TABLE_NAME = "ToDoList"
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}