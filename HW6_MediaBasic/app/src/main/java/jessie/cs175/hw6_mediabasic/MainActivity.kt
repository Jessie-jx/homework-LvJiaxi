package jessie.cs175.hw6_mediabasic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initButton()
    }

    private fun initButton() {
        open(R.id.image, ImageActivity::class.java)
        open(R.id.video, VideoActivity::class.java)
    }

    private fun open(buttonId: Int, clz: Class<*>) {
        findViewById<View>(buttonId).setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    clz
                )
            )
        }
    }
}