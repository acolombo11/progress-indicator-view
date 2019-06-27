package eu.acolombo.progressindicatorview

import android.content.Context
import android.content.res.TypedArray
import android.os.Handler
import android.util.AttributeSet
import com.rd.PageIndicatorView


class ProgressIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : PageIndicatorView(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "ProgressIndicatorView"
    }

    enum class State { STOP, NEXT, PREV }

    private val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressIndicatorView)
    var progress = typedArray.getInt(R.styleable.ProgressIndicatorView_piv_progress, 0)
    var min = typedArray.getInt(R.styleable.ProgressIndicatorView_piv_min, 0)
    var max = typedArray.getInt(R.styleable.ProgressIndicatorView_piv_max, 0)

    private val stateMachineHandler = Handler()
    private var stateMachine: Runnable = object : Runnable {

        private var step = stepProgress.also { selection = it - 1 }
        private var state = State.STOP
        private var prevState = State.NEXT

        override fun run() {

            val unit = max / (count - 1)
            val next = unit * step
            val prev = unit * (step - 1)

            when (state) {
                State.STOP -> {
                    when {
                        step > stepProgress -> {
                            decreaseStep()
                            state = State.PREV
                        }
                        step < stepProgress -> {
                            increaseStep()
                            state = State.NEXT
                        }
                        progress > min && progress < max -> {
                            state = prevState
                        }
                        selection != stepProgress - 1 -> {
                            setSelected(stepProgress - 1)
                        }
                    }
                }
                State.NEXT -> {
                    prevState = state
                    if ((selection + 1) <= stepProgress) selection++
                    state = if (progress < next) {
                        State.PREV
                    } else {
                        increaseStep()
                        State.STOP
                    }
                }
                State.PREV -> {
                    prevState = state
                    if ((selection - 1) >= stepProgress - 1) selection--
                    state = if (progress > prev) {
                        State.NEXT
                    } else {
                        decreaseStep()
                        State.STOP
                    }
                }
            }

            stateMachineHandler.postDelayed(this, if (state == State.STOP) 0 else animationDuration)

        }

        val stepProgress: Int
            get() = ((progress / max.toFloat()) * (count - 1)).toInt() + 1

        private fun increaseStep() {
            if (step < count - 1) step++
        }

        private fun decreaseStep() {
            if (step > 1) step--
        }

    }

    init {
        typedArray.recycle()
        stateMachine.run()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stateMachineHandler.removeCallbacks(stateMachine)
    }

}