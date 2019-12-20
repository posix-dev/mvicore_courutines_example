package com.shepeliev.livemvi.feature

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import com.shepeliev.livemvi.data.CalculatorApi
import com.shepeliev.livemvi.feature.CounterFeature.*
import com.shepeliev.livemvi.mvicore.rxFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SLOW_LOADING_THRESHOLD_MS = 1_000L

class CounterFeatureImpl(calculatorApi: CalculatorApi, coroutineScope: CoroutineScope) :
    ActorReducerFeature<Wish, Effect, State, Nothing>(
        initialState = State(),
        actor = ActorImpl(calculatorApi, coroutineScope),
        reducer = ReducerImpl()
    ), CounterFeature {

    private class ActorImpl(
        private val calculatorApi: CalculatorApi,
        coroutineScope: CoroutineScope
    ) : Actor<State, Wish, Effect>,
        CoroutineScope by coroutineScope {

        override fun invoke(state: State, action: Wish) = rxFlow<Effect> {
            when (action) {
                is Wish.Add -> {
                    onNext(Effect.Loading)

                    launch {
                        delay(SLOW_LOADING_THRESHOLD_MS)
                        onNext(Effect.StillLoading)
                    }

                    val result = calculatorApi.add(state.counter, action.n)
                    onNext(Effect.Success(result))
                    onComplete()
                }
            }
        }
    }

    private class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State {
            return when (effect) {
                is Effect.Loading -> state.copy(isLoading = true)
                is Effect.StillLoading -> state.copy(isSlowLoading = true)
                is Effect.Success -> state.copy(
                    counter = effect.payload,
                    isLoading = false,
                    isSlowLoading = false
                )
            }
        }
    }
}
