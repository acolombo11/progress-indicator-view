package eu.acolombo.progressindicatorview.example

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragmnent_hacker.*
import kotlin.random.Random

@SuppressLint("SetTextI18n")
abstract class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProgress(0)
        setupDemoProgressChangers()

        // Other ui configuration

        textMax?.text = "${progressIndicatorView.max}%"
        textMin?.text = "${progressIndicatorView.min}%"
    }

    private fun setupProgress(progress: Int) {
        progressIndicatorView?.progress = progress
        textProgress?.text = "$progress%"
    }

    private fun setupDemoProgressChangers() {
        var threadSpeed = 300L
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

                mHandler.post { setupProgress(p) }
            }
        })

        textProgress?.setOnClickListener {
            if (!running) {
                seekBarSpeed?.visibility = View.VISIBLE
                textSeekBarTitle?.visibility = View.VISIBLE
                running = true
                thread.start()
            } else {
                seekBarSpeed?.visibility = View.GONE
                textSeekBarTitle?.visibility = View.GONE
                running = false
                textProgress?.setOnClickListener { } // atm I don't want to manage stopping and resetting
            }
        }

        val animator = ValueAnimator()
        val random = Random(69)
        val randomUnit = 60

        fun ValueAnimator.start(progress: Int, previous: Int, func: (progress: Int) -> Unit) = apply {
            setObjectValues(previous, progress)
            addUpdateListener { func(it.animatedValue as Int) }
            duration = (maxOf(progress, previous) - minOf(progress, previous)) * 100L
            start()
        }

        fun setupProgressWithAnimation(progress: Int, previous: Int) {
            animator.start(progress, previous) { setupProgress(it) }
        }

        fun increaseProgressRandom(previous: Int, max: Int, random: Random, randomUnit: Int) {
            setupProgressWithAnimation(minOf(previous + random.nextInt(randomUnit), max), previous)
        }

        fun decreaseProgressRandom(previous: Int, min: Int, random: Random, randomUnit: Int) {
            setupProgressWithAnimation(maxOf(previous - random.nextInt(randomUnit), min), previous)
        }

        buttonMinus?.setOnClickListener {
            if (!animator.isRunning) decreaseProgressRandom(
                progressIndicatorView.progress,
                progressIndicatorView.min,
                random,
                randomUnit
            )
        }

        buttonPlus?.setOnClickListener {
            if (!animator.isRunning) increaseProgressRandom(
                progressIndicatorView.progress,
                progressIndicatorView.max,
                random,
                randomUnit
            )
        }

        seekBarSpeed?.max = threadSpeed.toInt() * 2
        seekBarSpeed?.progress = threadSpeed.toInt()
        seekBarSpeed?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                threadSpeed = seekBarSpeed.max - progress.toLong()
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