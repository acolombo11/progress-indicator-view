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

        // Initial view configuration

        progressIndicatorView.setAnimationType(AnimationType.THIN_WORM)
        progressIndicatorView.count = 5
        progressIndicatorView.min = 0
        progressIndicatorView.max = 100

        fun setProgress(progress: Int) {
            val percent = "$progress%"
            textProgress.text = percent
            progressIndicatorView.progress = progress
        }

        var threadSpeed = 400L
        var running = false
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

        val animator = ValueAnimator()
        val random = Random(69)
        val randomUnit = 60

        fun setProgressWithAnimation(progress: Int, previous: Int) {
            animator.start(progress, previous) { setProgress(it) }
        }

        fun increaseProgressRandom(previous: Int, max: Int, random: Random, randomUnit: Int){
            setProgressWithAnimation(minOf(previous + random.nextInt(randomUnit), max), previous)
        }

        fun decreaseProgressRandom(previous: Int, min: Int, random: Random, randomUnit: Int){
            setProgressWithAnimation(maxOf(previous - random.nextInt(randomUnit), min), previous)
        }

        buttonMinus.setOnClickListener {
            if (!animator.isRunning) decreaseProgressRandom(progressIndicatorView.progress, progressIndicatorView.min, random, randomUnit)
        }

        buttonPlus.setOnClickListener {
            if (!animator.isRunning) increaseProgressRandom(progressIndicatorView.progress, progressIndicatorView.max, random, randomUnit)
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

    private fun ValueAnimator.start(progress: Int, previous: Int, func: (progress: Int) -> Unit) = apply {
        setObjectValues(previous, progress)
        addUpdateListener { func(it.animatedValue as @kotlin.ParameterName(name = "progress") Int) }
        duration = (maxOf(progress, previous) - minOf(progress, previous)) * 100L
        start()
    }
}
