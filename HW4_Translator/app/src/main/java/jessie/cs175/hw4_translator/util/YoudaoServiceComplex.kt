package jessie.cs175.hw4_translator.util

import cn.json.dict.JsonRootBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YoudaoServiceComplex {
    @GET("jsonapi")
    fun getWordInfo(@Query("q") q: String): Call<JsonRootBean>
}