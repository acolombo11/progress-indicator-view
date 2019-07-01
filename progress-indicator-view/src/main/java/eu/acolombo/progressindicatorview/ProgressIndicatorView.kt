package eu.acolombo.progressindicatorview

import android.content.Context
import android.content.res.TypedArray
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import com.rd.PageIndicatorView


class ProgressIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : PageIndicatorView(context, attrs, defStyleAttr) {

    private val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressIndicatorView)
    var progress = typedArray.getInt(R.styleable.ProgressIndicatorView_piv_progress, 0)
    var min = typedArray.getInt(R.styleable.ProgressIndicatorView_piv_min, 0)
    var max = typedArray.getInt(R.styleable.ProgressIndicatorView_piv_max, 0)

    var stopOnStep: Boolean = typedArray.getBoolean(R.styleable.ProgressIndicatorView_piv_stopOnStep, false)
    var stepToMin: Boolean = typedArray.getBoolean(R.styleable.ProgressIndicatorView_piv_stepToMin, false)
    var skipSteps: Boolean = typedArray.getBoolean(R.styleable.ProgressIndicatorView_piv_skipSteps, false)
    var balanceForward: Boolean = typedArray.getBoolean(R.styleable.ProgressIndicatorView_piv_balanceForward, true)

    var onStepChanged: (step: Int, forward: Boolean) -> Unit = { _, _ -> }
    var onMinReached: () -> Unit = { }
    var onMaxReached: () -> Unit = { }

    fun setProgress(progress: Int, animate: Boolean) {
        this.progress = progress
        if (!animate) setSelected(stepProgress)
    }

    private val stepProgress: Int
        get() = ((progress / max.toFloat()) * (count - 1)).toInt() + 1

    private enum class State { STOP, FORWARD, BACKWARD }

    private val stateMachineHandler = Handler()
    private var stateMachine: Runnable = object : Runnable {

        private var step = stepProgress.also { selection = it - 1 }
        private var state = State.STOP
        private var statePrev = when {
            progress > max / 2 -> State.FORWARD
            progress < max / 2 -> State.BACKWARD
            else -> if (balanceForward) State.FORWARD else State.BACKWARD
        }

        override fun run() {

            val unit = max / (count - 1).toFloat()
            val next = unit * step
            val prev = unit * (step - 1)

            val stopStep = if (stopOnStep) progress % unit.toInt() == 0 else false

            when (state) {
                State.STOP -> {
                    when {
                        selection > stepProgress - 1 && !skipSteps -> {
                            state = State.BACKWARD
                        }
                        selection < stepProgress - 1 && !skipSteps -> {
                            state = State.FORWARD
                        }
                        progress > min && progress < max && !stopStep -> {
                            state = statePrev
                        }
                        selection != stepProgress - 1 && skipSteps -> {
                            selection = stepProgress - 1
                        }
                    }
                }
                State.FORWARD -> {
                    if (stepToMin) selection = stepProgress else if ((selection + 1) <= stepProgress) selection++

                    state = if (progress < next || statePrev == State.BACKWARD && progress <= next) {
                        State.BACKWARD
                    } else {
                        if (step < count - 1) onStepChanged(++step, true) else onMaxReached()
                        statePrev = state
                        State.STOP
                    }
                }
                State.BACKWARD -> {
                    if (stepToMin) selection = min else if ((selection + 1) >= stepProgress - 1) selection--

                    state = if (progress > prev || statePrev == State.FORWARD && progress >= prev) {
                        State.FORWARD
                    } else {
                        if (step > 1) onStepChanged(--step, false) else onMinReached()
                        statePrev = state
                        State.STOP
                    }
                }
            }
            
            stateMachineHandler.postDelayed(this, animationDuration)

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