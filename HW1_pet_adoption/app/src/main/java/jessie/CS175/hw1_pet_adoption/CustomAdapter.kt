package jessie.CS175.hw1_pet_adoption

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*

const val MALE:String="Male"
const val FEMALE:String="Female"

class CustomAdapter(private val cellClickListener: CellClickListener)
    : Adapter<CustomAdapter.ViewHolder>() {

//    private val mItems = arrayListOf<String>()
    private val mList= arrayListOf<Pet>()

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) :
        RecyclerView.ViewHolder(ItemView) {
        val hp_image: ImageView = itemView.findViewById(R.id.hp_image)
        val hp_name: TextView = itemView.findViewById(R.id.hp_name)
        val hp_gender:TextView=itemView.findViewById(R.id.hp_gender)
        val hp_breed:TextView=itemView.findViewById(R.id.hp_breed)

    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Pet_Item = mList[position]
        // sets the image to the imageview from our itemHolder class
        holder.hp_image.setImageResource(Pet_Item.image)
        // sets the text to the textview from our itemHolder class
        holder.hp_name.text = Pet_Item.name
        Pet_Item.gender?.let {
            if (Pet_Item.gender==1) {
                holder.hp_gender.text=MALE
            } else {
                holder.hp_gender.text=FEMALE
            }
        }

        holder.hp_breed.text=Pet_Item.breed

        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(Pet_Item)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
    fun getData(): List<Pet>{
        return mList
    }

    fun notifyItems(items: List<Pet>) {
        mList.clear()
        mList.addAll(items)
        notifyDataSetChanged()
    }
}