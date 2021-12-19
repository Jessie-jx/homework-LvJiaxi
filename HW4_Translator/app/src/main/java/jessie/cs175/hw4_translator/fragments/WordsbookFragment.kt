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

import android.content.ContentValues
import android.content.Intent
import android.util.Log
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import jessie.cs175.hw4_translator.model.WordDetail
import jessie.cs175.hw4_translator.activities.WordDetailActivity
import jessie.cs175.hw4_translator.adapter.SwipeToDeleteCallback
import java.io.Serializable
import java.util.ArrayList


class WordsbookFragment : Fragment(), CellClickListener {
    var uri: Uri = Uri.parse("content://jessie.cs175.hw4_translator.db.provider/WordBook")
    private lateinit var adapter: MyRecyclerviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_wordsbook, container, false)
        val showMeaningBtn = view.findViewById<SwitchCompat>(R.id.show_switch)

        adapter = MyRecyclerviewAdapter(getAllItems(), this)
        val wordbookRecyclerview = view.findViewById<RecyclerView>(R.id.word_book_list)


        wordbookRecyclerview.layoutManager = LinearLayoutManager(activity as Context)
        wordbookRecyclerview.adapter = adapter

        val swipeHandler = object : SwipeToDeleteCallback(activity as Context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val adapter = recyclerView.adapter as SimpleAdapter
                val deletedWord = adapter.removeAt(viewHolder.adapterPosition)

                val cv = ContentValues()
                cv.put("deleted", 1)
                requireActivity().contentResolver.update(uri, cv, "word = ?", arrayOf(deletedWord))

                adapter.swapCursor(getAllItems())
                if (deletedWord != null) {
                    Snackbar.make(view, deletedWord, Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.undo), object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                val cv = ContentValues()
                                cv.put("deleted", 0)
                                requireActivity().contentResolver.update(
                                    uri,
                                    cv,
                                    "word = ?",
                                    arrayOf(deletedWord)
                                )
                                adapter.swapCursor(getAllItems())
                            }

                        }).show()
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(wordbookRecyclerview)

        showMeaningBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                adapter.isMeaningHidden = false
                adapter.notifyDataSetChanged()
            } else {
                adapter.isMeaningHidden = true
                adapter.notifyDataSetChanged()
            }
        }


        return view
    }


    override fun onResume() {
        super.onResume()
        adapter.swapCursor(getAllItems())
    }


    private fun getAllItems(): Cursor? {
        return requireActivity().contentResolver.query(
            uri,
            arrayOf("_id", "word", "usphone", "meanings"),
            "star = ? AND deleted = ?",
            arrayOf("1","0"),
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
        if (singleCursor!!.getString(6) == "") {
            Log.d("@=>Translator", "No meaning")
        } else {
            meanings.add(singleCursor!!.getString(6))
        }

        val changes: ArrayList<String> = ArrayList()
        if (singleCursor!!.getString(7) == "") {
            Log.d("@=>Translator", "No changes")
        } else {
            changes.add(singleCursor!!.getString(7))
        }

        val sentences: ArrayList<String> = ArrayList()
        if (singleCursor!!.getString(8) == "") {
            Log.d("@=>Translator", "No sentences")
        } else {
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