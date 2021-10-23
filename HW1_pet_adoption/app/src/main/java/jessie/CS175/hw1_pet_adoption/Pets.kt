package jessie.CS175.hw1_pet_adoption


import android.content.res.Resources

/* Returns initial list of flowers. */
fun petList(): List<Pet> {
    return listOf(
        Pet(
            id = 1,
            image = R.drawable.shibainu_1,
            name = "Ada",
            gender = 0,
            age = 1,
            breed = "Shiba Inu"
        ),
        Pet(
            id = 2,
            image = R.drawable.shibainu_2,
            name = "Bob",
            gender = 1,
            age = 6,
            breed = "Shiba Inu"
        ),
        Pet(
            id = 3,
            image = R.drawable.bichon_1,
            name = "Catherine",
            gender = 0,
            age = 4,
            breed = "Bichon"
        ),
        Pet(
            id = 4,
            image = R.drawable.cocker_1,
            name = "Davis",
            gender = 1,
            age = 3,
            breed = "Cocker"
        ),
        Pet(
            id = 5,
            image = R.drawable.cocker_2,
            name = "Elsa",
            gender = 0,
            age = 3,
            breed = "Cocker"
        ),
        Pet(
            id = 6,
            image = R.drawable.corgi_1,
            name = "Floyd",
            gender = 1,
            age = 2,
            breed = "Corgi"
        ),
        Pet(
            id = 7,
            image = R.drawable.corgi_2,
            name = "George",
            gender = 1,
            age = 2,
            breed = "Corgi"
        ),
        Pet(
            id = 8,
            image = R.drawable.golden_1,
            name = "Helen",
            gender = 0,
            age = 5,
            breed = "Golden Retriever"
        ),
        Pet(
            id = 9,
            image = R.drawable.golden_2,
            name = "Ivy",
            gender = 0,
            age = 6,
            breed = "Golden Retriever"
        ),
        Pet(
            id = 10,
            image = R.drawable.labrador_1,
            name = "Jane",
            gender = 0,
            age = 7,
            breed = "Labrador"
        ),
        Pet(
            id = 11,
            image = R.drawable.labrador_2,
            name = "Kate",
            gender = 0,
            age = 1,
            breed = "Labrador"
        ),
        Pet(
            id = 12,
            image = R.drawable.schnauzer,
            name = "Leo",
            gender = 1,
            age = 1,
            breed = "Schnauzer"
        ),
        Pet(
            id = 13,
            image = R.drawable.teddy_1,
            name = "Mario",
            gender = 1,
            age = 4,
            breed = "Teddy"
        )
    )
}




