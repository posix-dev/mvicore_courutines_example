package com.shepeliev.livemvi

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import com.shepeliev.livemvi.feature.CounterFeature
import io.reactivex.Observer
import kotlinx.coroutines.CoroutineScope
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit

class MainActivityTest : KoinTest {
    @get:Rule val mockitoRule = MockitoJUnit.rule()!!

    @Mock private lateinit var feature: CounterFeature

    @Before
    fun setUp() {
        val mockModule = module(override = true) {
            factory { (_: CoroutineScope) -> feature }
        }
        loadKoinModules(mockModule)
    }

    @Test
    fun displays_counter_value() {
        ActivityScenario.launch(MainActivity::class.java)

        argumentCaptor<Observer<CounterFeature.State>> {
            verify(feature).subscribe(capture())
            firstValue.onNext(CounterFeature.State(counter = 1))
        }

        onView(withId(R.id.textView_counter)).check(matches(withText("1")))
    }

    @Test
    fun buttons_are_disabled_while_loading_data() {
        ActivityScenario.launch(MainActivity::class.java)

        argumentCaptor<Observer<CounterFeature.State>> {
            verify(feature).subscribe(capture())
            firstValue.onNext(CounterFeature.State(isLoading = true))
        }

        onView(withId(R.id.button_decrement)).check(matches(not(isEnabled())))
        onView(withId(R.id.button_increment)).check(matches(not(isEnabled())))
    }

    @Test
    fun buttons_are_enabled_when_data_is_not_loading() {
        ActivityScenario.launch(MainActivity::class.java)

        argumentCaptor<Observer<CounterFeature.State>> {
            verify(feature).subscribe(capture())
            firstValue.onNext(CounterFeature.State(isLoading = false))
        }

        onView(withId(R.id.button_decrement)).check(matches(isEnabled()))
        onView(withId(R.id.button_increment)).check(matches(isEnabled()))
    }

    @Test
    fun displays_progress_when_loading_is_slow() {
        ActivityScenario.launch(MainActivity::class.java)

        argumentCaptor<Observer<CounterFeature.State>> {
            verify(feature).subscribe(capture())
            firstValue.onNext(CounterFeature.State(isLoading = true, isSlowLoading = true))
        }

        onView(withId(R.id.progress)).check(matches(isDisplayed()))
    }

    @Test
    fun progress_is_not_displayed_when_loading_is_not_slow() {
        ActivityScenario.launch(MainActivity::class.java)

        argumentCaptor<Observer<CounterFeature.State>> {
            verify(feature).subscribe(capture())
            firstValue.onNext(CounterFeature.State(isLoading = true, isSlowLoading = false))
        }

        onView(withId(R.id.progress)).check(matches(not(isDisplayed())))
    }
}
