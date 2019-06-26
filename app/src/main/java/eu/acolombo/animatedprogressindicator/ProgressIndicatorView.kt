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

//    sealed class State {
//        object Stop : State()
//        object Prev : State()
//        object Next : State()
//    }
//
//    sealed class Event {
//        object OnProgressValid : Event()
//        object OnProgressStep : Event()
//    }
//
//    sealed class SideEffect {
//        object IncrementPosition : SideEffect()
//        object DecrementPosition : SideEffect()
//        object IncrementStep : SideEffect()
//    }
//
//    val stateMachine = StateMachine.create<State, Event, SideEffect> {
//        initialState(State.Stop)
//        state<State.Stop> {
//            on<Event.OnProgressValid> {
//                transitionTo(State.Next, SideEffect.LogMelted)
//            }
//        }
//        state<State.Liquid> {
//            on<Event.OnFroze> {
//                transitionTo(State.Solid, SideEffect.LogFrozen)
//            }
//            on<Event.OnVaporized> {
//                transitionTo(State.Gas, SideEffect.LogVaporized)
//            }
//        }
//        state<State.Gas> {
//            on<Event.OnCondensed> {
//                transitionTo(State.Liquid, SideEffect.LogCondensed)
//            }
//        }
//        onTransition {
//            val validTransition = it as? StateMachine.Transition.Valid ?: return@onTransition
//            when (validTransition.sideEffect) {
//                SideEffect.LogMelted -> logger.log(ON_MELTED_MESSAGE)
//                SideEffect.LogFrozen -> logger.log(ON_FROZEN_MESSAGE)
//                SideEffect.LogVaporized -> logger.log(ON_VAPORIZED_MESSAGE)
//                SideEffect.LogCondensed -> logger.log(ON_CONDENSED_MESSAGE)
//            }
//        }
//    }



    var progress = 0
    var min: Int = 0
    var max: Int = 100
    var state = State.STOP
    var step = 1

    enum class State { STOP, NEXT, PREV }

    private val stateMachineHandler = Handler()
    private var stateMachine: Runnable = object : Runnable {

        var exit = false

        override fun run() {

            val next = (max / (count - 1)) * step

            when (state) {
                State.STOP -> exitOrExecute {
                    if (progress > min && progress < max) state = State.NEXT
                }
                State.NEXT -> {
                    selection++
                    state = if (progress < next) {
                        State.PREV
                    } else {
                        step++
                        State.STOP
                    }
                }
                State.PREV -> {
                    selection--
                    state = State.NEXT
                }
            }

            if(!exit) stateMachineHandler.postDelayed(this, animationDuration)

        }

        fun exitOrExecute(execute: () -> Unit) {
            if (progress >= max) {
                exit = true
                stateMachineHandler.removeCallbacks(this)
            } else {
                execute()
            }
        }
    }

    init {
        stateMachine.run()
    }

}