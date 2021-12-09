package jessie.cs175.hw6_mediabasic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide

class ImageActivity : AppCompatActivity() {

    private val pages: MutableList<View> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        addImage("https://qnssl.niaogebiji.com/113625330360486a4da7cd39.93207103.gif")
        addImage("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fgss0.baidu.com%2F9vo3dSag_xI4khGko9WTAnF6hhy%2Fzhidao%2Fpic%2Fitem%2F9a504fc2d5628535d450b47893ef76c6a7ef6332.jpg")
        addImage("https://gimg2.baidu.com/image_search/src=http%3A%2F%2F09.imgmini.eastday.com%2Fmobile%2F20180623%2F20180623121641_1a0a25ff7eb8ac60c573496fc1ea471f_1.jpeg")
        addImage("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fs2.best-wallpaper.net%2Fwallpaper%2F2880x1800%2F1908%2FToy-Story-4-back-view_2880x1800.jpg")
        addImage("https://p5.itc.cn/q_70/images03/20210127/8d0466bcf2df46aab7313c7b48234746.jpeg")

        val adapter = ViewAdapter()
        adapter.setDatas(pages)

        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        viewPager.adapter = adapter
    }

    private fun addImage(path: String) {
        val imageView = layoutInflater.inflate(R.layout.image_item, null) as ImageView
        Glide.with(this).load(path).error(R.drawable.error).into(imageView)
        pages.add(imageView)
    }
}