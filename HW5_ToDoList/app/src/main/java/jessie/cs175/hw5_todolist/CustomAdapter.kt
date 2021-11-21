package jessie.cs175.hw5_todolist

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



class CustomAdapter(var mCursor: Cursor?,private val Listener:CellClickListener) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
//    var mCursor: Cursor? = null


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val taskView: TextView = ItemView.findViewById(R.id.task_text)
        val timeView: TextView = ItemView.findViewById(R.id.time_text)
        val checkBox:CheckBox=ItemView.findViewById(R.id.check_box)
        val delBtn:Button=ItemView.findViewById(R.id.del_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (!mCursor!!.moveToPosition(position) || mCursor == null) {
            return
        }
        val taskText: String? = mCursor!!.getString(1)
        val timeText: String? = mCursor!!.getString(2)
        holder.taskView.text = taskText
        holder.timeView.text = timeText


        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            buttonView.isSelected=buttonView.isChecked
        }

        holder.delBtn.setOnClickListener {
            mCursor!!.moveToPosition(position)
            Listener.onCellClickListener(mCursor!!.getInt(0))
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