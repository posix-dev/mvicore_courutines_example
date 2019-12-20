package com.shepeliev.livemvi.feature

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.shepeliev.livemvi.data.CalculatorApi
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@Suppress("EXPERIMENTAL_API_USAGE")
@RunWith(MockitoJUnitRunner::class)
class CounterFeatureImplTest {

    @Mock private lateinit var api: CalculatorApi

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var feature: CounterFeatureImpl

    @Before
    fun setUp() = runBlockingTest {
        Dispatchers.setMain(testDispatcher)
        feature = CounterFeatureImpl(api, MainScope())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun should_emit_correct_states_on_Add_wish() = runBlockingTest {
        // arrange
        whenever(api.add(any(), any())) doReturn 1
        val states = Observable.wrap(feature).test()

        // act
        feature.accept(CounterFeature.Wish.Add(1))

        // assert
        states.assertValues(
            CounterFeature.State(),
            CounterFeature.State(isLoading = true),
            CounterFeature.State(counter = 1, isLoading = false)
        )
    }

    @Test
    fun should_emit_correct_states_on_Add_wish_with_slow_API() = runBlocking<Unit> {
        // arrange
        whenever(api.add(any(), any())) doAnswer {
            testDispatcher.runBlockingTest { advanceTimeBy(5_000) }
            1
        }
        val states = Observable.wrap(feature).test()

        // act
        feature.accept(CounterFeature.Wish.Add(1))

        // assert
        states.assertValues(
            CounterFeature.State(),
            CounterFeature.State(isLoading = true),
            CounterFeature.State(isLoading = true, isSlowLoading = true),
            CounterFeature.State(counter = 1, isLoading = false, isSlowLoading = false)
        )
    }
}
