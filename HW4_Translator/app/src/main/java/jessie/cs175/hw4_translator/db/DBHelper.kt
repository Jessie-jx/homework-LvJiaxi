package jessie.cs175.hw4_translator.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context) : SQLiteOpenHelper(context, "WordBook.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("@=>Translator", "dbHelper onCreate")
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME(_id INTEGER PRIMARY KEY AUTOINCREMENT, word TEXT, usphone TEXT, ukphone TEXT, audiourl TEXT, examType TEXT, meanings TEXT, changes TEXT, sentences TEXT)")
    }

    companion object {
        const val TABLE_NAME = "WordBook"
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}