package eu.acolombo.progressindicatorview

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import com.rd.PageIndicatorView


class ProgressIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : PageIndicatorView(context, attrs, defStyleAttr) {

    enum class State { STOP, NEXT, PREV }

    var progress = 0
    var min: Int = 0
    var max: Int = 100
    var state = State.STOP
    var step = 1

    private var prevState: State = State.NEXT

    private val stateMachineHandler = Handler()
    private var stateMachine: Runnable = object : Runnable {

        override fun run() {

            val unit = (max) / (count - 1)
            val next = unit * step
            val prev = unit * (step - 1)
            val realStep = ((progress / max.toFloat()) * (count - 1)).toInt() + 1

            when (state) {
                State.STOP -> {
                    when {
                        step > realStep -> {
                            decreaseStep()
                            state = State.PREV
                        }
                        step < realStep -> {
                            increaseStep()
                            state = State.NEXT
                        }
                        progress > min && progress < max -> {
                            state = prevState
                        }
                    }
                }
                State.NEXT -> {
                    selection++
                    state = if (progress < next) {
                        State.PREV
                    } else {
                        increaseStep()
                        prevState = state
                        State.STOP
                    }
                }
                State.PREV -> {
                    selection--
                    state = if (progress > prev) {
                        State.NEXT
                    } else {
                        decreaseStep()
                        prevState = state
                        State.STOP
                    }
                }
            }

            stateMachineHandler.postDelayed(this, if (state == State.STOP) 0 else animationDuration)
        }

    }

    init {
        stateMachine.run()
    }

    private fun increaseStep() {
        if (step < count - 1) step++
    }

    private fun decreaseStep() {
        if (step > 1) step--
    }

}