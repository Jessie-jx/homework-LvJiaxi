package jessie.cs175.hw4_translator.adapter

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jessie.cs175.hw4_translator.R
import jessie.cs175.hw4_translator.fragments.CellClickListener
import jessie.cs175.hw4_translator.fragments.CellClickListener2

class HistoryRecyclerviewAdapter(var mCursor: Cursor?,private val cellClickListener2: CellClickListener2) :
    RecyclerView.Adapter<HistoryRecyclerviewAdapter.ViewHolder>(){
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val wordView: TextView = ItemView.findViewById(R.id.history_word)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        android.R.layout.simple_list_item_1
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (!mCursor!!.moveToPosition(position) || mCursor == null) {
            return
        }
        val wordText: String? = mCursor!!.getString(1)

        holder.wordView.text = wordText


        holder.itemView.setOnClickListener {
            if (wordText != null) {
                cellClickListener2.onCellClickListener(wordText)
            }
        }

    }

    override fun getItemCount(): Int {
        if (mCursor != null) {
            return mCursor!!.count
        } else {
            return 0
        }

    }

    fun swapCursor(newCursor: Cursor?) {
        if (mCursor != null) {
            mCursor!!.close()
        }
        mCursor = newCursor
        if (newCursor != null) {
            notifyDataSetChanged()
        }

    }




}