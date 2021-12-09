package jessie.cs175.hw6_mediabasic


import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_video.*
import java.io.IOException

class VideoActivity : AppCompatActivity() {
    private val player: MediaPlayer by lazy {
        MediaPlayer()
    }
    private var holder: SurfaceHolder? = null
//    private var mSeekbar: SeekBar? = null


    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "MediaPlayer"

//        mSeekbar = findViewById<SeekBar>(R.id.mySeekbar)

        setContentView(R.layout.activity_video)
        try {
            player.setDataSource(resources.openRawResourceFd(R.raw.big_buck_bunny))
            holder = surfaceView.holder
            holder?.setFormat(PixelFormat.TRANSPARENT)
            holder?.addCallback(PlayerCallBack())
            player.prepare()
            player.setOnPreparedListener { // 自动播放
                initializeSeekBar()
                player.start()
                player.isLooping = true
            }
            player.setOnBufferingUpdateListener { mp, percent -> println(percent) }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        buttonPlay.setOnClickListener {
            player.start()

        }
        buttonPause.setOnClickListener { player.pause() }
        buttonReplay.setOnClickListener {
            player.seekTo(0)
            player.start()
        }

        mySeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    player.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }

    private fun initializeSeekBar() {
        mySeekbar.max = player.duration
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    mySeekbar.progress = player.currentPosition
                    handler.postDelayed(this, 1000)

                } catch (e: IOException) {
                    mySeekbar.progress = 0
                }
            }

        }, 0)

    }


    override fun onPause() {
        super.onPause()
        player.stop()
        player.release()
    }

    private inner class PlayerCallBack : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            player.setDisplay(holder)
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
        override fun surfaceDestroyed(holder: SurfaceHolder) {}
    }
}
