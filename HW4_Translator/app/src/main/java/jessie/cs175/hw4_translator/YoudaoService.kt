package jessie.cs175.hw4_translator

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YoudaoService {
    @GET("suggest")
    fun getWordMeaning(
        @Query("q") q: String,
        @Query("num") num: Int,
        @Query("doctype") doctype: String
    ): Call<YoudaoBean>
}
