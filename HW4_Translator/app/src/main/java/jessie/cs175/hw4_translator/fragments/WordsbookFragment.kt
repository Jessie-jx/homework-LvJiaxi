package jessie.cs175.hw4_translator.fragments

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jessie.cs175.hw4_translator.R
import jessie.cs175.hw4_translator.adapter.MyRecyclerviewAdapter
import android.animation.AnimatorSet

import android.animation.ObjectAnimator
import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import jessie.cs175.hw4_translator.WordDetail
import jessie.cs175.hw4_translator.WordDetailActivity
import java.io.Serializable
import java.util.ArrayList


class WordsbookFragment : Fragment(), CellClickListener {
    private var animatorSet2: AnimatorSet? = null
    var uri: Uri = Uri.parse("content://jessie.cs175.hw4_translator.db.provider/WordBook")
    private lateinit var adapter: MyRecyclerviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_wordsbook, container, false)

        adapter = MyRecyclerviewAdapter(getAllItems(), this)
        val wordbookRecyclerview = view.findViewById<RecyclerView>(R.id.word_book_list)


        wordbookRecyclerview.layoutManager = LinearLayoutManager(activity as Context)
        wordbookRecyclerview.adapter = adapter



        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        view?.postDelayed(object : Runnable {
            override fun run() {
                val lottie_alpha = ObjectAnimator.ofFloat(
                    view!!.findViewById(R.id.loading_lottie),
                    "alpha",
                    1f,
                    0f
                )
                lottie_alpha.duration = 1000
                lottie_alpha.repeatCount = 0

                val wordbook_alpha =
                    ObjectAnimator.ofFloat(
                        view!!.findViewById(R.id.word_book_list),
                        "alpha",
                        0f,
                        1f
                    )
                wordbook_alpha.duration = 1000
                wordbook_alpha.repeatCount = 0

                animatorSet2 = AnimatorSet()
                animatorSet2!!.playSequentially(lottie_alpha, wordbook_alpha)
                animatorSet2!!.start()
            }

        }, 3000)
    }

    override fun onResume() {
        super.onResume()
        adapter.swapCursor(getAllItems())
    }

//    override fun onCellClickListener(word: String) {
////        wordInfo=
////        val intent = Intent(activity, WordDetailActivity::class.java)
////        intent.putExtra("wordInfo", wordInfo as Serializable)
////        startActivity(intent)
//    }

    private fun getAllItems(): Cursor? {
        return requireActivity().contentResolver.query(
            uri,
            arrayOf("_id", "word", "usphone"),
            null,
            null,
            null
        )
    }

    override fun onCellClickListener(word: String) {
        val singleCursor: Cursor? = requireActivity().contentResolver?.query(
            uri,
            arrayOf(
                "_id",
                "word",
                "usphone",
                "ukphone",
                "audiourl",
                "examType",
                "meanings",
                "changes",
                "sentences"
            ),
            "word=?",
            arrayOf("$word"),
            null
        )
        singleCursor!!.moveToFirst()

        val word: String? = singleCursor!!.getString(1)
        val usphone: String? = singleCursor!!.getString(2)
        val ukphone: String? = singleCursor!!.getString(3)
        val audiourl: String? = singleCursor!!.getString(4)
        val examType: String? = singleCursor!!.getString(5)

        val meanings: ArrayList<String> = ArrayList()
        if (singleCursor!!.getString(6)==""){
            Log.d("@=>Translator","No meaning")
        }else{
            meanings.add(singleCursor!!.getString(6))
        }

        val changes: ArrayList<String> = ArrayList()
        if (singleCursor!!.getString(7)==""){
            Log.d("@=>Translator","No changes")
        }else{
            changes.add(singleCursor!!.getString(7))
        }

        val sentences: ArrayList<String> = ArrayList()
        if (singleCursor!!.getString(8)==""){
            Log.d("@=>Translator","No sentences")
        }else{
            sentences.add(singleCursor!!.getString(8))
        }


        val wordInfo = WordDetail(
            word, usphone, ukphone, audiourl, examType,
            meanings, changes, sentences
        )
        val intent = Intent(activity, WordDetailActivity::class.java)
        intent.putExtra("wordInfo", wordInfo as Serializable)
        startActivity(intent)
    }
}

interface CellClickListener {
    fun onCellClickListener(word: String)
}