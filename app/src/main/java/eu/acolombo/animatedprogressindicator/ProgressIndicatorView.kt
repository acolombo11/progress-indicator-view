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

    var progress = 0
    var min: Int = 0
    var max: Int = 100
    var state = State.STOP
    var step = 1

    enum class State { STOP, NEXT, PREV }

    private val stateMachineHandler = Handler()
    private var prevState: State = State.NEXT
    private var stateMachine: Runnable = object : Runnable {

        override fun run() {

            val unit = (max) / (count - 1)

            val next = unit * step
            val prev = unit * (step - 1)

            val realStep = ((progress / max.toFloat()) * (count - 1)).toInt()

            when (state) {
                State.STOP -> {
                    if (progress > min && progress < max) {
                        state = prevState
                    }
                }
                State.NEXT -> {
                    selection++
                    state = if (progress < next) {
                        State.PREV
                    } else {
                        if (step < count - 1) step++
                        prevState = state
                        State.STOP
                    }
                }
                State.PREV -> {
                    selection--
                    state = if (progress > prev) {
                        State.NEXT
                    } else {
                        if (step > 1) step--
                        prevState = state
                        State.STOP
                    }
                }
            }

            stateMachineHandler.postDelayed(this, animationDuration)

        }

    }

    init {
        stateMachine.run()
    }

}