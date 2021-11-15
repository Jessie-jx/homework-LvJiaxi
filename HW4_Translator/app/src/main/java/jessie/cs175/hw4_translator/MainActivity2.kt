package jessie.cs175.hw4_translator


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.gson.GsonBuilder
import com.google.gson.internal.UnsafeAllocator.create
import okhttp3.*
import okhttp3.EventListener
import okhttp3.OkHttpClient
import java.io.IOException
import java.net.URI.create

class MainActivity2 : AppCompatActivity() {
    var showText: TextView? = null

    val okhttpListener = object : EventListener() {
        override fun dnsStart(call: Call, domainName: String) {
            super.dnsStart(call, domainName)
            runOnUiThread {
                showText?.text = showText?.text.toString() + "\nDNS Search" + domainName
            }

        }

        override fun responseBodyStart(call: Call) {
            super.responseBodyStart(call)
            runOnUiThread {
                showText?.text = showText?.text.toString() + "\nResponse Start"
            }

        }
    }

    val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(TimeConsumeInterceptor())
        .eventListener(okhttpListener).build()
    val gson = GsonBuilder().create()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        showText = findViewById(R.id.show_text)

        val editText = findViewById<EditText>(R.id.input)

        editText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                Log.d("@=>enter", "get!")
                showText?.text = ""
                click(editText.text.toString())
                return@OnKeyListener true
            }
            false
        })
    }

    fun request(url: String, callback: Callback) {
        val request: Request =
            Request.Builder().url(url).header("User-Agent", "Sjtu-Android-OKHttp").build()
        client.newCall(request).enqueue(callback)
    }

    fun click(word: String) {
        val url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word
//        val url="https://dict.youdao.com/jsonapi?q=time"
        request(url, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    showText?.text = e.message
                }

            }

            override fun onResponse(call: Call, response: Response) {
                val bodyString = response.body?.string()
                val freeDictBean = gson.fromJson(bodyString, FreeDictBean::class.java)
                Log.d("@=>session","success")
//                val doubanBean = gson.fromJson(bodyString, DoubanBean::class.java)
//                val youdaoBean = gson.fromJson(bodyString, com.bejson.pojo.JsonRootBean::class.java)

                runOnUiThread {
                    showText?.text = "${showText?.text.toString()} \n\n\n" +
                            "Word: ${freeDictBean.word} \n" +
                            "phonetics: ${freeDictBean.phonetics.get(0).text}  "
                }
            }
        })


    }


}