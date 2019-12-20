package com.shepeliev.livemvi.feature

import com.badoo.mvicore.feature.Feature
import com.shepeliev.livemvi.feature.CounterFeature.State
import com.shepeliev.livemvi.feature.CounterFeature.Wish

interface CounterFeature : Feature<Wish, State, Nothing> {
    sealed class Wish {
        data class Add(val n: Int) : Wish()
    }

    sealed class Effect {
        object Loading : Effect()
        object StillLoading : Effect()
        data class Success(val payload: Int) : Effect()
    }

    data class State(
        val counter: Int = 0,
        val isLoading: Boolean = false,
        val isSlowLoading: Boolean = false
    )
}
