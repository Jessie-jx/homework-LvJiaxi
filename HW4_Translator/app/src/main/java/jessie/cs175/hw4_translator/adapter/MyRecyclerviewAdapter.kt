package jessie.cs175.hw4_translator.adapter

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jessie.cs175.hw4_translator.R
import jessie.cs175.hw4_translator.fragments.CellClickListener

class MyRecyclerviewAdapter(var mCursor: Cursor?,private val cellClickListener: CellClickListener) :
    RecyclerView.Adapter<MyRecyclerviewAdapter.ViewHolder>(){
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val wordView: TextView = ItemView.findViewById(R.id.wordbook_word)
        val phoneView: TextView = ItemView.findViewById(R.id.wordbook_phone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wordbook_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (!mCursor!!.moveToPosition(position) || mCursor == null) {
            return
        }
        val wordText: String? = mCursor!!.getString(1)
        val phoneText: String? = mCursor!!.getString(2)

        holder.wordView.text = wordText
        holder.phoneView.text = phoneText

        holder.itemView.setOnClickListener {
            if (wordText != null) {
                cellClickListener.onCellClickListener(wordText)
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