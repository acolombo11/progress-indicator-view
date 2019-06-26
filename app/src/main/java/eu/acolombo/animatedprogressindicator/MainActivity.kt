package eu.acolombo.animatedprogressindicator

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.rd.animation.type.AnimationType
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pageIndicatorView.setAnimationType(AnimationType.THIN_WORM)

        pageIndicatorView.count = 5

        buttonPlus.setOnClickListener {
            pageIndicatorView.indicator += 1
        }

        buttonMinus.setOnClickListener {
            pageIndicatorView.indicator -= 1
        }

        val mHandler = Handler()
        var progressPercentage = 0
        var currentStep = 1

        Thread(Runnable {
            while (progressPercentage < 100) {
                // sleeping for 20milliseconds
                try {
                    Thread.sleep(400)
                    progressPercentage++
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                mHandler.post {
                    if (progressPercentage > (pageIndicatorView.max / pageIndicatorView.count - 1) * currentStep) {
                        currentStep++
                        pageIndicatorView.indicator++
                    }
                    textProgress.text = "$progressPercentage%"
                }
            }
        }).start()

    }

}
