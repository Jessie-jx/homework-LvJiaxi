package jessie.cs175.hw4_translator.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context) : SQLiteOpenHelper(context, "WordBook.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("@=>Translator", "dbHelper onCreate")
        db?.execSQL("CREATE TABLE IF NOT EXISTS $WORDS_TABLE_NAME(_id INTEGER PRIMARY KEY AUTOINCREMENT, word TEXT, usphone TEXT, ukphone TEXT, audiourl TEXT, examType TEXT, meanings TEXT, changes TEXT, sentences TEXT, star INTEGER DEFAULT 0, deleted INTEGER DEFAULT 0)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS $USER_TABLE_NAME(user_id INTEGER PRIMARY KEY AUTOINCREMENT, user_name TEXT, user_email TEXT, user_password TEXT)")
    }

    companion object {
        const val WORDS_TABLE_NAME = "WordBook"
        const val USER_TABLE_NAME = "UserTable"
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}