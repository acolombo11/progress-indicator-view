package eu.acolombo.animatedprogressindicator

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.rd.animation.type.AnimationType
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressIndicatorView.setAnimationType(AnimationType.THIN_WORM)
        progressIndicatorView.count = 5
        progressIndicatorView.min = 0
        progressIndicatorView.max = 100

        fun setProgress(progress: Int) {
            val prev = progressIndicatorView.progress

            ValueAnimator().apply {
                setObjectValues(prev, progress)
                addUpdateListener {
                    val percent = "${it.animatedValue}%"
                    textProgress.text = percent
                }
                duration = (maxOf(progress, prev) - minOf(progress, prev)) * 100L
            }.start()


            progressIndicatorView.progress = progress
        }

        var threadSpeed = 400L
        var running = false
        val random = Random(69)
        val mHandler = Handler()
        val thread = Thread(Runnable {

            var p = 0

            while (p < progressIndicatorView.max) {

                try {
                    Thread.sleep(threadSpeed)
                    p++
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                mHandler.post {
                    setProgress(p)
                }
            }
        })

        textProgress.setOnClickListener {
            if (!running) {
                seekBar.visibility = View.VISIBLE
                running = true
                thread.start()
            } else {
                seekBar.visibility = View.GONE
                running = false
                textProgress.setOnClickListener { } // atm I don't want to manage stopping and resetting
            }
        }

        buttonMinus.setOnClickListener {
            setProgress(maxOf(progressIndicatorView.progress - random.nextInt(16), 0))
        }

        buttonPlus.setOnClickListener {
            setProgress(minOf(progressIndicatorView.progress + random.nextInt(20), 100))
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                threadSpeed = progress.toLong()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // not implemented
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // not implemented
            }
        })

    }
}
