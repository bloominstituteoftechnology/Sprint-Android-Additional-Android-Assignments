package com.example.mediaplayer

import android.graphics.drawable.Animatable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import java.util.concurrent.TimeUnit
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.os.postDelayed
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val handler = Handler()

    private val handlerRunnable = object: Runnable{
        override fun run() {
            while(true) {
                runOnUiThread {
                    if (video_view.isPlaying) {
                        Toast.makeText(this@MainActivity, "HIMAN", Toast.LENGTH_SHORT).show()
                    } else {
                        video_view.start()
                    }
                    //handler.postDelayed(this, 5000)
                    Thread.sleep(5000)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        video_view.isEnabled = false
        setVideoSource()
        //at this point the video has been prepared when the video uri is set
        video_view.setOnPreparedListener { mp->
            video_view.isEnabled = true
            mp?.let{
                video_seekbar.max = 100
                handlerRunnable.run()
            }
        }
        //seekBarFunctionality()
    }

    //good for displaying animations
    override fun onStart() {
        btn_pause_or_play.setOnClickListener {
            playAndPauseFunctionality()
        }
        super.onStart()
    }

    private fun setVideoSource(){
        video_view.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.live_views_of_starman))
    }

    private fun playAndPauseFunctionality(){
        if(video_view.isPlaying){
            //pause it
            video_view.pause()
            btn_pause_or_play.setImageDrawable(getDrawable(R.drawable.avd_anim_pause_play))
            //handler.removeCallbacks(handlerRunnable)
        } else{
            video_view.start()
            btn_pause_or_play.setImageDrawable(getDrawable(R.drawable.avd_anim_play_pause))
            //handler.post(handlerRunnable)
        }
        (btn_pause_or_play.drawable as Animatable).start()
    }

    private fun seekBarFunctionality(){

        video_seekbar.setOnSeekBarChangeListener(
            object: SeekBar.OnSeekBarChangeListener{

                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    p0?.let{
                        video_view.seekTo(p1)
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}

                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })
    }

    override fun onStop() {
        video_view.pause()
        super.onStop()
    }
}
