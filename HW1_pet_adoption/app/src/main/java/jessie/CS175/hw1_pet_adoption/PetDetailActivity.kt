package jessie.CS175.hw1_pet_adoption

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import org.w3c.dom.Text

class PetDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_detail)

        var currentPetId: Long? = null

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            currentPetId = bundle.getLong(PET_ID)
        }
        val pet_item_image: ImageView = findViewById(R.id.pet_item_image)
        val pet_item_name: TextView = findViewById(R.id.pet_item_name)
        val pet_item_des:TextView=findViewById(R.id.pet_item_des)

        val petList = petList()
        currentPetId?.let {
            val currentPet: Pet=petList[(currentPetId-1).toInt()]
            pet_item_image.setImageResource(currentPet.image)
            pet_item_name.text=currentPet.name
            pet_item_des.text=generate_des(currentPet.name,currentPet.gender,currentPet.age,currentPet.breed)

        }


    }
    private fun generate_des(name: String, gender: Int, age: Long, breed: String): String {
        val gender_str: String

        if (gender == 1) {
            gender_str = MALE
        } else {
            gender_str = FEMALE
        }

        return "Hi! Nice to meet you. My name is $name. I am a $gender_str $breed and I am $age years old. I am very friendly and lovely. I wish I could become your friend and bring happiness to you :)"
    }
}