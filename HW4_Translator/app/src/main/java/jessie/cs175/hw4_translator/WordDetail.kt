package jessie.cs175.hw4_translator

import cn.json.dict.TransList
import java.io.Serializable

data class WordDetail(
    val word: String,
    val usphone: String,
    val ukphone: String,
    val audiourl:String,
    val examType:String,
    val meanings: String,
    val changes:String,
    val sentences: String

) : Serializable

