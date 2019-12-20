package com.shepeliev.livemvi

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.shepeliev.livemvi.feature.CounterFeature
import com.shepeliev.livemvi.mvicore.toLivaData
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class CounterViewModel : ViewModel(), KoinComponent {
    private val feature: CounterFeature by inject { parametersOf(viewModelScope) }

    private val viewModelTransformer: (CounterFeature.State) -> Counter = {
        Counter(
            count = it.counter.toString(),
            progressVisibility = if (it.isSlowLoading) View.VISIBLE else View.GONE,
            areButtonsEnabled = !it.isLoading
        )
    }

    val counter = feature.toLivaData().map(viewModelTransformer)

    fun onIncrementClicked() {
        feature.accept(CounterFeature.Wish.Add(1))
    }

    fun onDecrementClicked() {
        feature.accept(CounterFeature.Wish.Add(-1))
    }

    data class Counter(
        val count: String,
        val progressVisibility: Int,
        val areButtonsEnabled: Boolean
    )
}
