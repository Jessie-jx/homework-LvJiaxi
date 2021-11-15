package jessie.cs175.hw4_translator

import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.io.IOException
import androidx.cardview.widget.CardView


class WordDetailActivity : AppCompatActivity() {
//    var myMediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_detail2)
        setSupportActionBar(findViewById(R.id.my_toolbar2))

        // layouts
        val basicInfoCard=findViewById<CardView>(R.id.basic_info_card)
        val meaningCard=findViewById<LinearLayout>(R.id.meaning_card)
        val changesCard=findViewById<LinearLayout>(R.id.changes_card)
        val sentencesCard=findViewById<LinearLayout>(R.id.sentences_card)


        val word = findViewById<TextView>(R.id.word)
        val phone = findViewById<TextView>(R.id.phone)
        val examType = findViewById<TextView>(R.id.exam_type)
        val meanings = findViewById<TextView>(R.id.meaning)
        val audioButton = findViewById<ImageView>(R.id.audio_play_btn)
        val changes = findViewById<TextView>(R.id.changes)
        val sentences = findViewById<TextView>(R.id.sentences)

        val wordDetail = intent.extras?.get("wordInfo") as WordDetail
        word.text = wordDetail.word

        if (wordDetail.usphone=="null" && wordDetail.ukphone!="null") {
            phone.text = "英 /${wordDetail.ukphone}/"
        } else if (wordDetail.usphone!="null" && wordDetail.ukphone=="null") {
            phone.text = "美 /${wordDetail.usphone}/"
        } else if (wordDetail.usphone!="null" && wordDetail.ukphone!="null") {
            phone.text = "美 /${wordDetail.usphone}/    英 /${wordDetail.ukphone}/"
        } else {
            phone.visibility= View.GONE
        }

        if (wordDetail.examType=="null") {
            examType.visibility=View.GONE
        } else{
            examType.text = wordDetail.examType
        }

        if (wordDetail.meanings=="") {
            meanings.text ="未在词库中查询到该词哦"+"\n"
        }else {
            meanings.text = wordDetail.meanings
        }

        if (wordDetail.changes=="") {
            changesCard.visibility=View.GONE
        } else {
            changes.text = wordDetail.changes
        }

        if (wordDetail.sentences=="") {
            sentencesCard.visibility=View.GONE
        } else {
            sentences.text = wordDetail.sentences
        }


        val url = wordDetail.audiourl
        if (url=="null") {
            audioButton.visibility=View.GONE
        }


        // 获取发音
        audioButton.setOnClickListener {

            val mediaPlayer: MediaPlayer? = MediaPlayer().apply {
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                try {
                    setDataSource(url)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                prepareAsync() // might take long! (for buffering, etc)
                setOnPreparedListener(object : OnPreparedListener {
                    override fun onPrepared(mp: MediaPlayer?) {
                        if (mp != null) {
                            mp.start()
                        }
                    }
                })
                setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
                    override fun onCompletion(mp: MediaPlayer?) {
                        if (mp != null) {
                            mp.release()
                        }
                    }
                })
            }

        }
    }


}


