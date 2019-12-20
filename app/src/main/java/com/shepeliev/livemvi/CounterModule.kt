package com.shepeliev.livemvi

import com.shepeliev.livemvi.data.CalculatorApi
import com.shepeliev.livemvi.data.RemoteCalculatorApi
import com.shepeliev.livemvi.feature.CounterFeature
import com.shepeliev.livemvi.feature.CounterFeatureImpl
import kotlinx.coroutines.CoroutineScope
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val counterModule = module {
    single<CalculatorApi> { RemoteCalculatorApi() }

    factory<CounterFeature> { (coroutineScope: CoroutineScope) ->
        CounterFeatureImpl(get(), coroutineScope)
    }

    viewModel { CounterViewModel() }
}
