package eu.acolombo.animatedprogressindicator

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import com.rd.PageIndicatorView


class ProgressIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : PageIndicatorView(context, attrs, defStyleAttr) {

    var min: Int = 0
    var max: Int = 100

    var mHandler = Handler()

    init {
        selection = 0

        object : Runnable {
            override fun run() {
                // Run the passed runnable
                animation()
                // Re-run it after the update interval
                mHandler.postDelayed(this, animationDuration)
            }
        }.run()
    }


    var indicator = 0
    var currentStep = 0
    private var direction = 1
    private var lastIndicator = 0
    private var idling: Boolean = true

    private fun animation() {
        direction = when {
            direction > 0 -> -1
            direction < 0 -> 1
            else -> 0
        }

        if (idling) {
            when {
                direction > 0 -> selection++
                direction < 0 -> selection--
            }
        }

        idling = indicator == lastIndicator

        if (!idling && indicator > lastIndicator ) {

            selection++

            lastIndicator = indicator


        } else if (!idling && indicator < lastIndicator && direction < 0) {

            selection--

            lastIndicator = indicator


        }
    }


}