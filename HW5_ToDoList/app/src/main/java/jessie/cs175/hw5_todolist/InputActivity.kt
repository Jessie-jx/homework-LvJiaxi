package jessie.cs175.hw5_todolist

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class InputActivity : AppCompatActivity() {
    var uri: Uri = Uri.parse("content://jessie.cs175.hw5_todolist.provider/ToDoList")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        val editText=findViewById<EditText>(R.id.input_text)
        val confirmBtn=findViewById<Button>(R.id.confirm_btn)
        val cancelBtn=findViewById<Button>(R.id.cancel_btn)

        confirmBtn.setOnClickListener {
            val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
            val currentDate = sdf.format(Date())
            val values = ContentValues()
            values.put("task", editText.text.toString())
            values.put("created_time",currentDate)
            contentResolver.insert(uri, values)
            Log.d("@=>todolist", "${editText.text.toString()} add successfully")

            val intent=Intent(this,MainActivity::class.java)
            finish()
            startActivity(intent)
        }

        cancelBtn.setOnClickListener {
            val intent2=Intent(this,MainActivity::class.java)
            finish()
            startActivity(intent2)
        }


    }
}