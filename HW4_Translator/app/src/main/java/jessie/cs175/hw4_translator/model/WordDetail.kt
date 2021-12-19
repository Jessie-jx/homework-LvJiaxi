package jessie.cs175.hw4_translator.model

import cn.json.dict.TransList
import java.io.Serializable
import java.util.ArrayList

data class WordDetail(
    val word: String?,
    val usphone: String?,
    val ukphone: String?,
    val audiourl:String?,
    val examType:String?,
    val meanings: ArrayList<String>,
    val changes: ArrayList<String>,
    val sentences: ArrayList<String>

) : Serializable

