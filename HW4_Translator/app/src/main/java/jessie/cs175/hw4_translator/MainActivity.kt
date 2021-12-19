package jessie.cs175.hw4_translator

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import cn.json.dict.JsonRootBean
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import android.net.http.HttpResponseCache
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import jessie.cs175.hw4_translator.adapter.ViewPagerAdapter
import jessie.cs175.hw4_translator.db.AppPreferences
import jessie.cs175.hw4_translator.fragments.MyFragment
import jessie.cs175.hw4_translator.fragments.RootFragment
import jessie.cs175.hw4_translator.fragments.SearchFragment
import jessie.cs175.hw4_translator.fragments.WordsbookFragment
import okhttp3.*
import okhttp3.EventListener
import java.io.File
import okhttp3.CacheControl
import java.io.IOException
import java.util.concurrent.TimeUnit


const val HEADER_CACHE_CONTROL = "Cache-Control"
const val HEADER_PRAGMA = "Pragma"
const val HISTORY_WORDS_LIMIT: Int = 10

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppPreferences.init(this)

        val viewPager = findViewById<ViewPager>(R.id.viewPager)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        setupViewPager(viewPager)
        tabLayout.setupWithViewPager(viewPager)

    }

    private fun setupViewPager(viewpager: ViewPager) {
        var adapter: ViewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        // LoginFragment is the name of Fragment and the Login
        // is a title of tab
        adapter.addFragment(SearchFragment(), "查词")
        adapter.addFragment(WordsbookFragment(), "生词本")
        adapter.addFragment(RootFragment(), "我的")

        // setting adapter to view pager.
        viewpager.setAdapter(adapter)
    }


}

