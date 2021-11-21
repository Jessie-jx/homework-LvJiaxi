package jessie.cs175.hw5_todolist

import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException

class MainActivity : AppCompatActivity(),CellClickListener {

    var uri: Uri = Uri.parse("content://jessie.cs175.hw5_todolist.provider/ToDoList")
//    var adapter:CustomAdapter?=null
//    var adapter= CustomAdapter(getAllItems(),this)
    private lateinit var adapter:CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter= CustomAdapter(getAllItems(),this)
        val taskRecyclerview=findViewById<RecyclerView>(R.id.task_recyclerview)


        taskRecyclerview.layoutManager=LinearLayoutManager(this)
        taskRecyclerview.adapter=adapter


        findViewById<Button>(R.id.add_btn)?.setOnClickListener {
            startActivity(Intent(this,InputActivity::class.java))

            }
//        findViewById<Button>(R.id.query_btn)?.setOnClickListener {
//            val cursor: Cursor? =
//                contentResolver.query(uri, arrayOf("_id", "task", "created_time"), null, null, null)
//            while (cursor!!.moveToNext()) {
//                Log.d(
//                    "@=>todolist",
//                    "query:" + cursor.getInt(0)
//                        .toString() + " ---- " + cursor.getString(1) + " ---- " + cursor.getString(2)
//                )
//            }
//            cursor.close()
//        }

    }


    private fun getAllItems(): Cursor? {
        return contentResolver.query(uri, arrayOf("_id", "task", "created_time"), null, null, null)
    }

    override fun onCellClickListener(id: Int) {
        contentResolver.delete(uri,"_id = ?", arrayOf("$id"))
        try {
            adapter.swapCursor(getAllItems())
        }catch (e:IOException){
            e.printStackTrace()
        }

    }
}

interface CellClickListener {
    fun onCellClickListener(id:Int)
}