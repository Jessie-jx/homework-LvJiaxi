package jessie.cs175.hw4_translator.db

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.util.Log

class WordBookProvider : ContentProvider() {

    val AUTOHORITY = "jessie.cs175.hw4_translator.db.provider"
    val Word_Table_Code = 1
    val User_Table_Code = 2
    lateinit var db: SQLiteDatabase
    private var mMatcher: UriMatcher? = null

    init {
        mMatcher = UriMatcher(UriMatcher.NO_MATCH)
        mMatcher?.addURI(AUTOHORITY, "WordBook", Word_Table_Code)
        mMatcher?.addURI(AUTOHORITY, "UserTable", User_Table_Code)
    }

    override fun onCreate(): Boolean {
        Log.d("@=>Translator", "$this onCreate")
        val mDbHelper = DBHelper(context!!)
        db = mDbHelper.writableDatabase

        return true
    }


    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val table: String = getTableName(uri)
        return db.query(table, projection, selection, selectionArgs, null, null, sortOrder, null)
    }

    override fun getType(uri: Uri): String? {
        when (mMatcher!!.match(uri)) {
            Word_Table_Code -> return "type_wordbook"
            User_Table_Code -> return "type_user"
        }
        return ""
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val table: String = getTableName(uri)
        db.insert(table, null, values)
        context!!.contentResolver.notifyChange(uri, null) // 当该URI的ContentProvider数据发生变化时，通知外界
        return uri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val table: String = getTableName(uri)
        db.delete(table, selection, selectionArgs)
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val table: String = getTableName(uri)
        db.update(table, values, selection, selectionArgs)
        return 0
    }

    private fun getTableName(uri: Uri): String {
        var tableName = ""
        when (mMatcher!!.match(uri)) {
            Word_Table_Code -> tableName = DBHelper.WORDS_TABLE_NAME
            User_Table_Code -> tableName = DBHelper.USER_TABLE_NAME
        }
        return tableName
    }


}