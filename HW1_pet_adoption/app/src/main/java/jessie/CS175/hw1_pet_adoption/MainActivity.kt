package jessie.CS175.hw1_pet_adoption

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

const val PET_ID="pet id"

class MainActivity : AppCompatActivity(), CellClickListener, AdapterView.OnItemSelectedListener {

    var spinner_list_gender = arrayOf("All gender", "Female", "Male")
    var spinner_list_breed = arrayOf("All breeds", "Shiba Inu", "Bichon", "Cocker",
        "Corgi", "Golden Retriever", "Labrador", "Schnauzer", "Teddy")
    var spinner_gender_state:Int = 0
    var spinner_breed_state:Int = 0
    val data = arrayListOf<Pet>()
    val petList = petList()
    val adapter = CustomAdapter(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))


        // RECYCLERVIEW
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)
        for (i in 0..petList.count() - 1) {
            data.add(Pet(petList[i].id, petList[i].image, petList[i].name,petList[i].gender,petList[i].age,petList[i].breed))
        }
        adapter.notifyItems(data)


        // SPINNER
        //spinner 1
        val spinner_gender=findViewById<Spinner>(R.id.spinner_gender)
        spinner_gender!!.setOnItemSelectedListener(this)
        val array_adapter_gender = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinner_list_gender)
        array_adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_gender!!.setAdapter(array_adapter_gender)

        //spinner 2
        val spinner_breed=findViewById<Spinner>(R.id.spinner_breed)
        spinner_breed!!.setOnItemSelectedListener(this)
        val array_adapter_breed = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinner_list_breed)
        array_adapter_breed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_breed!!.setAdapter(array_adapter_breed)
    }


    override fun onCellClickListener(pet: Pet) {
        val intent = Intent(this, PetDetailActivity::class.java)
        intent.putExtra(PET_ID, pet.id)
        startActivity(intent)
//        Toast.makeText(this,"Cell clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent!!.id) {
            R.id.spinner_gender -> {
                spinner_gender_state=position
            }
            R.id.spinner_breed -> {
                spinner_breed_state=position
            }
            else -> {

            }
        }
        if (spinner_gender_state==0 && spinner_breed_state==0) {
            adapter.notifyItems(data)
        } else if (spinner_gender_state==0 && spinner_breed_state!=0) {
            val selected_data=petList.filter {it.breed==spinner_list_breed[spinner_breed_state]}
            adapter.notifyItems(selected_data)
        } else if (spinner_gender_state!=0 && spinner_breed_state==0) {
            val selected_data=petList.filter {it.gender==position-1}
            adapter.notifyItems(selected_data)
        } else {
            val selected_data=petList.filter { it.gender==position-1 && it.breed==spinner_list_breed[spinner_breed_state]}
            adapter.notifyItems(selected_data)
        }

        Log.d("@=>", "Spinner: $parent.getId()")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}

interface CellClickListener {
    fun onCellClickListener(pet: Pet)
}