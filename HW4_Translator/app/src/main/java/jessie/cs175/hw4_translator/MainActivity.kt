package jessie.cs175.hw4_translator

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import cn.json.dict.JsonRootBean
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import android.net.http.HttpResponseCache
import okhttp3.*
import okhttp3.EventListener
import java.io.File
import okhttp3.CacheControl
import java.io.IOException
import java.util.concurrent.TimeUnit


const val HEADER_CACHE_CONTROL = "Cache-Control"
const val HEADER_PRAGMA = "Pragma"
const val HISTORY_WORDS_LIMIT: Int = 10

class MainActivity : AppCompatActivity() {
    var showText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))


        showText = findViewById(R.id.show_text)
        val editText = findViewById<EditText>(R.id.input)
        var dateString = findViewById<TextView>(R.id.date)
        var dailySentence = findViewById<TextView>(R.id.daily_sentence)
        var dailyImage = findViewById<ImageView>(R.id.daily_img)
        var historyWordsView = findViewById<ListView>(R.id.history_words)
        val clearBtn = findViewById<Button>(R.id.clear_btn)


        val sdf = SimpleDateFormat("yyyy/MM/dd")
        val currentDate = sdf.format(Date())

        dateString.text = "每日一句  $currentDate"
        dailySentence.text =
            "Some of us get dipped in flat, some in satin, some in gloss.... But every once in a while, you find someone who's iridescent, and when you do, nothing will ever compare."
        dailyImage.setImageResource(R.drawable.flipped)

//        var historyWords: Queue<String> = LinkedList<String>()
        var historyWords: ArrayList<String> = arrayListOf<String>()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, historyWords)
        historyWordsView.adapter = adapter


        // 输入框输入单词
        editText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                showText?.text = ""

                // 修改历史记录
                if (editText.text.toString() in historyWords) {
                    historyWords.remove(editText.text.toString())
                }
                if (historyWords.size == HISTORY_WORDS_LIMIT) {
                    historyWords.remove(historyWords[historyWords.size - 1])
                }
                historyWords.add(0, editText.text.toString())
                adapter.notifyDataSetChanged()

                //进行url单词查询
                click(editText.text.toString())

                return@OnKeyListener true
            }
            false
        })

        // 查询历史记录中的单词
        historyWordsView.setOnItemClickListener { parent, view, position, id ->
            showText?.text = ""
            val researchWord = historyWords[position]
            click(researchWord)
            historyWords.remove(researchWord)
            historyWords.add(0, researchWord)
            adapter.notifyDataSetChanged()
        }

        // 清空输入框
        clearBtn.setOnClickListener {
            editText.setText("")
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editText.text.toString() != "") {
                    clearBtn.visibility = View.VISIBLE
                } else {
                    clearBtn.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    val okhttpListener = object : EventListener() {
        override fun dnsStart(call: Call, domainName: String) {
            super.dnsStart(call, domainName)
            runOnUiThread {
                showText?.text = showText?.text.toString() + "\nDNS Search $domainName"
            }
        }

        override fun responseBodyStart(call: Call) {
            super.responseBodyStart(call)
            runOnUiThread {
                showText?.text = showText?.text.toString() + "\nResponse start"
            }
        }
    }


    val client: OkHttpClient =
        OkHttpClient.Builder().addInterceptor(TimeConsumeInterceptor())
            .eventListener(okhttpListener).build()

    var retrofit = Retrofit.Builder().baseUrl("https://dict.youdao.com/")
        .addConverterFactory(GsonConverterFactory.create()).client(client).build()

//    override fun onStop() {
//        super.onStop()
//        val cache = HttpResponseCache.getInstalled()
//        cache?.flush()
//    }

    fun request(callback: retrofit2.Callback<JsonRootBean>, word: String) {
        try {
//            val YoudaoService = retrofit.create(YoudaoService::class.java)
//            YoudaoService.getWordMeaning(word,1,"json").enqueue(callback)

            val YoudaoServiceComplex = retrofit.create(YoudaoServiceComplex::class.java)
            YoudaoServiceComplex.getWordInfo(word).enqueue(callback)

        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun click(word: String) {
        request(object : Callback<JsonRootBean> {
            override fun onResponse(
                call: retrofit2.Call<JsonRootBean>,
                response: Response<JsonRootBean>
            ) {
                if (response.isSuccessful) {

                    // COMPLEX API
                    val youdaoBean = response.body()

                    val usphone = youdaoBean?.ec?.word?.get(0)?.usphone.toString()
                    val ukphone = youdaoBean?.ec?.word?.get(0)?.ukphone.toString()
                    val audiourl = youdaoBean?.collins_primary?.gramcat?.get(0)?.audiourl.toString()
                    val examType =
                        youdaoBean?.ec?.exam_type?.joinToString(separator = '/'.toString())
                            .toString()

                    var meanings = ""
                    val trs_list = youdaoBean?.ec?.word?.get(0)?.trs
                    val trs_iterator = trs_list?.listIterator()
                    if (trs_iterator != null) {
                        var cnt: Int = 1
                        for (trs_item in trs_iterator) {
                            if (cnt <= 2) {
                                val meaning = trs_item?.tr?.get(0)?.l?.i?.get(0).toString()
                                meanings = meanings + meaning + "\n"
                                cnt += 1
                            } else {
                                break
                            }

                        }
                    }


                    var changes = ""
                    val changes_list = youdaoBean?.ec?.word?.get(0)?.wfs
                    val changes_iterator = changes_list?.listIterator()
                    if (changes_iterator != null) {
                        for (changes_item in changes_iterator) {
                            changes =
                                changes + changes_item.wf.name + "：" + changes_item.wf.value + "\n"
                        }
                    }

                    var sentences = ""
                    val sentence_pair_list = youdaoBean?.blng_sents_part?.sentence_pair
                    val sentence_pair_iterator = sentence_pair_list?.listIterator()
                    if (sentence_pair_iterator != null) {
                        var cnt: Int = 1
                        for (sentence_pair_item in sentence_pair_iterator) {
                            if (cnt <= 3) {
                                sentences =
                                    sentences + cnt.toString() + ". " + sentence_pair_item.sentence + "\n" + "    " + sentence_pair_item.sentence_translation + "\n"
                                cnt += 1
                            } else {
                                break
                            }
                        }
                    }


                    val wordInfo = WordDetail(
                        word, usphone, ukphone, audiourl, examType,
                        meanings, changes, sentences
                    )
                    val intent = Intent(this@MainActivity, WordDetailActivity::class.java)
                    intent.putExtra("wordInfo", wordInfo as Serializable)
                    startActivity(intent)

                } else {
                    showText?.text = "failed code is :${response.code()}"
                }
            }

            override fun onFailure(call: retrofit2.Call<JsonRootBean>, t: Throwable) {
                showText?.text = t.message
            }
        }, word)

    }

    fun isInternetAvailable(): Boolean {
        var isConnected: Boolean = false // Initial Value
        try {
            var connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            if (activeNetwork != null && activeNetwork.isConnected)
                isConnected = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return isConnected
    }
}

